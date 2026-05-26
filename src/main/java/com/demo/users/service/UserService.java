package com.demo.users.service;

import com.demo.users.om.User;
import com.demo.users.om.other.FilterUserRequest;
import com.demo.users.om.other.FilterUserResult;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface UserService {
    User get(long id);
    FilterUserResult filter(FilterUserRequest filter);
    BigDecimal transfer(Long from, Long to, BigDecimal amount1);
}
