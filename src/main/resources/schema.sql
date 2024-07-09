CREATE SEQUENCE IF NOT EXISTS users_seq
    INCREMENT 1 MINVALUE 1;

CREATE SEQUENCE IF NOT EXISTS cash_seq
    INCREMENT 1 MINVALUE 1;

CREATE SEQUENCE IF NOT EXISTS bank_seq
    INCREMENT 1 MINVALUE 1;

CREATE TABLE IF NOT EXISTS users
(
    id     BIGINT NOT NULL,
    dc_tag TEXT   NOT NULL,
    name   TEXT   NOT NULL,
    CONSTRAINT user_id_pk PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS cash
(
    id      BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    balance DOUBLE NOT NULL,
    CONSTRAINT cash_id_pk PRIMARY KEY (id),
    CONSTRAINT cash_user_id_fk FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS bank
(
    id      BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    balance DOUBLE NOT NULL,
    loan    DOUBLE,
    CONSTRAINT bank_id_pk PRIMARY KEY (id),
    CONSTRAINT bank_user_id_fk FOREIGN KEY (user_id) REFERENCES users (id)
);