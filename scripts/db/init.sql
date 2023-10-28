DO
$$
    BEGIN
        IF NOT EXISTS(SELECT 1 FROM pg_roles WHERE rolname = 'wizy_user') THEN
            CREATE USER wizy_user WITH PASSWORD '1234567';
        END IF;
    END;
$$;

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
    id        INT PRIMARY KEY,
    code      VARCHAR(3) UNIQUE  NOT NULL,
    full_name VARCHAR(50) UNIQUE NOT NULL,
    sing      VARCHAR(4) UNIQUE  NOT NULL
);

CREATE UNIQUE INDEX currency_index
    ON currency (code);


CREATE TABLE IF NOT EXISTS exchanger
(
    id                 INT PRIMARY KEY,
    base_currency_id   INT NOT NULL,
    target_currency_id INT NOT NULL,
    rate               DECIMAL(6),
    FOREIGN KEY (base_currency_id) REFERENCES currency (id) ON DELETE CASCADE,
    FOREIGN KEY (target_currency_id) REFERENCES currency (id) ON DELETE CASCADE
);

CREATE UNIQUE INDEX exchanger_pair_index
    ON exchanger (base_currency_id, target_currency_id);
