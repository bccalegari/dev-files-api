CREATE TABLE IF NOT EXISTS "user".invitations (
    id BIGSERIAL PRIMARY KEY,
    slug VARCHAR(255) NOT NULL,
    user_id BIGINT NOT NULL,
    code VARCHAR(255) NOT NULL,
    consumed BOOLEAN DEFAULT FALSE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    deleted_at TIMESTAMP DEFAULT NULL,
    CONSTRAINT unique_invitation_code UNIQUE (code),
    CONSTRAINT unique_invitation_slug UNIQUE (slug),
    CONSTRAINT fk_invitation_user FOREIGN KEY (user_id) REFERENCES "user".users(id)
);