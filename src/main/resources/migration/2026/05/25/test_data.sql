insert into public."USER"(NAME, DATE_OF_BIRTH, PASSWORD) values ('Иван', '01.05.1993'::timestamp, 'mypassword1');
insert into public."USER"(NAME, DATE_OF_BIRTH, PASSWORD) values ('Максим', '05.10.1996'::timestamp, 'mypassword2');
insert into public."USER"(NAME, DATE_OF_BIRTH, PASSWORD) values ('Андрей', '07.12.1990'::timestamp, 'mypassword3');

insert into public."ACCOUNT"(USER_ID, BALANCE) values ((select ID from public."USER" where NAME = 'Иван'), 10000);
insert into public."ACCOUNT"(USER_ID, BALANCE) values ((select ID from public."USER" where NAME = 'Максим'), 20000);
insert into public."ACCOUNT"(USER_ID, BALANCE) values ((select ID from public."USER" where NAME = 'Андрей'), 30000);

insert into public."EMAIL_DATA"(USER_ID, EMAIL) values ((select ID from public."USER" where NAME = 'Иван'), 'ivan@mail.ru');
insert into public."EMAIL_DATA"(USER_ID, EMAIL) values ((select ID from public."USER" where NAME = 'Максим'), 'max@mail.ru');
insert into public."EMAIL_DATA"(USER_ID, EMAIL) values ((select ID from public."USER" where NAME = 'Андрей'), 'andrey@mail.ru');

insert into public."PHONE_DATA"(USER_ID, PHONE) values ((select ID from public."USER" where NAME = 'Иван'), '79226820000');
insert into public."PHONE_DATA"(USER_ID, PHONE) values ((select ID from public."USER" where NAME = 'Максим'), '79226821111');
insert into public."PHONE_DATA"(USER_ID, PHONE) values ((select ID from public."USER" where NAME = 'Андрей'), '79226822222');