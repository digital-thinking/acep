package de.ixeption.acep.web.rest;

import de.ixeption.acep.domain.PortfolioEntry;
import de.ixeption.acep.domain.User;
import de.ixeption.acep.repository.PortfolioEntryRepository;
import de.ixeption.acep.repository.search.PortfolioEntrySearchRepository;
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
 * REST controller for managing {@link de.ixeption.acep.domain.PortfolioEntry}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PortfolioEntryResource extends AbstractResource {

    private static final String ENTITY_NAME = "portfolioEntry";
    private final Logger log = LoggerFactory.getLogger(PortfolioEntryResource.class);
    private final PortfolioEntryRepository portfolioEntryRepository;
    private final PortfolioEntrySearchRepository portfolioEntrySearchRepository;
    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public PortfolioEntryResource(PortfolioEntryRepository portfolioEntryRepository, PortfolioEntrySearchRepository portfolioEntrySearchRepository) {
        this.portfolioEntryRepository = portfolioEntryRepository;
        this.portfolioEntrySearchRepository = portfolioEntrySearchRepository;
    }

    /**
     * {@code POST  /portfolio-entries} : Create a new portfolioEntry.
     *
     * @param portfolioEntry the portfolioEntry to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new portfolioEntry, or with status {@code 400 (Bad Request)} if the portfolioEntry has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/portfolio-entries")
    public ResponseEntity<PortfolioEntry> createPortfolioEntry(@Valid @RequestBody PortfolioEntry portfolioEntry) throws URISyntaxException {
        log.debug("REST request to save PortfolioEntry : {}", portfolioEntry);

        if (portfolioEntry.getId() != null) {
            throw new BadRequestAlertException("A new entity cannot already have an ID", ENTITY_NAME, "idexists");
        }

        User loggedInUser = getLoggedInUser();
        if (portfolioEntry.getPortfolio().getUser().equals(loggedInUser) || isAdminUser()) {
            PortfolioEntry result = portfolioEntryRepository.save(portfolioEntry);
            portfolioEntrySearchRepository.save(result);
            return ResponseEntity.created(new URI("/api/portfolio-entries/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                .body(result);
        }

        throw new BadRequestAlertException("A new portfolioEntry cannot already have an ID", ENTITY_NAME, "idnull");

    }

    private PortfolioEntry hasAccessToExistingEntry(Long entryId) {
        if (entryId != null) {
            Optional<PortfolioEntry> existingPortfolioEntryOptional = portfolioEntryRepository.findById(entryId);
            if (existingPortfolioEntryOptional.isPresent()) {
                PortfolioEntry existingPortfolioEntry = existingPortfolioEntryOptional.get();
                if (existingPortfolioEntry.getPortfolio().getUser().equals(getLoggedInUser()) || isAdminUser()) {
                    return existingPortfolioEntry;
                }
            }
        }
        return null;
    }

    /**
     * {@code PUT  /portfolio-entries} : Updates an existing portfolioEntry.
     *
     * @param portfolioEntry the portfolioEntry to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated portfolioEntry,
     * or with status {@code 400 (Bad Request)} if the portfolioEntry is not valid,
     * or with status {@code 500 (Internal Server Error)} if the portfolioEntry couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/portfolio-entries")
    public ResponseEntity<PortfolioEntry> updatePortfolioEntry(@Valid @RequestBody PortfolioEntry portfolioEntry) throws URISyntaxException {
        log.debug("REST request to update PortfolioEntry : {}", portfolioEntry);
        if (portfolioEntry.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        if (hasAccessToExistingEntry(portfolioEntry.getId()) != null) {
            PortfolioEntry result = portfolioEntryRepository.save(portfolioEntry);
            portfolioEntrySearchRepository.save(result);
            return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, portfolioEntry.getId().toString()))
                .body(result);
        }

        throw new BadRequestAlertException("Couldn't update Portfolio", ENTITY_NAME, "idnull");
    }


    /**
     * {@code PATCH  /portfolio-entries} : Updates given fields of an existing portfolioEntry.
     *
     * @param portfolioEntry the portfolioEntry to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated portfolioEntry,
     * or with status {@code 400 (Bad Request)} if the portfolioEntry is not valid,
     * or with status {@code 404 (Not Found)} if the portfolioEntry is not found,
     * or with status {@code 500 (Internal Server Error)} if the portfolioEntry couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/portfolio-entries", consumes = "application/merge-patch+json")
    public ResponseEntity<PortfolioEntry> partialUpdatePortfolioEntry(@NotNull @RequestBody PortfolioEntry portfolioEntry) throws URISyntaxException {
        log.debug("REST request to update PortfolioEntry partially : {}", portfolioEntry);
        PortfolioEntry existingEntry = hasAccessToExistingEntry(portfolioEntry.getId());
        if (existingEntry != null) {
            updateEntity(portfolioEntry, existingEntry);
            PortfolioEntry saved = portfolioEntryRepository.save(existingEntry);
            PortfolioEntry ignored = portfolioEntrySearchRepository.save(saved);
            return ResponseUtil.wrapOrNotFound(
                Optional.of(saved),
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, portfolioEntry.getId().toString())
            );
        }

        throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");


    }

    private void updateEntity(PortfolioEntry portfolioEntry, PortfolioEntry existingPortfolioEntry) {
        if (portfolioEntry.getAmount() != null) {
            existingPortfolioEntry.setAmount(portfolioEntry.getAmount());
        }

        if (portfolioEntry.getPrice() != null) {
            existingPortfolioEntry.setPrice(portfolioEntry.getPrice());
        }

        if (portfolioEntry.getBought() != null) {
            existingPortfolioEntry.setBought(portfolioEntry.getBought());
        }

        if (portfolioEntry.getSold() != null) {
            existingPortfolioEntry.setSold(portfolioEntry.getSold());
        }

        if (portfolioEntry.getCustomName() != null) {
            existingPortfolioEntry.setCustomName(portfolioEntry.getCustomName());
        }

        if (portfolioEntry.getGroup1() != null) {
            existingPortfolioEntry.setGroup1(portfolioEntry.getGroup1());
        }

        if (portfolioEntry.getGroup2() != null) {
            existingPortfolioEntry.setGroup2(portfolioEntry.getGroup2());
        }

        if (portfolioEntry.getGroup3() != null) {
            existingPortfolioEntry.setGroup3(portfolioEntry.getGroup3());
        }

        if (portfolioEntry.getGroup4() != null) {
            existingPortfolioEntry.setGroup4(portfolioEntry.getGroup4());
        }

        if (portfolioEntry.getPortfolio() != null) {
            existingPortfolioEntry.setPortfolio(portfolioEntry.getPortfolio());
        }
    }

    /**
     * {@code GET  /portfolio-entries} : get all the portfolioEntries.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of portfolioEntries in body.
     */
    @GetMapping("/portfolio-entries")
    public List<PortfolioEntry> getAllPortfolioEntries() {
        log.debug("REST request to get all PortfolioEntries");
        User loggedInUser = getLoggedInUser();
        if (isAdminUser()) {
            return portfolioEntryRepository.findAll();
        } else {
            return portfolioEntryRepository.findAllByUserId(loggedInUser.getId());
        }

    }

    /**
     * {@code GET  /portfolio-entries/:id} : get the "id" portfolioEntry.
     *
     * @param id the id of the portfolioEntry to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the portfolioEntry, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/portfolio-entries/{id}")
    public ResponseEntity<PortfolioEntry> getPortfolioEntry(@PathVariable Long id) {
        log.debug("REST request to get PortfolioEntry : {}", id);
        Optional<PortfolioEntry> portfolioEntry = portfolioEntryRepository.findById(id);
        User loggedInUser = getLoggedInUser();
        boolean allowed = portfolioEntry.isPresent() && (
            portfolioEntry.get().getPortfolio().getUser().equals(loggedInUser) || isAdminUser());
        if (!allowed) {
            portfolioEntry = Optional.empty();
        }
        return ResponseUtil.wrapOrNotFound(portfolioEntry);

    }

    /**
     * {@code DELETE  /portfolio-entries/:id} : delete the "id" portfolioEntry.
     *
     * @param id the id of the portfolioEntry to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/portfolio-entries/{id}")
    public ResponseEntity<Void> deletePortfolioEntry(@PathVariable Long id) {
        log.debug("REST request to delete PortfolioEntry : {}", id);

        Optional<PortfolioEntry> portfolioEntry = portfolioEntryRepository.findById(id);
        User loggedInUser = getLoggedInUser();
        if (portfolioEntry.isPresent() && portfolioEntry.get().getPortfolio().getUser().equals(loggedInUser) || isAdminUser()) {
            portfolioEntryRepository.deleteById(id);
            portfolioEntrySearchRepository.deleteById(id);
            return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
        }
        throw new BadRequestAlertException("not logged in", ENTITY_NAME, "messages.register.noaccount");
    }

    /**
     * {@code SEARCH  /_search/portfolio-entries?query=:query} : search for the portfolioEntry corresponding
     * to the query.
     *
     * @param query the query of the portfolioEntry search.
     * @return the result of the search.
     */
    @GetMapping("/_search/portfolio-entries")
    public List<PortfolioEntry> searchPortfolioEntries(@RequestParam String query) {
        log.debug("REST request to search PortfolioEntries for query {}", query);
        return StreamSupport
            .stream(portfolioEntrySearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .filter(portfolioEntry -> portfolioEntry.getPortfolio().getUser().equals(getLoggedInUser()))
            .collect(Collectors.toList());
    }
}
