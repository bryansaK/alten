-- Drop unique constraint on username
ALTER TABLE users DROP CONSTRAINT IF EXISTS users_username_key;

-- Fallback in case a unique index exists with a different name
DO $$
BEGIN
    IF EXISTS (
        SELECT 1
        FROM pg_indexes
        WHERE schemaname = 'public'
          AND tablename = 'users'
          AND indexname = 'users_username_idx'
    ) THEN
        EXECUTE 'DROP INDEX users_username_idx';
    END IF;
END $$;
