package de.ixeption.acep.domain;

import de.ixeption.acep.domain.portfolio.PortfolioEntry;
import de.ixeption.acep.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PortfolioEntryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PortfolioEntry.class);
        PortfolioEntry portfolioEntry1 = new PortfolioEntry();
        portfolioEntry1.setId(1L);
        PortfolioEntry portfolioEntry2 = new PortfolioEntry();
        portfolioEntry2.setId(portfolioEntry1.getId());
        assertThat(portfolioEntry1).isEqualTo(portfolioEntry2);
        portfolioEntry2.setId(2L);
        assertThat(portfolioEntry1).isNotEqualTo(portfolioEntry2);
        portfolioEntry1.setId(null);
        assertThat(portfolioEntry1).isNotEqualTo(portfolioEntry2);
    }
}
