package com.demo.users.om;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.Collection;

@EqualsAndHashCode
@ToString(of = {"id", "name"})
public class User {
    private @Getter @Setter long id;
    private @Getter @Setter String name;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private @Getter @Setter LocalDate dateOfBirth;
    private @Getter @Setter String password;
    private @Getter @Setter Account account;
    private @Getter @Setter Collection<PhoneData> phones;
    private @Getter @Setter Collection<EmailData> emails;
}
