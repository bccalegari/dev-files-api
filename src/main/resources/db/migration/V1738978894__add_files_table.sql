CREATE TABLE IF NOT EXISTS file.files (
    id BIGSERIAL PRIMARY KEY,
    slug VARCHAR(255) NOT NULL,
    "name" VARCHAR(255) NOT NULL,
    mime_type VARCHAR(255) NOT NULL,
    "path" VARCHAR(255) NOT NULL,
    "size" BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    deleted_at TIMESTAMP DEFAULT NULL,
    CONSTRAINT unique_file_slug UNIQUE (slug),
    CONSTRAINT unique_file_name UNIQUE ("name"),
    CONSTRAINT unique_file_path UNIQUE ("path"),
    CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES "user".users (id)
);