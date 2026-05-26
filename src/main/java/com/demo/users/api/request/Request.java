package com.demo.users.api.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

public class Request {

    public static class Filter {
        private @Getter @Setter Long id;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
        @JsonDeserialize(using = LocalDateDeserializer.class)
        @JsonSerialize(using = LocalDateSerializer.class)
        private @Getter @Setter LocalDate dateOfBirth;
        private @Getter @Setter String phone;
        private @Getter @Setter String name;
        private @Getter @Setter String email;
        private @Getter @Setter Integer size;
        private @Getter @Setter Integer offset;
    }


}
