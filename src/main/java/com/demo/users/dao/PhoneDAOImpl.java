package com.demo.users.dao;

import com.demo.users.config.VelocityJdbcTemplate;
import com.demo.users.om.PhoneData;
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
public class PhoneDAOImpl implements PhoneDAO {

    @Autowired
    VelocityJdbcTemplate velocityJdbcTemplate;

    @Override
    public Integer remove(long userId, String phone) {
        int affectCount = velocityJdbcTemplate.update(
                velocityJdbcTemplate.makeTemplate("/phone/remove.sql.vm", createMap("user_id", userId, "phone", phone))
        );
        if(count(userId) == 0) throw new RuntimeException("Must be >= 1");
        return affectCount;
    }

    @Override
    public Integer change(long userId, String from, String to) {
        return velocityJdbcTemplate.update(
                velocityJdbcTemplate.makeTemplate("/phone/update.sql.vm", createMap("user_id", userId, "from", from, "to", to))
        );
    }

    @Override
    public Integer add(long userId, String phone) {
        return velocityJdbcTemplate.update(
                velocityJdbcTemplate.makeTemplate("/phone/insert.sql.vm", createMap("user_id", userId, "phone", phone))
        );
    }

    @Override
    public Integer count(long userId) {
        return velocityJdbcTemplate.queryForObject(velocityJdbcTemplate.makeTemplate("/email/count.sql.vm", createMap("user_id", userId)), Integer.class);
    }

    @Override
    public Collection<PhoneData> filter(Long[] userIds) {
        return velocityJdbcTemplate.query(
                velocityJdbcTemplate.makeTemplate("/phone/select.sql.vm"),
                new Object[]{userIds},
                new RowMapper<PhoneData>() {
                    @Override
                    public PhoneData mapRow(ResultSet rs, int rowNum) throws SQLException {
                        PhoneData phoneData = new PhoneData();
                        phoneData.setId(rs.getLong("ID"));
                        phoneData.setUserId(rs.getLong("USER_ID"));
                        phoneData.setPhone(rs.getString("PHONE"));
                        return phoneData;
                    }
                }
        );
    }
}
