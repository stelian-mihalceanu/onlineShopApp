ALTER TABLE users
    ADD COLUMN IF NOT EXISTS username VARCHAR(50);

UPDATE users
SET username = LEFT(
    REGEXP_REPLACE(LOWER(SPLIT_PART(email, '@', 1)), '[^a-z0-9_]', '_', 'g')
    || '_' || id,
    50
)
WHERE username IS NULL;

ALTER TABLE users
    ALTER COLUMN username SET NOT NULL;

CREATE UNIQUE INDEX IF NOT EXISTS uk_users_username
    ON users (username);