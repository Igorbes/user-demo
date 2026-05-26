package com.demo.users.api.validation;

import com.demo.users.exception.InvalidFormatInputData;
import org.springframework.util.StringUtils;

import static com.demo.users.api.validation.EmailValidator.EMAIL_PATTERN;
import static com.demo.users.api.validation.PhoneValidator.PHONE_PATTERN;

public class UserAuthPhoneValidator implements Validable{
    @Override
    public void validate(Object[] args) {
        String phone = (String) args[0];
        String password = (String) args[1];
        if(StringUtils.isEmpty(password)) throw new InvalidFormatInputData();
        if(phone == null || !PHONE_PATTERN.matcher(phone).matches()) throw new InvalidFormatInputData();
    }
}
