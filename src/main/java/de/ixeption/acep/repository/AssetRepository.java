package de.ixeption.acep.repository;

import de.ixeption.acep.domain.Asset;
import de.ixeption.acep.domain.enumeration.Source;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data SQL repository for the Asset entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AssetRepository extends JpaRepository<Asset, Long> {

    String ASSETS_BY_SOURCE_AND_SYMBOL = "assetsBySourceAndSymbol";

    @Cacheable(cacheNames = ASSETS_BY_SOURCE_AND_SYMBOL)
    Optional<Asset> findAssetBySourceAndSymbol(Source source, String symbol);

}
