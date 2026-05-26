package com.demo.users.dao;

import com.demo.users.om.EmailData;
import com.demo.users.om.PhoneData;

import java.util.Collection;

public interface EmailDAO {
    Integer remove(long userId, String email);
    Integer update(long userId, String fromEmail, String toEmail);
    Integer insert(long userId, String email);
    Integer count(long userId);
    Collection<EmailData> filter(Long[] userIds);
}
