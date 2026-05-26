package com.demo.users.api.validation;

import com.demo.users.api.request.Request;
import com.demo.users.exception.InvalidFormatInputData;
import org.springframework.util.StringUtils;

import static com.demo.users.api.validation.EmailValidator.EMAIL_PATTERN;
import static com.demo.users.api.validation.PhoneValidator.PHONE_PATTERN;

public class UserAuthEmailValidator implements Validable{
    @Override
    public void validate(Object[] args) {
        String email = (String) args[0];
        String password = (String) args[1];
        if(StringUtils.isEmpty(password)) throw new InvalidFormatInputData();
        if(email == null || !EMAIL_PATTERN.matcher(email).matches()) throw new InvalidFormatInputData();
    }
}
