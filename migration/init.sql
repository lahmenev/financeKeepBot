

CREATE TABLE IF NOT EXISTS account
(
    id                  serial primary key                                                          NOT NULL,
    name                varchar(250)                                                                NOT NULL,
    amount              bigint,
    created_date        timestamp with time zone default (now() at time zone 'Europe/Moscow'::text) NOT NULL,
    modified_date       timestamp with time zone default (now() at time zone 'Europe/Moscow'::text) NOT NULL,
    user_id             bigint                                                                      NOT NULL
    );

CREATE TABLE IF NOT EXISTS category
(
    id                  serial primary key                                                          NOT NULL,
    name                varchar(250)                                                                NOT NULL,
    user_id             bigint                                                                      NOT NULL,
    created_date        timestamp with time zone default (now() at time zone 'Europe/Moscow'::text) NOT NULL,
    modified_date       timestamp with time zone default (now() at time zone 'Europe/Moscow'::text) NOT NULL
    );

CREATE TABLE IF NOT EXISTS transactions
(
    id                  serial primary key                                                          NOT NULL,
    category_id         bigint,
    account_id          bigint                                                                      NOT NULL,
    amount              bigint                                                                      NOT NULL,
    type                varchar(40)                                                                 NOT NULL,
    created_date        timestamp with time zone default (now() at time zone 'Europe/Moscow'::text) NOT NULL,
    modified_date       timestamp with time zone default (now() at time zone 'Europe/Moscow'::text) NOT NULL
    );

CREATE TABLE IF NOT EXISTS users
(
    chat_id             bigint primary key                                                          NOT NULL,
    name                varchar(256),
    surname             varchar(256),
    login               varchar(256),
    created_date        timestamp with time zone default (now() at time zone 'Europe/Moscow'::text) NOT NULL,
    modified_date       timestamp with time zone default (now() at time zone 'Europe/Moscow'::text) NOT NULL
);

CREATE TABLE IF NOT EXISTS user_state
(
    id                  serial primary key                                                          NOT NULL,
    user_id             bigint                                                                      NOT NULL,
    last_state          varchar(32)                                                                 NOT NULL,
    created_date        timestamp with time zone default (now() at time zone 'Europe/Moscow'::text) NOT NULL,
    modified_date       timestamp with time zone default (now() at time zone 'Europe/Moscow'::text) NOT NULL
);

CREATE TABLE IF NOT EXISTS user_callback_state
(
    id                  serial primary key                                                          NOT NULL,
    user_state_id       bigint                                                                      NOT NULL,
    callback_state      varchar(32)                                                                 NOT NULL,
    value               varchar(256),
    created_date        timestamp with time zone default (now() at time zone 'Europe/Moscow'::text) NOT NULL,
    modified_date       timestamp with time zone default (now() at time zone 'Europe/Moscow'::text) NOT NULL
);

alter table transactions ADD CONSTRAINT fk_transactions_category FOREIGN KEY (category_id) REFERENCES category(id);
alter table transactions ADD CONSTRAINT fk_transactions_account FOREIGN KEY (account_id) REFERENCES account(id);
alter table account ADD CONSTRAINT fk_account_users FOREIGN KEY (user_id) REFERENCES users(chat_id);
alter table user_state ADD CONSTRAINT fk_user_state_users FOREIGN KEY (user_id) REFERENCES users(chat_id);
alter table user_callback_state ADD CONSTRAINT fk_user_callback_state_user_state FOREIGN KEY (user_state_id) REFERENCES user_state(id);
