-- configuration table --
CREATE TABLE IF NOT EXISTS configuration (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    modified_datetime TIMESTAMP NOT NULL,
    created_datetime TIMESTAMP NOT NULL,
    created_userid BIGINT NOT NULL,
    last_modified_userid BIGINT NOT NULL
);
-- boolean_config table --
CREATE TABLE IF NOT EXISTS boolean_config (
    id SERIAL PRIMARY KEY,
    configuration_id BIGINT NOT NULL,
    -- configuration table FOREIGN KEY --
    FOREIGN KEY (configuration_id) REFERENCES configuration(id),
    value BOOLEAN NOT NULL,
    modified_datetime TIMESTAMP NOT NULL,
    created_datetime TIMESTAMP NOT NULL,
    created_userid BIGINT NOT NULL,
    last_modified_userid BIGINT NOT NULL
);
-- type_config table --
CREATE TABLE IF NOT EXISTS type_config (
    id SERIAL PRIMARY KEY,
    configuration_id BIGINT NOT NULL,
    -- configuration table FOREIGN KEY --
    FOREIGN KEY (configuration_id) REFERENCES configuration(id),
    type VARCHAR(255) NOT NULL,
    modified_datetime TIMESTAMP NOT NULL,
    created_datetime TIMESTAMP NOT NULL,
    created_userid BIGINT NOT NULL,
    last_modified_userid BIGINT NOT NULL
);

-- service table --
CREATE TABLE IF NOT EXISTS service (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    description VARCHAR(255) NOT NULL,
    modified_datetime TIMESTAMP NOT NULL,
    created_datetime TIMESTAMP NOT NULL,
    created_userid BIGINT NOT NULL,
    last_modified_userid BIGINT NOT NULL
);

-- service_config table --
CREATE TABLE IF NOT EXISTS service_config (
    id SERIAL PRIMARY KEY,
    configuration_id BIGINT NOT NULL,
    service_id BIGINT NOT NULL,
    -- configuration table FOREIGN KEY --
    FOREIGN KEY (configuration_id) REFERENCES configuration(id),
    -- service table FOREIGN KEY --
    FOREIGN KEY (service_id) REFERENCES service(id),
    type VARCHAR(255) NOT NULL,
    modified_datetime TIMESTAMP NOT NULL,
    created_datetime TIMESTAMP NOT NULL,
    created_userid BIGINT NOT NULL,
    last_modified_userid BIGINT NOT NULL
);
