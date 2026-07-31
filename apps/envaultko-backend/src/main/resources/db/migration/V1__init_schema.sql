-- Walletko schema — matches the original Drizzle schema exactly

-- Auth tables (better-auth compatible)
CREATE TABLE IF NOT EXISTS "user" (
    id TEXT PRIMARY KEY,
    name TEXT NOT NULL,
    email TEXT NOT NULL UNIQUE,
    email_verified BOOLEAN NOT NULL DEFAULT FALSE,
    image TEXT,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS "session" (
    id TEXT PRIMARY KEY,
    expires_at TIMESTAMP WITH TIME ZONE NOT NULL,
    token TEXT NOT NULL UNIQUE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ip_address TEXT,
    user_agent TEXT,
    user_id TEXT NOT NULL REFERENCES "user"(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS "account" (
    id TEXT PRIMARY KEY,
    account_id TEXT NOT NULL,
    provider_id TEXT NOT NULL,
    user_id TEXT NOT NULL REFERENCES "user"(id) ON DELETE CASCADE,
    access_token TEXT,
    refresh_token TEXT,
    id_token TEXT,
    access_token_expires_at TIMESTAMP WITH TIME ZONE,
    refresh_token_expires_at TIMESTAMP WITH TIME ZONE,
    scope TEXT,
    password TEXT,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS "verification" (
    id TEXT PRIMARY KEY,
    identifier TEXT NOT NULL,
    value TEXT NOT NULL,
    expires_at TIMESTAMP WITH TIME ZONE NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE
);

-- Pots
CREATE TABLE IF NOT EXISTS "pots" (
    id TEXT PRIMARY KEY,
    name TEXT NOT NULL,
    percentage INTEGER NOT NULL,
    color TEXT NOT NULL,
    is_default BOOLEAN NOT NULL DEFAULT FALSE,
    user_id TEXT NOT NULL REFERENCES "user"(id) ON DELETE CASCADE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE,
    archived_at TIMESTAMP WITH TIME ZONE
);

CREATE INDEX IF NOT EXISTS idx_pots_user_id ON pots(user_id);
CREATE UNIQUE INDEX IF NOT EXISTS idx_pots_user_default ON pots(user_id) WHERE is_default = TRUE;

-- Tags
CREATE TABLE IF NOT EXISTS "tags" (
    id TEXT PRIMARY KEY,
    name TEXT NOT NULL,
    user_id TEXT NOT NULL REFERENCES "user"(id) ON DELETE CASCADE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (name, user_id)
);

CREATE INDEX IF NOT EXISTS idx_tags_user_id ON tags(user_id);

-- Transaction type enum as text (matching original)
CREATE TABLE IF NOT EXISTS "transactions" (
    id TEXT PRIMARY KEY,
    type TEXT NOT NULL CHECK (type IN ('income', 'expense', 'transfer', 'canceled_income', 'income_cancellation', 'canceled_expense', 'expense_cancellation')),
    name TEXT NOT NULL,
    amount BIGINT NOT NULL,
    user_id TEXT NOT NULL REFERENCES "user"(id) ON DELETE CASCADE,
    cancels_transaction_id TEXT REFERENCES "transactions"(id),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_transactions_user_id ON transactions(user_id);
CREATE INDEX IF NOT EXISTS idx_transactions_type_created_at ON transactions(type, created_at);
CREATE INDEX IF NOT EXISTS idx_transactions_created_at ON transactions(created_at);

-- Pot allocations (income distribution)
CREATE TABLE IF NOT EXISTS "pot_allocations" (
    id TEXT PRIMARY KEY,
    transaction_id TEXT NOT NULL REFERENCES "transactions"(id) ON DELETE CASCADE,
    pot_id TEXT NOT NULL REFERENCES "pots"(id) ON DELETE RESTRICT,
    amount BIGINT NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (transaction_id, pot_id)
);

CREATE INDEX IF NOT EXISTS idx_pot_allocations_pot_id ON pot_allocations(pot_id);

-- Expense allocations
CREATE TABLE IF NOT EXISTS "expense_allocations" (
    id TEXT PRIMARY KEY,
    transaction_id TEXT NOT NULL REFERENCES "transactions"(id) ON DELETE CASCADE,
    pot_id TEXT NOT NULL REFERENCES "pots"(id) ON DELETE RESTRICT,
    amount BIGINT NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (transaction_id, pot_id)
);

CREATE INDEX IF NOT EXISTS idx_expense_allocations_pot_id ON expense_allocations(pot_id);

-- Transaction-tag junction
CREATE TABLE IF NOT EXISTS "transaction_tags" (
    transaction_id TEXT NOT NULL REFERENCES "transactions"(id) ON DELETE CASCADE,
    tag_id TEXT NOT NULL REFERENCES "tags"(id) ON DELETE RESTRICT,
    PRIMARY KEY (transaction_id, tag_id)
);

CREATE INDEX IF NOT EXISTS idx_transaction_tags_tag_id ON transaction_tags(tag_id);

-- Saved views (tag_ids as native PostgreSQL text array)
CREATE TABLE IF NOT EXISTS "saved_views" (
    id TEXT PRIMARY KEY,
    user_id TEXT NOT NULL REFERENCES "user"(id) ON DELETE CASCADE,
    name TEXT NOT NULL,
    description TEXT,
    name_filter TEXT,
    tag_ids TEXT[] NOT NULL DEFAULT '{}',
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (name, user_id)
);

CREATE INDEX IF NOT EXISTS idx_saved_views_user_id ON saved_views(user_id);
