-- Migration V1

CREATE TABLE IF NOT EXISTS users (
    id VARCHAR(255) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    email_verified BOOLEAN NOT NULL DEFAULT FALSE,
    image VARCHAR(500),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS pots (
    id VARCHAR(255) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    percentage INT NOT NULL,
    color VARCHAR(50) NOT NULL,
    is_default BOOLEAN NOT NULL DEFAULT FALSE,
    user_id VARCHAR(255) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE,
    archived_at TIMESTAMP WITH TIME ZONE,
    CONSTRAINT fk_pots_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_pots_user_id ON pots(user_id);
CREATE UNIQUE INDEX IF NOT EXISTS idx_pots_user_default ON pots(user_id) WHERE is_default = TRUE;

CREATE TABLE IF NOT EXISTS transactions (
    id VARCHAR(255) PRIMARY KEY,
    type VARCHAR(50) NOT NULL,
    name VARCHAR(255) NOT NULL,
    amount BIGINT NOT NULL,
    user_id VARCHAR(255) NOT NULL,
    cancels_transaction_id VARCHAR(255),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_transactions_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_transactions_user_id ON transactions(user_id);
CREATE INDEX IF NOT EXISTS idx_transactions_type_created_at ON transactions(type, created_at);
CREATE INDEX IF NOT EXISTS idx_transactions_created_at ON transactions(created_at);

CREATE TABLE IF NOT EXISTS pot_allocations (
    id VARCHAR(255) PRIMARY KEY,
    transaction_id VARCHAR(255) NOT NULL REFERENCES transactions(id) ON DELETE CASCADE,
    pot_id VARCHAR(255) NOT NULL REFERENCES pots(id) ON DELETE RESTRICT,
    amount BIGINT NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE UNIQUE INDEX IF NOT EXISTS idx_pot_allocations_trans_pot ON pot_allocations(transaction_id, pot_id);
CREATE INDEX IF NOT EXISTS idx_pot_allocations_pot_id ON pot_allocations(pot_id);

CREATE TABLE IF NOT EXISTS expense_allocations (
    id VARCHAR(255) PRIMARY KEY,
    transaction_id VARCHAR(255) NOT NULL REFERENCES transactions(id) ON DELETE CASCADE,
    pot_id VARCHAR(255) NOT NULL REFERENCES pots(id) ON DELETE RESTRICT,
    amount BIGINT NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE UNIQUE INDEX IF NOT EXISTS idx_expense_allocations_trans_pot ON expense_allocations(transaction_id, pot_id);
CREATE INDEX IF NOT EXISTS idx_expense_allocations_pot_id ON expense_allocations(pot_id);

CREATE TABLE IF NOT EXISTS tags (
    id VARCHAR(255) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    user_id VARCHAR(255) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_tags_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT uq_tags_name_user UNIQUE(name, user_id)
);

CREATE INDEX IF NOT EXISTS idx_tags_user_id ON tags(user_id);

CREATE TABLE IF NOT EXISTS transaction_tags (
    transaction_id VARCHAR(255) NOT NULL REFERENCES transactions(id) ON DELETE CASCADE,
    tag_id VARCHAR(255) NOT NULL REFERENCES tags(id) ON DELETE RESTRICT,
    PRIMARY KEY (transaction_id, tag_id)
);

CREATE INDEX IF NOT EXISTS idx_transaction_tags_tag_id ON transaction_tags(tag_id);

CREATE TABLE IF NOT EXISTS saved_views (
    id VARCHAR(255) PRIMARY KEY,
    user_id VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    name_filter VARCHAR(255),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_saved_views_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT uq_saved_views_name_user UNIQUE(name, user_id)
);

CREATE INDEX IF NOT EXISTS idx_saved_views_user_id ON saved_views(user_id);

CREATE TABLE IF NOT EXISTS saved_view_tags (
    saved_view_id VARCHAR(255) NOT NULL REFERENCES saved_views(id) ON DELETE CASCADE,
    tag_id VARCHAR(255) NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_saved_view_tags_view_id ON saved_view_tags(saved_view_id);
