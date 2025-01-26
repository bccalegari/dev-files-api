CREATE TABLE IF NOT EXISTS "user".users (
    id BIGSERIAL PRIMARY KEY,
    slug VARCHAR(255) NOT NULL,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    deleted_at TIMESTAMP DEFAULT NULL,
    active BOOLEAN DEFAULT FALSE NOT NULL,
    CONSTRAINT unique_user_slug UNIQUE (slug),
    CONSTRAINT unique_user_username UNIQUE (username),
    CONSTRAINT unique_user_email UNIQUE (email)
);