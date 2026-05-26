package com.demo.users.api;

import com.demo.users.api.request.Request;
import com.demo.users.om.other.FilterUserResult;

import java.math.BigDecimal;

public interface UserController {
    int removeEmail(String email, String auth);
    int changeEmail(String fromEmail, String toEmail, String auth);
    int addEmail(String email, String auth);

    int removePhone(String phone, String auth);
    int changePhone(String fromPhone, String toPhone, String auth);
    int addPhone(String phone, String auth);

    FilterUserResult filter(Request.Filter request);

    String authEmail(String email, String password);
    String authPhone(String phone, String password);

    BigDecimal transfer(Long to, BigDecimal amount, String auth);
}
