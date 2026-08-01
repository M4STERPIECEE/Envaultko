package com.envaultko.backend.domain.pot;

import com.envaultko.backend.domain.shared.vo.Id;
import java.util.List;
import java.util.Optional;

public interface PotRepository {
    void save(Pot pot);
    List<Pot> findAll(Id userId);
    List<Pot> findAllWithArchived(Id userId);
    Optional<Pot> findById(Id id, Id userId);
    Optional<Pot> findDefault(Id userId);
    List<PotSnapshot> findSnapshots(Id userId);
}
