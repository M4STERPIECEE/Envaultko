package com.walletko.backend.domain.repository;

import com.walletko.backend.domain.entity.Pot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PotRepository extends JpaRepository<Pot, String> {

    List<Pot> findByUserIdAndArchivedAtIsNull(String userId);

    List<Pot> findByUserId(String userId);

    Optional<Pot> findByUserIdAndIsDefaultTrue(String userId);

    @Query("SELECT COALESCE(SUM(p.percentage), 0) FROM Pot p WHERE p.userId = :userId AND p.archivedAt IS NULL")
    Integer sumActivePercentagesByUserId(String userId);
}
