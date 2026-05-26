package com.demo.users.dao;

import com.demo.users.om.Account;
import com.demo.users.om.PhoneData;

import java.util.Collection;

public interface PhoneDAO {
    Integer remove(long userId, String phone);
    Integer change(long userId, String fromPhone, String toPhone);
    Integer add(long userId, String phone);
    Integer count(long userId);
    Collection<PhoneData> filter(Long[] userIds);
}
