package com.demo.users.service;

public interface EmailService {
    int remove(long userId, String email);
    int change(long userId, String fromEmail, String toEmail);
    int add(long userId, String email);
}
