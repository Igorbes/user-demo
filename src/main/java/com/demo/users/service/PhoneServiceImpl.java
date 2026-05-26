package com.demo.users.service;

import com.demo.users.dao.PhoneDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PhoneServiceImpl implements PhoneService {
    private static final Logger LOG = LoggerFactory.getLogger(PhoneService.class);

    @Autowired
    private PhoneDAO phoneDAO;

    @Override
    public int remove(long userId, String phone) {
        return phoneDAO.remove(userId, phone);
    }

    @Override
    public int change(long userId, String fromPhone, String toPhone) {
        LOG.debug(String.format("Change phone, userid: %s, from %s, to: %s", userId, fromPhone, toPhone));
        return phoneDAO.change(userId, fromPhone, toPhone);
    }

    @Override
    public int add(long userId, String phone) {
        LOG.debug(String.format("Add phone, userid: %s, phone: %s", userId, phone));
        return phoneDAO.add(userId, phone);
    }
}
