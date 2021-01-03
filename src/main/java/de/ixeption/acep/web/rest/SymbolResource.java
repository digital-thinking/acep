package de.ixeption.acep.web.rest;

import de.ixeption.acep.domain.Asset;
import de.ixeption.acep.service.data.AlphaVantageProvider;
import org.patriques.output.search.QueryResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * REST controller for managing {@link Asset}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class SymbolResource {

    private static final String ENTITY_NAME = "symbol";
    private final Logger log = LoggerFactory.getLogger(SymbolResource.class);

    @Resource
    AlphaVantageProvider alphaVantageProvider;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    /**
     * {@code SEARCH  /_search/symbol?query=:query} : search for the symbols corresponding
     * to the query.
     *
     * @param query the query of the symbol search.
     * @return the result of the search.
     */
    @GetMapping("/_search/symbol")
    @PreAuthorize("hasAuthority(T(de.ixeption.acep.security.Role).ROLE_USER.name())")
    public List<QueryResultData.QueryResultEntry> searchSymbols(@RequestParam String query) {
        log.debug("REST request to search Assets for query {}", query);
        QueryResultData queryResultData = alphaVantageProvider.query(query);
        return queryResultData.getResultEntryList();
    }
}
