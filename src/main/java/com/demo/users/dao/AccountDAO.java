package com.demo.users.dao;

import com.demo.users.om.Account;

import java.math.BigDecimal;
import java.util.Collection;

public interface AccountDAO {
    Collection<Account> filter(Long[] userIds);
    BigDecimal deposit(Long id, BigDecimal max, int percent);
    BigDecimal getForUpdate(Long from);
    BigDecimal transfer(Long from, Long to, BigDecimal amount);
}
