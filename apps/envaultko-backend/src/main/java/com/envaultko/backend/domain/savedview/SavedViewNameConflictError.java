package com.envaultko.backend.domain.savedview;

public class SavedViewNameConflictError extends RuntimeException {
    public SavedViewNameConflictError(String name) {
        super("Saved view name already exists: " + name);
    }
}
