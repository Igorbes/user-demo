package com.demo.users.om;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode
public class PhoneData {
    private @Getter @Setter long id;
    private @Getter @Setter long userId;
    private @Getter @Setter String phone;
}
