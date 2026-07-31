package com.walletko.backend.domain.tag;

public class TagNameConflictError extends RuntimeException {
    public TagNameConflictError(String name) {
        super("Tag name already exists: " + name);
    }
}
