package com.demo.users.om;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@EqualsAndHashCode
@ToString(of = "balance")
public class Account {
    private @Getter @Setter Long id;
    private @Getter @Setter Long userId;
    private @Getter @Setter BigDecimal balance;
}
