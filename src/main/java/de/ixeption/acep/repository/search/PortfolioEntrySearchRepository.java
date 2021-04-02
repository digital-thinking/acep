package de.ixeption.acep.repository.search;

import de.ixeption.acep.domain.portfolio.PortfolioEntry;
import de.ixeption.acep.domain.search.PortfolioEntrySearchDTO;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link PortfolioEntry} entity.
 */
public interface PortfolioEntrySearchRepository extends ElasticsearchRepository<PortfolioEntrySearchDTO, Long> {
}
