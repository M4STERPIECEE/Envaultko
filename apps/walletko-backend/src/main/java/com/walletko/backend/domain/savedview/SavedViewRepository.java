package com.walletko.backend.domain.savedview;

import com.walletko.backend.domain.shared.vo.*;
import java.util.List;
import java.util.Optional;

public interface SavedViewRepository {
    void save(SavedView view);
    Optional<SavedView> findById(Id id, Id userId);
    boolean existsByName(Name name, Id userId);
    void remove(Id id, Id userId);
    List<SavedView> findAll(Id userId);
}
