CREATE TABLE public."USER"
(
    ID            bigserial NOT NULL,
    NAME          varchar(500),
    DATE_OF_BIRTH date,
    PASSWORD      varchar(500),
    CONSTRAINT user_pk PRIMARY KEY (ID)
);

CREATE TABLE public."ACCOUNT"
(
    ID      bigserial NOT NULL,
    USER_ID bigint NOT NULL,
    BALANCE bigint default 0,
    CONSTRAINT account_pk PRIMARY KEY (id),
    CONSTRAINT account_unique UNIQUE (USER_ID),
    CONSTRAINT account_user_fk FOREIGN KEY (USER_ID) REFERENCES public."USER" (ID) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE public."EMAIL_DATA"
(
    ID      bigserial NOT NULL,
    USER_ID bigint NOT NULL,
    EMAIL   varchar(200),
    CONSTRAINT email_data_pk PRIMARY KEY (ID),
    CONSTRAINT email_data_unique UNIQUE (EMAIL),
    CONSTRAINT email_data_user_fk FOREIGN KEY (USER_ID) REFERENCES public."USER" (ID) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE public."PHONE_DATA"
(
    ID      bigserial NOT NULL,
    USER_ID bigint NOT NULL,
    PHONE   varchar(13),
    CONSTRAINT phone_data_pk PRIMARY KEY (ID),
    CONSTRAINT phone_data_unique UNIQUE (PHONE),
    CONSTRAINT phone_data_user_fk FOREIGN KEY (USER_ID) REFERENCES public."USER" (ID) ON DELETE CASCADE ON UPDATE CASCADE
);