package com.demo.users.api.validation;

import com.demo.users.api.request.Request;
import com.demo.users.exception.InvalidFormatInputData;

import java.time.LocalDate;

import static com.demo.users.api.validation.EmailValidator.EMAIL_PATTERN;
import static com.demo.users.api.validation.PhoneValidator.PHONE_PATTERN;

public class UserFilterValidator implements Validable{
    @Override
    public void validate(Object[] args) {
        Object arg = args[0];
        if(arg instanceof Request.Filter) {
            Request.Filter request = (Request.Filter) arg;
            String email = request.getEmail();
            String phone = request.getPhone();
            if(email != null && !EMAIL_PATTERN.matcher(email).matches()) throw new InvalidFormatInputData();
            if(phone != null && !PHONE_PATTERN.matcher(phone).matches()) throw new InvalidFormatInputData();
        }
    }
}
