CREATE TABLE IF NOT EXISTS TokenPrice (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    token_symbol VARCHAR(10) NOT NULL,
    price NUMERIC(20, 8) NOT NULL,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_token_symbol ON TokenPrice (token_symbol);
CREATE INDEX idx_timestamp ON TokenPrice (timestamp);
