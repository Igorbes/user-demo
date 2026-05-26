package com.demo.users.dao;

import com.demo.users.config.VelocityJdbcTemplate;
import com.demo.users.exception.InvalidTransferException;
import com.demo.users.om.Account;
import com.demo.users.om.EmailData;
import com.demo.users.om.PhoneData;
import com.demo.users.om.User;
import com.demo.users.om.other.FilterUserRequest;
import com.demo.users.om.other.FilterUserResult;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import static com.demo.users.utils.Utils.createMap;

@Repository
@Transactional(rollbackFor = Exception.class)
public class UserDaoImpl implements UserDao {
    private static final String TEMPLATE_SELECT = "/user/select.sql.vm";

    @Autowired
    private VelocityJdbcTemplate velocityJdbcTemplate;

    @Autowired
    private AccountDAO accountDAO;

    @Autowired
    private PhoneDAO phoneDAO;

    @Autowired
    private EmailDAO emailDAO;

    @Override
    public User get(long id) {
        FilterUserResult search = filter(FilterUserRequest.builder().id(id).build());
        if(search == null || CollectionUtils.isEmpty(search.getUsers())) return null;

        return search.getUsers().iterator().next();
    }

    @Override
    public FilterUserResult filter(FilterUserRequest request) {
        Map<String, Object> filterParams = createMap(
                "id", request.getId(),
                "date_of_birth", Optional.ofNullable(request.getDateOfBirth()).orElse(null),
                "phone", request.getPhone(),
                "name", request.getName(),
                "email", request.getEmail(),
                "password", request.getPassword()
        );
        filterParams.put("count", true);
        Integer size = velocityJdbcTemplate.queryForObject(velocityJdbcTemplate.makeTemplate(TEMPLATE_SELECT, filterParams), Integer.class);

        filterParams.put("count", false);
        filterParams.put("limit", request.getSize());
        filterParams.put("offset", request.getOffset());
        List<User> users = velocityJdbcTemplate.query(
                velocityJdbcTemplate.makeTemplate(TEMPLATE_SELECT, filterParams),
                new RowMapper<User>() {
                    @Override
                    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                        User user = new User();
                        user.setId(rs.getLong("ID"));
                        user.setName(rs.getString("NAME"));
                        user.setDateOfBirth(rs.getDate("DATE_OF_BIRTH").toLocalDate());
                        user.setPassword(rs.getString("PASSWORD"));
                        return user;
                    }
                }
        );
        fillAccount(users);
        fillPhone(users);
        fillEmail(users);
        return FilterUserResult.builder()
                .users(users)
                .size(size)
                .page(request.getOffset())
                .build();
    }

    @Override
    public BigDecimal transfer(Long from, Long to, BigDecimal amount) {
        BigDecimal balance = accountDAO.getForUpdate(from);
        if(balance.compareTo(amount) < 0) throw new InvalidTransferException("Not enough amount");

        return accountDAO.transfer(from, to, amount);
    }

    private void fillPhone(List<User> users) {
        Long[] userIds = users.stream().map(User::getId).toArray(Long[]::new);
        Collection<PhoneData> phones = phoneDAO.filter(userIds);
        Map<Long, Collection<PhoneData>> phonesMap = new HashMap<>(phones.size());
        for (PhoneData phone : phones) {
            phonesMap.computeIfAbsent(phone.getUserId(), a -> new ArrayList<>(64)).add(phone);
        }
        for (User user : users) {
            user.setPhones(phonesMap.get(user.getId()));
        }
    }

    private void fillEmail(List<User> users) {
        Long[] userIds = users.stream().map(User::getId).toArray(Long[]::new);
        Collection<EmailData> emails = emailDAO.filter(userIds);
        Map<Long, Collection<EmailData>> emailMap = new HashMap<>(emails.size());
        for (EmailData phone : emails) {
            emailMap.computeIfAbsent(phone.getUserId(), a -> new ArrayList<>(64)).add(phone);
        }
        for (User user : users) {
            user.setEmails(emailMap.get(user.getId()));
        }
    }

    private void fillAccount(List<User> users) {
        Long[] userIds = users.stream().map(User::getId).toArray(Long[]::new);
        Map<Long, Account> accounts = accountDAO.filter(userIds).stream().collect(Collectors.toMap(Account::getUserId, v -> v));
        for (User user : users) {
            user.setAccount(accounts.get(user.getId()));
        }
    }
}
