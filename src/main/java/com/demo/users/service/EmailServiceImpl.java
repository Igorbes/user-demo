package com.demo.users.service;

import com.demo.users.dao.EmailDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {
    private static final Logger LOG = LoggerFactory.getLogger(EmailService.class);

    @Autowired EmailDAO emailDAO;

    @Override
    public int remove(long userId, String email) {
        return emailDAO.remove(userId, email);
    }

    @Override
    public int change(long userId, String fromEmail, String toEmail) {
        LOG.debug(String.format("Change email, userid: %s, from %s, to: %s", userId, fromEmail, toEmail));
        return emailDAO.update(userId, fromEmail, toEmail);
    }

    @Override
    public int add(long userId, String email) {
        LOG.debug(String.format("Add email, userid: %s, email: %s", userId, email));
        return emailDAO.insert(userId, email);
    }

}
