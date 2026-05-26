package com.demo.users.om;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@EqualsAndHashCode
public class EmailData {
    private @Getter @Setter long id;
    private @Getter @Setter long userId;
    private @Getter @Setter String email;
}
