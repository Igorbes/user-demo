package com.demo.users.dao;

import com.demo.users.config.VelocityJdbcTemplate;
import com.demo.users.exception.MinimalDataRequired;
import com.demo.users.om.EmailData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

import static com.demo.users.utils.Utils.createMap;

@Repository
@Transactional(rollbackFor = Exception.class)
public class EmailDAOImpl implements EmailDAO {
    @Autowired
    VelocityJdbcTemplate velocityJdbcTemplate;

    @Override
    public Integer remove(long userId, String email) {
        int affectCount = velocityJdbcTemplate.update(
                velocityJdbcTemplate.makeTemplate("/email/remove.sql.vm", createMap("user_id", userId, "email", email))
        );
        if(count(userId) == 0) throw new MinimalDataRequired("Must be >= 1");
        return affectCount;
    }

    @Override
    public Integer count(long userId) {
        return velocityJdbcTemplate.queryForObject(velocityJdbcTemplate.makeTemplate("/email/count.sql.vm", createMap("user_id", userId)), Integer.class);
    }

    @Override
    public Integer update(long userId, String fromEmail, String toEmail) {
        return velocityJdbcTemplate.update(
                velocityJdbcTemplate.makeTemplate("/email/update.sql.vm", createMap("user_id", userId, "from_email", fromEmail, "to_email", toEmail))
        );
    }

    @Override
    public Integer insert(long userId, String email) {
        return velocityJdbcTemplate.update(
                velocityJdbcTemplate.makeTemplate("/email/insert.sql.vm", createMap("user_id", userId, "email", email))
        );
    }

    @Override
    public Collection<EmailData> filter(Long[] userIds) {
        return velocityJdbcTemplate.query(
                velocityJdbcTemplate.makeTemplate("/email/select.sql.vm"),
                new Object[]{userIds},
                new RowMapper<EmailData>() {
                    @Override
                    public EmailData mapRow(ResultSet rs, int rowNum) throws SQLException {
                        EmailData emailData = new EmailData();
                        emailData.setId(rs.getLong("ID"));
                        emailData.setUserId(rs.getLong("USER_ID"));
                        emailData.setEmail(rs.getString("EMAIL"));
                        return emailData;
                    }
                }
        );
    }
}
