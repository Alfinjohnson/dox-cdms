-- CDMS SQL Schema V20230910194950_initial_version.sql ------

-- Configuration table --
CREATE TABLE IF NOT EXISTS configuration (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL,
    description TEXT,
    modified_datetime TIMESTAMPTZ DEFAULT NOW() NOT NULL,
    created_datetime TIMESTAMPTZ DEFAULT NOW() NOT NULL
);

-- Index the name column for faster lookups
CREATE INDEX idx_configuration_name ON configuration(name);

-- Subscriber table --
CREATE TABLE IF NOT EXISTS subscriber (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL,
    description TEXT,
    modified_datetime TIMESTAMPTZ DEFAULT NOW() NOT NULL,
    created_datetime TIMESTAMPTZ DEFAULT NOW() NOT NULL
);

-- Index the name column for faster lookups
CREATE INDEX idx_subscriber_name ON subscriber(name);

-- Data_dt table --
CREATE TABLE IF NOT EXISTS data_dt (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL,
    value TEXT  NOT NULL,
    enabled BOOLEAN DEFAULT true NOT NULL,
    modified_datetime TIMESTAMPTZ DEFAULT NOW() NOT NULL,
    created_datetime TIMESTAMPTZ DEFAULT NOW() NOT NULL
);

-- Index the name column for faster lookups
CREATE INDEX idx_data_dt_name ON data_dt(name);

-- Configuration Subscriber Data Mapping Table --
CREATE TABLE IF NOT EXISTS csd_mapping (
    id SERIAL PRIMARY KEY,
    dt_id INT NOT NULL,
    configuration_id INT NOT NULL,
    subscriber_id INT NOT NULL,
    modified_datetime TIMESTAMPTZ DEFAULT NOW() NOT NULL,
    created_datetime TIMESTAMPTZ DEFAULT NOW() NOT NULL,
    -- Index the foreign key columns for better performance
    FOREIGN KEY (dt_id) REFERENCES data_dt(id),
    FOREIGN KEY (configuration_id) REFERENCES configuration(id),
    FOREIGN KEY (subscriber_id) REFERENCES subscriber(id)
);

-- Index the foreign key columns for better performance
CREATE INDEX idx_csd_mapping_dt_id ON csd_mapping(dt_id);
CREATE INDEX idx_csd_mapping_configuration_id ON csd_mapping(configuration_id);
CREATE INDEX idx_csd_mapping_subscriber_id ON csd_mapping(subscriber_id);
