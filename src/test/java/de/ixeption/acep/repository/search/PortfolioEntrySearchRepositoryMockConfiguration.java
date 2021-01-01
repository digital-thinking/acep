package de.ixeption.acep.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link PortfolioEntrySearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class PortfolioEntrySearchRepositoryMockConfiguration {

    @MockBean
    private PortfolioEntrySearchRepository mockPortfolioEntrySearchRepository;

}
