package com.demo.users.api;

import com.demo.users.api.request.Request;
import com.demo.users.api.validation.*;
import com.demo.users.exception.UnauthorizedException;
import com.demo.users.om.User;
import com.demo.users.om.other.FilterUserRequest;
import com.demo.users.om.other.FilterUserResult;
import com.demo.users.service.EmailService;
import com.demo.users.service.PhoneService;
import com.demo.users.service.UserService;
import com.demo.users.utils.Utils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/user")
public class UserControllerImpl implements UserController {
    private static final String CLAIM = "USER_ID";

    @Autowired
    private EmailService emailService;

    @Autowired
    private PhoneService phoneService;

    @Autowired
    private UserService userService;

    @Override
    @RequestMapping(value = "/v1/email", method = RequestMethod.DELETE)
    @RequestParamValidator(EmailValidator.Remove.class)
    @CacheEvict(cacheNames = "user")
    public int removeEmail(@RequestParam String email, @RequestHeader(value = "Authorization") String auth) {
        return emailService.remove(getUserId(auth), email);
    }

    @Override
    @RequestMapping(value = "/v1/email", method = RequestMethod.PUT)
    @RequestParamValidator(EmailValidator.Update.class)
    @CacheEvict(cacheNames = "user")
    public int changeEmail(@RequestParam String from, @RequestParam String to, @RequestHeader(value = "Authorization") String auth) {
        return emailService.change(getUserId(auth), from, to);
    }

    @Override
    @RequestMapping(value = "/v1/email", method = RequestMethod.POST)
    @RequestParamValidator(EmailValidator.Add.class)
    @CacheEvict(cacheNames = "user")
    public int addEmail(@RequestParam String email, @RequestHeader(value = "Authorization") String auth) {
        return emailService.add(getUserId(auth), email);
    }

    @Override
    @RequestMapping(value = "/v1/phone", method = RequestMethod.DELETE)
    @RequestParamValidator(PhoneValidator.Remove.class)
    @CacheEvict(cacheNames = "user")
    public int removePhone(@RequestParam String phone, @RequestHeader(value = "Authorization") String auth) {
        return phoneService.remove(getUserId(auth), phone);
    }

    @Override
    @RequestMapping(value = "/v1/phone", method = RequestMethod.PUT)
    @RequestParamValidator(PhoneValidator.Update.class)
    @CacheEvict(cacheNames = "user")
    public int changePhone(@RequestParam String from, @RequestParam String to, @RequestHeader(value = "Authorization") String auth) {
        return phoneService.change(getUserId(auth), from, to);
    }

    @Override
    @RequestMapping(value = "/v1/phone", method = RequestMethod.POST)
    @RequestParamValidator(PhoneValidator.Add.class)
    @CacheEvict(cacheNames = "user")
    public int addPhone(@RequestParam String phone, @RequestHeader(value = "Authorization") String auth) {
        return phoneService.add(getUserId(auth), phone);
    }

    @Override
    @RequestMapping(value = "/v1/filter", method = RequestMethod.POST)
    @ResponseBody
    @RequestParamValidator(UserFilterValidator.class)
    @Cacheable("user")
    public FilterUserResult filter(@RequestBody(required = false) Request.Filter request) {
        return userService.filter(
                FilterUserRequest.builder()
                        .id(Optional.ofNullable(request).map(Request.Filter::getId).orElse(null))
                        .dateOfBirth(Optional.ofNullable(request).map(Request.Filter::getDateOfBirth).orElse(null))
                        .phone(Optional.ofNullable(request).map(Request.Filter::getPhone).orElse(null))
                        .name(Optional.ofNullable(request).map(Request.Filter::getName).orElse(null))
                        .email(Optional.ofNullable(request).map(Request.Filter::getEmail).orElse(null))
                        .size(Optional.ofNullable(request).map(Request.Filter::getSize).orElse(null))
                        .offset(Optional.ofNullable(request).map(Request.Filter::getOffset).orElse(null))
                        .build()
        );
    }

    @Override
    @RequestMapping(value = "/v1/auth/email", method = RequestMethod.POST)
    @ResponseBody
    @RequestParamValidator(UserAuthEmailValidator.class)
    public String authEmail(@RequestParam String email, @RequestParam String password) {
        return auth(FilterUserRequest.builder()
                .email(email)
                .password(password)
                .build());
    }

    @Override
    @RequestMapping(value = "/v1/auth/phone", method = RequestMethod.POST)
    @ResponseBody
    @RequestParamValidator(UserAuthPhoneValidator.class)
    public String authPhone(@RequestParam String phone, @RequestParam String password) {
        return auth(FilterUserRequest.builder()
                .phone(phone)
                .password(password)
                .build());
    }

    @Override
    @RequestMapping(value = "/v1/account/transfer", method = RequestMethod.POST)
    @ResponseBody
    @CacheEvict(cacheNames = "user")
    public BigDecimal transfer(@RequestParam Long transferTo, @RequestParam  BigDecimal amount, @RequestHeader(value = "Authorization") String auth) {
        return userService.transfer(this.getUserId(auth), transferTo, amount);
    }

    private String auth(FilterUserRequest request) {
        FilterUserResult result = userService.filter(request);
        if(CollectionUtils.isEmpty(result.getUsers())) throw new UnauthorizedException();

        User user = result.getUsers().iterator().next();
        return Jwts.builder()
                .setClaims(Utils.createMap(CLAIM, user.getId()))
                .setSubject(user.getName())
                .compact();
    }

    private Long getUserId(String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        Claims claims = Jwts.parser()
                .parseClaimsJwt(token)
                .getBody();
        Long userId = claims.get(CLAIM, Long.class);
        if(userId == null) throw new UnauthorizedException();
        return userId;
    }

}
