package de.ixeption.acep.domain;

import de.ixeption.acep.domain.portfolio.Portfolio;
import de.ixeption.acep.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PortfolioTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Portfolio.class);
        Portfolio portfolio1 = new Portfolio();
        portfolio1.setId(1L);
        Portfolio portfolio2 = new Portfolio();
        portfolio2.setId(portfolio1.getId());
        assertThat(portfolio1).isEqualTo(portfolio2);
        portfolio2.setId(2L);
        assertThat(portfolio1).isNotEqualTo(portfolio2);
        portfolio1.setId(null);
        assertThat(portfolio1).isNotEqualTo(portfolio2);
    }
}
