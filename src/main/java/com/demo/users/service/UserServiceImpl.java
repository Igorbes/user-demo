package com.demo.users.service;

import com.demo.users.dao.UserDao;
import com.demo.users.exception.InvalidTransferException;
import com.demo.users.om.User;
import com.demo.users.om.other.FilterUserRequest;
import com.demo.users.om.other.FilterUserResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserDao userDao;

    @Override
    public User get(long id) {
        LOG.debug(String.format("get user, id: %s", id));
        return userDao.get(id);
    }

    @Override
    public FilterUserResult filter(FilterUserRequest filter) {
        LOG.debug(String.format("Filter user, %s", filter));
        return userDao.filter(filter);
    }

    @Override
    public BigDecimal transfer(Long from, Long to, BigDecimal amount) {
        LOG.debug(String.format("Transfer from id: %s, to id: %s, amount: %s", from, to, amount));
        if(get(to) == null) throw new InvalidTransferException("User not found for transfer amount");

        return userDao.transfer(from, to, amount);
    }
}
