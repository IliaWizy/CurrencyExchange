DO
$$
    BEGIN
        IF NOT EXISTS(SELECT 1 FROM pg_database WHERE datname = 'currency_exchange_db') THEN
            CREATE DATABASE currency_exchange_db;
        END IF;
    END;
$$;



GRANT ALL PRIVILEGES ON DATABASE currency_exchange_db TO wizy_user;

CREATE TABLE IF NOT EXISTS currency
(
    id   INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    code VARCHAR(3) UNIQUE  NOT NULL,
    name VARCHAR(50) UNIQUE NOT NULL,
    sign VARCHAR(4) UNIQUE  NOT NULL
);

CREATE UNIQUE INDEX currency_index
    ON currency (code);


CREATE TABLE IF NOT EXISTS exchanger
(
    id                 INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    base_currency_id   INT NOT NULL,
    target_currency_id INT NOT NULL,
    rate               DECIMAL(10, 4),
    FOREIGN KEY (base_currency_id) REFERENCES currency (id) ON DELETE CASCADE,
    FOREIGN KEY (target_currency_id) REFERENCES currency (id) ON DELETE CASCADE
);

CREATE UNIQUE INDEX exchanger_pair_index
    ON exchanger (base_currency_id, target_currency_id);

insert into currency (code, name, sign)
values ('USD', 'United States dollar', '$'),
       ('EUR', 'Euro', '€'),
       ('CYN', 'China', 'CH'),
       ('RUB', 'Деревянный', 'р.');

insert into exchanger (base_currency_id, target_currency_id, rate)
values (1, 2, 0.85),
       (1, 3, 5),
       (1, 4, 0.0090);
