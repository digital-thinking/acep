package de.ixeption.acep.repository;

import de.ixeption.acep.domain.PortfolioEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PortfolioEntry entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PortfolioEntryRepository extends JpaRepository<PortfolioEntry, Long> {
}
