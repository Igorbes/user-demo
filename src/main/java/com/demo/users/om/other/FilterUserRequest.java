package com.demo.users.om.other;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Builder
@ToString
public class FilterUserRequest {
    private @Getter @Setter Long id;
    private @Getter @Setter LocalDate dateOfBirth;
    private @Getter @Setter String phone;
    private @Getter @Setter String name;
    private @Getter @Setter String email;
    private @Getter @Setter String password;
    private @Getter @Setter Integer size;
    private @Getter @Setter Integer offset;
}
