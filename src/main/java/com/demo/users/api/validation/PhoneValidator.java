package com.demo.users.api.validation;

import com.demo.users.exception.InvalidFormatInputData;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.regex.Pattern;

public class PhoneValidator {
    public static final Pattern PHONE_PATTERN = Pattern.compile("^7(\\d){10}$");

    public static class Update implements Validable {
        @Override
        public void validate(Object[] args) {
            if(StringUtils.isEmpty(args[0]) || !PHONE_PATTERN.matcher((String) args[0]).matches()) throw new InvalidFormatInputData();
            if(StringUtils.isEmpty(args[1]) || !PHONE_PATTERN.matcher((String) args[1]).matches()) throw new InvalidFormatInputData();
        }
    }

    public static class Add implements Validable {
        @Override
        public void validate(Object[] args) {
            if(StringUtils.isEmpty(args[0]) || !PHONE_PATTERN.matcher((String) args[0]).matches()) throw new InvalidFormatInputData();
        }
    }

    public static class Remove implements Validable {
        @Override
        public void validate(Object[] args) {
            if(StringUtils.isEmpty(args[0]) || !PHONE_PATTERN.matcher((String) args[0]).matches()) throw new InvalidFormatInputData();
        }
    }
}
