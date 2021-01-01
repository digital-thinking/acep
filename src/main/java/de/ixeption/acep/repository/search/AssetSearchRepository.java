package de.ixeption.acep.repository.search;

import de.ixeption.acep.domain.Asset;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Asset} entity.
 */
public interface AssetSearchRepository extends ElasticsearchRepository<Asset, Long> {
}
