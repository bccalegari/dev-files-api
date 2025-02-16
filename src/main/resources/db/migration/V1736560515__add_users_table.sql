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
    CONSTRAINT unique_user_slug UNIQUE (slug)
);

CREATE UNIQUE INDEX IF NOT EXISTS idx_unique_user_username
    ON "user".users (username)
    WHERE deleted_at IS NULL;

CREATE UNIQUE INDEX IF NOT EXISTS idx_unique_user_email
    ON "user".users (email)
    WHERE deleted_at IS NULL;