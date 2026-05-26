package com.demo.users.service;

import com.demo.users.dao.AccountDAO;
import com.demo.users.dao.UserDao;
import com.demo.users.om.Account;
import com.demo.users.om.User;
import com.demo.users.om.other.FilterUserRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class AccountServiceImpl implements AccountService {
    private static final Logger LOG = LoggerFactory.getLogger(AccountService.class);

    @Autowired
    private AccountDAO accountDAO;

    @Autowired
    private UserDao userDao;

    @Value("${app.deposit.percent}")
    private int percent;

    @Value("${app.deposit.max}")
    private int max;

    private static final Map<Long, BigDecimal> initial = new ConcurrentHashMap<>();
    private static final ReentrantLock lock = new ReentrantLock(true);

    @Override
    @Scheduled(cron = "${app.deposit}")
    public void deposit() {
        if(lock.tryLock()) {
            try {
                LOG.debug("Start deposit percent");
                if(initial.isEmpty()) {
                    Collection<User> users = userDao.filter(FilterUserRequest.builder().build()).getUsers();
                    Collection<Account> accounts = accountDAO.filter(users.stream().map(User::getId).toArray(Long[]::new));
                    for (Account account : accounts) {
                        initial.put(account.getId(), account.getBalance());
                    }
                }
                for (Map.Entry<Long, BigDecimal> entry : initial.entrySet()) {
                    try {
                        accountDAO.deposit(entry.getKey(), entry.getValue().multiply(new BigDecimal(1d * max / 100, MathContext.DECIMAL64)), percent);
                    } catch (Exception e) {
                        LOG.error(String.format("Account Balance is reached"));
                    }
                }
            } finally {
                lock.unlock();
            }
        }
    }
}
