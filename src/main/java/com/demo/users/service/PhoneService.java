package com.demo.users.service;

public interface PhoneService {
    int remove(long userId, String phone);
    int change(long userId, String fromPhone, String toPhone);
    int add(long userId, String phone);
}
