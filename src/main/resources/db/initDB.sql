DROP TABLE IF EXISTS user_roles;
DROP TABLE IF EXISTS card;
DROP TABLE IF EXISTS bill;
DROP TABLE IF EXISTS users;

CREATE TABLE users
(
    id               IDENTITY PRIMARY KEY auto_increment,
    first_name       VARCHAR,
    second_name      VARCHAR,
    patronymic       VARCHAR
);

CREATE TABLE bill
(
    id              IDENTITY PRIMARY KEY auto_increment,
    user_id         INTEGER         NOT NULL,
    bill_number      VARCHAR(20)     NOT NULL,
    balance         DOUBLE          default 0,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);
CREATE UNIQUE INDEX bill_unique_idx ON bill (bill_number);

CREATE TABLE card
(
    id              IDENTITY PRIMARY KEY auto_increment,
    user_id         INTEGER         NOT NULL,
    bill_id         INTEGER         NOT NULL,
    card_number      VARCHAR(16)     NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (bill_id) REFERENCES users (id) ON DELETE CASCADE
);
CREATE UNIQUE INDEX card_unique_idx ON card (card_number);