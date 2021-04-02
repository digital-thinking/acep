package de.ixeption.acep.repository;

import de.ixeption.acep.domain.portfolio.PortfolioEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data SQL repository for the PortfolioEntry entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PortfolioEntryRepository extends JpaRepository<PortfolioEntry, Long> {
    @Query("SELECT pe FROM PortfolioEntry pe WHERE pe.portfolio.user.id = :id")
    List<PortfolioEntry> findAllByUserId(@Param("id") Long id);
}
