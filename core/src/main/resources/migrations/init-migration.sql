CREATE TABLE bank
(
    id              SERIAL PRIMARY KEY,
    name            VARCHAR(255) NOT NULL UNIQUE,
    bank_commission INT          NOT NULL
);

CREATE TABLE bank_percent
(
    id            SERIAL PRIMARY KEY,
    bank_id       INT NOT NULL,
    money_amount  INT,
    money_kopecks SMALLINT,
    percent       INT,
    FOREIGN KEY (bank_id) REFERENCES bank (id)
);

-- CREATE TABLE bank_commission
-- (
--     id            SERIAL PRIMARY KEY,
--     bank_id       INT NOT NULL,
--     money_amount  INT,
--     money_kopecks SMALLINT,
--     commission    INT,
--     FOREIGN KEY (bank_id) REFERENCES bank (id)
-- );

CREATE TABLE client
(
    id                            SERIAL PRIMARY KEY,
    name                          VARCHAR(255) NOT NULL,
    birthday                      DATE         NOT NULL,
    address                       VARCHAR(255),
    series                        VARCHAR(50),
    number                        VARCHAR(50),
    bank_id                       INT,
    is_subscribed_to_notification BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (bank_id) REFERENCES bank (id)
);

CREATE TABLE bank_account
(
    id                SERIAL PRIMARY KEY,
    money_amount      INT,
    money_kopecks     SMALLINT,
    bank_account_type VARCHAR(255),
    client_id         INT,
    bank_id           INT,
    opening_date      DATE,
    FOREIGN KEY (client_id) REFERENCES client (id),
    FOREIGN KEY (bank_id) REFERENCES bank (id)
);

CREATE TABLE credit
(
    id            INT,
    limit_amount  INT,
    limit_kopecks SMALLINT,
    FOREIGN KEY (id) REFERENCES bank_account (id)
);

CREATE TABLE debit
(
    id INT,
    FOREIGN KEY (id) REFERENCES bank_account (id)
);

CREATE TABLE deposit
(
    id                  INT,
    start_money_amount  INT,
    start_money_kopecks SMALLINT,
    date_of_closing     DATE NOT NULL,
    FOREIGN KEY (id) REFERENCES bank_account (id)
);

CREATE TABLE message
(
    id        SERIAL PRIMARY KEY,
    message   TEXT,
    client_id INT,
    FOREIGN KEY (client_id) REFERENCES client (id)
);

CREATE TABLE transaction
(
    id              UUID PRIMARY KEY,
    date            DATE,
    amount          INT,
    kopecks         SMALLINT,
    bank_account_id INT,
    operation_type  VARCHAR(50),
    FOREIGN KEY (bank_account_id) REFERENCES bank_account (id)
);
