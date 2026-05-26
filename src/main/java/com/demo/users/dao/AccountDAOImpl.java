package com.demo.users.dao;

import com.demo.users.config.VelocityJdbcTemplate;
import com.demo.users.exception.AccountReachedException;
import com.demo.users.om.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.MathContext;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

import static com.demo.users.utils.Utils.createMap;

@Repository
@Transactional(rollbackFor = Exception.class)
public class AccountDAOImpl implements AccountDAO {
    @Autowired
    VelocityJdbcTemplate velocityJdbcTemplate;

    @Override
    public Collection<Account> filter(Long[] userIds) {
        return velocityJdbcTemplate.query(
                velocityJdbcTemplate.makeTemplate("/account/select.sql.vm"),
                new Object[]{userIds},
        new RowMapper<Account>() {
                    @Override
                    public Account mapRow(ResultSet rs, int rowNum) throws SQLException {
                        Account account = new Account();
                        account.setId(rs.getLong("ID"));
                        account.setUserId(rs.getLong("USER_ID"));
                        account.setBalance(rs.getBigDecimal("BALANCE"));
                        return account;
                    }
                }
        );
    }

    public BigDecimal deposit(Long id, BigDecimal maxBalance, int percent) {
        BigDecimal balance = new BigDecimal(
                velocityJdbcTemplate.queryForObject(velocityJdbcTemplate.makeTemplate("/account/deposit.sql.vm", createMap(
                        "id", id,
                        "percent", percent
                )), Long.class),
                MathContext.DECIMAL64
        );
        if(balance.compareTo(maxBalance) > 0) throw new AccountReachedException();
        return balance;
    }

    @Override
    public BigDecimal getForUpdate(Long userId) {
        return new BigDecimal(
                velocityJdbcTemplate.queryForObject(velocityJdbcTemplate.makeTemplate("/account/select.for.update.sql.vm", createMap(
                        "user_id", userId
                )), Long.class),
                MathContext.DECIMAL64
        );
    }

    @Override
    public BigDecimal transfer(Long from, Long to, BigDecimal amount) {
        return new BigDecimal(
                velocityJdbcTemplate.queryForObject(velocityJdbcTemplate.makeTemplate("/account/transfer.sql.vm", createMap(
                        "from", from,
                        "to", to,
                        "amount", amount
                )), Long.class),
                MathContext.DECIMAL64
        );
    }
}
