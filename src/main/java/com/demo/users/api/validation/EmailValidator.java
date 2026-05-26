package com.demo.users.api.validation;

import com.demo.users.exception.InvalidFormatInputData;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.regex.Pattern;

public class EmailValidator {
    public static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9а-яА-Я._%+-]+@[a-zA-Z0-9а-яА-Я.-]+\\.[a-zA-Zа-яА-Я]{2,}$");

    public static class Update implements Validable {
        @Override
        public void validate(Object[] args) {
            if(StringUtils.isEmpty(args[0]) || !EMAIL_PATTERN.matcher((CharSequence) args[0]).matches()) throw new InvalidFormatInputData();
            if(StringUtils.isEmpty(args[1]) || !EMAIL_PATTERN.matcher((CharSequence) args[1]).matches()) throw new InvalidFormatInputData();
        }
    }

    public static class Add implements Validable {
        @Override
        public void validate(Object[] args) {
            if(StringUtils.isEmpty(args[0]) || !EMAIL_PATTERN.matcher((CharSequence) args[0]).matches()) throw new InvalidFormatInputData();
        }
    }

    public static class Remove implements Validable {
        @Override
        public void validate(Object[] args) {
            if(StringUtils.isEmpty(args[0]) || !EMAIL_PATTERN.matcher((CharSequence) args[0]).matches()) throw new InvalidFormatInputData();
        }
    }
}
