package de.ixeption.acep.repository.search;

import de.ixeption.acep.domain.portfolio.Portfolio;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Portfolio} entity.
 */
public interface PortfolioSearchRepository extends ElasticsearchRepository<Portfolio, Long> {
}
