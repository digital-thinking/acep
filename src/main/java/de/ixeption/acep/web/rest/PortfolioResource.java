package de.ixeption.acep.web.rest;

import de.ixeption.acep.domain.Portfolio;
import de.ixeption.acep.domain.User;
import de.ixeption.acep.repository.PortfolioRepository;
import de.ixeption.acep.repository.search.PortfolioSearchRepository;
import de.ixeption.acep.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * REST controller for managing {@link de.ixeption.acep.domain.Portfolio}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PortfolioResource extends AbstractResource {

    private static final String ENTITY_NAME = "portfolio";
    private final Logger log = LoggerFactory.getLogger(PortfolioResource.class);
    private final PortfolioRepository portfolioRepository;
    private final PortfolioSearchRepository portfolioSearchRepository;
    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public PortfolioResource(PortfolioRepository portfolioRepository, PortfolioSearchRepository portfolioSearchRepository) {
        this.portfolioRepository = portfolioRepository;
        this.portfolioSearchRepository = portfolioSearchRepository;
    }

    /**
     * {@code POST  /portfolios} : Create a new portfolio.
     *
     * @param portfolio the portfolio to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new portfolio, or with status {@code 400 (Bad Request)} if the portfolio has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/portfolios")
    public ResponseEntity<Portfolio> createPortfolio(@Valid @RequestBody Portfolio portfolio) throws URISyntaxException {
        log.debug("REST request to save Portfolio : {}", portfolio);
        if (portfolio.getId() != null) {
            throw new BadRequestAlertException("A new portfolio cannot already have an ID", ENTITY_NAME, "idexists");
        }
        portfolio.setUser(getLoggedInUser());
        Portfolio result = portfolioRepository.save(portfolio);
        portfolioSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/portfolios/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /portfolios} : Updates an existing portfolio.
     *
     * @param portfolio the portfolio to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated portfolio,
     * or with status {@code 400 (Bad Request)} if the portfolio is not valid,
     * or with status {@code 500 (Internal Server Error)} if the portfolio couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/portfolios")
    public ResponseEntity<Portfolio> updatePortfolio(@Valid @RequestBody Portfolio portfolio) throws URISyntaxException {
        log.debug("REST request to update Portfolio : {}", portfolio);
        if (portfolio.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Portfolio result = portfolioRepository.save(portfolio);
        portfolioSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, portfolio.getId().toString()))
            .body(result);
    }


    /**
     * {@code PATCH  /portfolios} : Updates given fields of an existing portfolio.
     *
     * @param portfolio the portfolio to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated portfolio,
     * or with status {@code 400 (Bad Request)} if the portfolio is not valid,
     * or with status {@code 404 (Not Found)} if the portfolio is not found,
     * or with status {@code 500 (Internal Server Error)} if the portfolio couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/portfolios", consumes = "application/merge-patch+json")
    public ResponseEntity<Portfolio> partialUpdatePortfolio(@NotNull @RequestBody Portfolio portfolio) throws URISyntaxException {
        log.debug("REST request to update Portfolio partially : {}", portfolio);
        if (portfolio.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }


        Optional<Portfolio> result = portfolioRepository.findById(portfolio.getId())
            .map(existingPortfolio -> {
                if (portfolio.getName() != null) {
                    existingPortfolio.setName(portfolio.getName());
                }

                if (portfolio.getCreated() != null) {
                    existingPortfolio.setCreated(portfolio.getCreated());
                }


                return existingPortfolio;
            })
            .map(portfolioRepository::save)
            .map(savedPortfolio -> {
                portfolioSearchRepository.save(savedPortfolio);

                return savedPortfolio;

            });

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, portfolio.getId().toString())
        );
    }

    /**
     * {@code GET  /portfolios} : get all the portfolios.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of portfolios in body.
     */
    @GetMapping("/portfolios")
    public List<Portfolio> getAllPortfolios() {
        log.debug("REST request to get all Portfolios");
        User loggedInUser = getLoggedInUser();
        if (isAdminUser()) {
            return portfolioRepository.findAll();
        } else {
            return portfolioRepository.findAllByUserId(loggedInUser.getId());
        }
    }

    /**
     * {@code GET  /portfolios/:id} : get the "id" portfolio.
     *
     * @param id the id of the portfolio to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the portfolio, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/portfolios/{id}")
    public ResponseEntity<Portfolio> getPortfolio(@PathVariable Long id) {
        log.debug("REST request to get Portfolio : {}", id);
        Optional<Portfolio> portfolio = portfolioRepository.findById(id);
        User loggedInUser = getLoggedInUser();
        if (portfolio.isPresent() && portfolio.get().getUser().equals(loggedInUser) || isAdminUser()) {
            return ResponseUtil.wrapOrNotFound(portfolio);
        }
        throw new BadRequestAlertException("not logged in", ENTITY_NAME, "messages.register.noaccount");
    }

    /**
     * {@code DELETE  /portfolios/:id} : delete the "id" portfolio.
     *
     * @param id the id of the portfolio to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/portfolios/{id}")
    public ResponseEntity<Void> deletePortfolio(@PathVariable Long id) {
        log.debug("REST request to delete Portfolio : {}", id);
        Optional<Portfolio> portfolio = portfolioRepository.findById(id);
        User loggedInUser = getLoggedInUser();
        if (portfolio.isPresent() && portfolio.get().getUser().equals(loggedInUser) || isAdminUser()) {
            portfolioRepository.deleteById(id);
            portfolioSearchRepository.deleteById(id);
            return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
        }
        throw new BadRequestAlertException("not logged in", ENTITY_NAME, "messages.register.noaccount");
    }

    /**
     * {@code SEARCH  /_search/portfolios?query=:query} : search for the portfolio corresponding
     * to the query.
     *
     * @param query the query of the portfolio search.
     * @return the result of the search.
     */
    @GetMapping("/_search/portfolios")
    public List<Portfolio> searchPortfolios(@RequestParam String query) {
        log.debug("REST request to search Portfolios for query {}", query);
        return StreamSupport
            .stream(portfolioSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .filter(portfolio -> portfolio.getUser().equals(getLoggedInUser()))
            .collect(Collectors.toList());
    }
}
