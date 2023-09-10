-- configuration table --
CREATE TABLE IF NOT EXISTS configuration (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    modified_datetime TIMESTAMP NOT NULL,
    created_datetime TIMESTAMP NOT NULL,
    created_userid BIGINT,
    last_modified_userid BIGINT
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
    created_userid BIGINT,
    last_modified_userid BIGINT
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
    created_userid BIGINT,
    last_modified_userid BIGINT
);
