-- main tables ----

-- configuration table --
CREATE TABLE IF NOT EXISTS configuration (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    description VARCHAR(255) NOT NULL,
    modified_datetime TIMESTAMP NOT NULL,
    created_datetime TIMESTAMP NOT NULL,
    created_userid BIGINT NOT NULL,
    last_modified_userid BIGINT NOT NULL
);
-- dt_type table --
CREATE TABLE IF NOT EXISTS dt_type (
    id SERIAL PRIMARY KEY,
    type VARCHAR(255) NOT NULL,
    modified_datetime TIMESTAMP NOT NULL,
    created_datetime TIMESTAMP NOT NULL,
    created_userid BIGINT NOT NULL,
    last_modified_userid BIGINT NOT NULL
);

-- subscriber table --
CREATE TABLE IF NOT EXISTS subscriber (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    description VARCHAR(255) NOT NULL,
    modified_datetime TIMESTAMP NOT NULL,
    created_datetime TIMESTAMP NOT NULL,
    created_userid BIGINT NOT NULL,
    last_modified_userid BIGINT NOT NULL
);

-- reference tables ---

-- boolean_dt table --
CREATE TABLE IF NOT EXISTS boolean_dt (
    id SERIAL PRIMARY KEY,
    configuration_id BIGINT NOT NULL,
    subscriber_id BIGINT NOT NULL,
    data_type_id BIGINT NOT NULL,
    -- FOREIGN KEY --
    FOREIGN KEY (configuration_id) REFERENCES configuration(id),
    FOREIGN KEY (subscriber_id) REFERENCES subscriber(id),
    FOREIGN KEY (data_type_id) REFERENCES dt_type(id),
    value BOOLEAN NOT NULL,
    modified_datetime TIMESTAMP NOT NULL,
    created_datetime TIMESTAMP NOT NULL,
    created_userid BIGINT NOT NULL,
    last_modified_userid BIGINT NOT NULL
);