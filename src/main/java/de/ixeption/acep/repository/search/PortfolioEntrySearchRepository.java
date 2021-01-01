package de.ixeption.acep.repository.search;

import de.ixeption.acep.domain.PortfolioEntry;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link PortfolioEntry} entity.
 */
public interface PortfolioEntrySearchRepository extends ElasticsearchRepository<PortfolioEntry, Long> {
}
