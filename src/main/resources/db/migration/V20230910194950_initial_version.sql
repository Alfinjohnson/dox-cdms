-- Dox - CDMS SQL Schema V20230910194950_initial_version.sql ------
-- Configuration table --
CREATE TABLE IF NOT EXISTS configuration (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL,
    description TEXT,
    enabled BOOLEAN DEFAULT true NOT NULL,
    modified_datetime TIMESTAMPTZ DEFAULT NOW() NOT NULL,
    created_datetime TIMESTAMPTZ DEFAULT NOW() NOT NULL
);

-- Index the name column for faster lookups
CREATE INDEX idx_configuration_name ON configuration(name);

-- Subscriber table --

CREATE TABLE IF NOT EXISTS subscriber (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    data_type TEXT NOT NULL,
    string_dt TEXT,
    double_dt DOUBLE PRECISION,
    integer_dt INT,
    float_dt REAL,
    boolean_dt BOOLEAN,
    json_dt JSON,
    enabled BOOLEAN DEFAULT true NOT NULL,
    modified_datetime TIMESTAMPTZ DEFAULT NOW() NOT NULL,
    created_datetime TIMESTAMPTZ DEFAULT NOW() NOT NULL
);

-- Index the name column for faster lookups
CREATE INDEX idx_subscriber_name ON subscriber(name);

-- Configuration Subscriber Data Mapping Table --
CREATE TABLE IF NOT EXISTS csd_mapping (
    id SERIAL PRIMARY KEY,
    configuration_id INT NOT NULL,
    subscriber_id INT NOT NULL,
    modified_datetime TIMESTAMPTZ DEFAULT NOW() NOT NULL,
    created_datetime TIMESTAMPTZ DEFAULT NOW() NOT NULL,
    -- Index the foreign key columns for better performance
    FOREIGN KEY (configuration_id) REFERENCES configuration(id),
    FOREIGN KEY (subscriber_id) REFERENCES subscriber(id)
);

-- Index the foreign key columns for better performance
CREATE INDEX idx_csd_mapping_configuration_id ON csd_mapping(configuration_id);
CREATE INDEX idx_csd_mapping_subscriber_id ON csd_mapping(subscriber_id);
