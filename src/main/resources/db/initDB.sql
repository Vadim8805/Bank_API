DROP TABLE IF EXISTS contragent;
DROP TABLE IF EXISTS card;
DROP TABLE IF EXISTS bill;
DROP TABLE IF EXISTS users;

CREATE TABLE users
(
    id               IDENTITY PRIMARY KEY auto_increment,
    version          INTEGER,
    first_name       VARCHAR,
    second_name      VARCHAR,
    patronymic       VARCHAR
);

CREATE TABLE bill
(
    id              IDENTITY PRIMARY KEY auto_increment,
    version         INTEGER,
    user_id         INTEGER         NOT NULL,
    bill_number     VARCHAR(20)     NOT NULL,
    balance         DECIMAL         default 0,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);
CREATE UNIQUE INDEX bill_unique_idx ON bill (bill_number);

CREATE TABLE card
(
    id              IDENTITY PRIMARY KEY auto_increment,
    version         Integer,
    user_id         INTEGER         NOT NULL,
    bill_id         INTEGER         NOT NULL,
    card_number     VARCHAR(16)     NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (bill_id) REFERENCES users (id) ON DELETE CASCADE
);
CREATE UNIQUE INDEX card_unique_idx ON card (card_number);

CREATE TABLE contragent
(
    id              IDENTITY PRIMARY KEY auto_increment,
    version         INTEGER,
    user_id         INTEGER         NOT NULL,
    bill_number     VARCHAR(20)     NOT NULL,
    deposit_sum     DECIMAL         default 0,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);
CREATE UNIQUE INDEX user_id_contragent_id_unique_idx ON contragent(id, user_id);