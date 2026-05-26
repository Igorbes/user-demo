package com.demo.users.om.other;

import com.demo.users.om.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Collection;
import java.util.List;

@Builder
@ToString
public class FilterUserResult {
    private @Getter @Setter List<User> users;
    private @Getter @Setter Integer size;
    private @Getter @Setter Integer page;
}
