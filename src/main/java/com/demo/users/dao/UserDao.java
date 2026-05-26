package com.demo.users.dao;

import com.demo.users.om.User;
import com.demo.users.om.other.FilterUserRequest;
import com.demo.users.om.other.FilterUserResult;

import java.math.BigDecimal;

public interface UserDao {
    User get(long id);
    FilterUserResult filter(FilterUserRequest request);
    BigDecimal transfer(Long from, Long to, BigDecimal amount);
}
