package de.ixeption.acep.web.rest;

import de.ixeption.acep.IntegrationTest;
import de.ixeption.acep.domain.PortfolioEntry;
import de.ixeption.acep.repository.PortfolioEntryRepository;
import de.ixeption.acep.repository.search.PortfolioEntrySearchRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;

import static de.ixeption.acep.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link PortfolioEntryResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PortfolioEntryResourceIT {

    private static final Long DEFAULT_AMOUNT = 1L;
    private static final Long UPDATED_AMOUNT = 2L;

    private static final BigDecimal DEFAULT_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRICE = new BigDecimal(2);

    private static final Instant DEFAULT_BOUGHT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_BOUGHT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_SOLD = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SOLD = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CUSTOM_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CUSTOM_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_GROUP_1 = "AAAAAAAAAA";
    private static final String UPDATED_GROUP_1 = "BBBBBBBBBB";

    private static final String DEFAULT_GROUP_2 = "AAAAAAAAAA";
    private static final String UPDATED_GROUP_2 = "BBBBBBBBBB";

    private static final String DEFAULT_GROUP_3 = "AAAAAAAAAA";
    private static final String UPDATED_GROUP_3 = "BBBBBBBBBB";

    private static final String DEFAULT_GROUP_4 = "AAAAAAAAAA";
    private static final String UPDATED_GROUP_4 = "BBBBBBBBBB";

    @Autowired
    private PortfolioEntryRepository portfolioEntryRepository;

    /**
     * This repository is mocked in the de.ixeption.acep.repository.search test package.
     *
     * @see de.ixeption.acep.repository.search.PortfolioEntrySearchRepositoryMockConfiguration
     */
    @Autowired
    private PortfolioEntrySearchRepository mockPortfolioEntrySearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPortfolioEntryMockMvc;

    private PortfolioEntry portfolioEntry;

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PortfolioEntry createEntity(EntityManager em) {
        PortfolioEntry portfolioEntry = new PortfolioEntry()
            .amount(DEFAULT_AMOUNT)
            .price(DEFAULT_PRICE)
            .bought(DEFAULT_BOUGHT)
            .sold(DEFAULT_SOLD)
            .customName(DEFAULT_CUSTOM_NAME)
            .group1(DEFAULT_GROUP_1)
            .group2(DEFAULT_GROUP_2)
            .group3(DEFAULT_GROUP_3)
            .group4(DEFAULT_GROUP_4);
        return portfolioEntry;
    }

    /**
     * Create an updated entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PortfolioEntry createUpdatedEntity(EntityManager em) {
        PortfolioEntry portfolioEntry = new PortfolioEntry()
            .amount(UPDATED_AMOUNT)
            .price(UPDATED_PRICE)
            .bought(UPDATED_BOUGHT)
            .sold(UPDATED_SOLD)
            .customName(UPDATED_CUSTOM_NAME)
            .group1(UPDATED_GROUP_1)
            .group2(UPDATED_GROUP_2)
            .group3(UPDATED_GROUP_3)
            .group4(UPDATED_GROUP_4);
        return portfolioEntry;
    }

    @BeforeEach
    public void initTest() {
        portfolioEntry = createEntity(em);
    }

    @Test
    @Transactional
    void createPortfolioEntry() throws Exception {
        int databaseSizeBeforeCreate = portfolioEntryRepository.findAll().size();
        // Create the PortfolioEntry
        restPortfolioEntryMockMvc.perform(post("/api/portfolio-entries").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(portfolioEntry)))
            .andExpect(status().isCreated());

        // Validate the PortfolioEntry in the database
        List<PortfolioEntry> portfolioEntryList = portfolioEntryRepository.findAll();
        assertThat(portfolioEntryList).hasSize(databaseSizeBeforeCreate + 1);
        PortfolioEntry testPortfolioEntry = portfolioEntryList.get(portfolioEntryList.size() - 1);
        assertThat(testPortfolioEntry.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testPortfolioEntry.getPrice()).isEqualByComparingTo(DEFAULT_PRICE);
        assertThat(testPortfolioEntry.getBought()).isEqualTo(DEFAULT_BOUGHT);
        assertThat(testPortfolioEntry.getSold()).isEqualTo(DEFAULT_SOLD);
        assertThat(testPortfolioEntry.getCustomName()).isEqualTo(DEFAULT_CUSTOM_NAME);
        assertThat(testPortfolioEntry.getGroup1()).isEqualTo(DEFAULT_GROUP_1);
        assertThat(testPortfolioEntry.getGroup2()).isEqualTo(DEFAULT_GROUP_2);
        assertThat(testPortfolioEntry.getGroup3()).isEqualTo(DEFAULT_GROUP_3);
        assertThat(testPortfolioEntry.getGroup4()).isEqualTo(DEFAULT_GROUP_4);

        // Validate the PortfolioEntry in Elasticsearch
        verify(mockPortfolioEntrySearchRepository, times(1)).save(testPortfolioEntry);
    }

    @Test
    @Transactional
    void createPortfolioEntryWithExistingId() throws Exception {
        // Create the PortfolioEntry with an existing ID
        portfolioEntry.setId(1L);

        int databaseSizeBeforeCreate = portfolioEntryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPortfolioEntryMockMvc.perform(post("/api/portfolio-entries").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(portfolioEntry)))
            .andExpect(status().isBadRequest());

        // Validate the PortfolioEntry in the database
        List<PortfolioEntry> portfolioEntryList = portfolioEntryRepository.findAll();
        assertThat(portfolioEntryList).hasSize(databaseSizeBeforeCreate);

        // Validate the PortfolioEntry in Elasticsearch
        verify(mockPortfolioEntrySearchRepository, times(0)).save(portfolioEntry);
    }


    @Test
    @Transactional
    void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = portfolioEntryRepository.findAll().size();
        // set the field null
        portfolioEntry.setAmount(null);

        // Create the PortfolioEntry, which fails.


        restPortfolioEntryMockMvc.perform(post("/api/portfolio-entries").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(portfolioEntry)))
            .andExpect(status().isBadRequest());

        List<PortfolioEntry> portfolioEntryList = portfolioEntryRepository.findAll();
        assertThat(portfolioEntryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = portfolioEntryRepository.findAll().size();
        // set the field null
        portfolioEntry.setPrice(null);

        // Create the PortfolioEntry, which fails.


        restPortfolioEntryMockMvc.perform(post("/api/portfolio-entries").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(portfolioEntry)))
            .andExpect(status().isBadRequest());

        List<PortfolioEntry> portfolioEntryList = portfolioEntryRepository.findAll();
        assertThat(portfolioEntryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkBoughtIsRequired() throws Exception {
        int databaseSizeBeforeTest = portfolioEntryRepository.findAll().size();
        // set the field null
        portfolioEntry.setBought(null);

        // Create the PortfolioEntry, which fails.


        restPortfolioEntryMockMvc.perform(post("/api/portfolio-entries").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(portfolioEntry)))
            .andExpect(status().isBadRequest());

        List<PortfolioEntry> portfolioEntryList = portfolioEntryRepository.findAll();
        assertThat(portfolioEntryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPortfolioEntries() throws Exception {
        // Initialize the database
        portfolioEntryRepository.saveAndFlush(portfolioEntry);

        // Get all the portfolioEntryList
        restPortfolioEntryMockMvc.perform(get("/api/portfolio-entries?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(portfolioEntry.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(sameNumber(DEFAULT_PRICE))))
            .andExpect(jsonPath("$.[*].bought").value(hasItem(DEFAULT_BOUGHT.toString())))
            .andExpect(jsonPath("$.[*].sold").value(hasItem(DEFAULT_SOLD.toString())))
            .andExpect(jsonPath("$.[*].customName").value(hasItem(DEFAULT_CUSTOM_NAME)))
            .andExpect(jsonPath("$.[*].group1").value(hasItem(DEFAULT_GROUP_1)))
            .andExpect(jsonPath("$.[*].group2").value(hasItem(DEFAULT_GROUP_2)))
            .andExpect(jsonPath("$.[*].group3").value(hasItem(DEFAULT_GROUP_3)))
            .andExpect(jsonPath("$.[*].group4").value(hasItem(DEFAULT_GROUP_4)));
    }

    @Test
    @Transactional
    void getPortfolioEntry() throws Exception {
        // Initialize the database
        portfolioEntryRepository.saveAndFlush(portfolioEntry);

        // Get the portfolioEntry
        restPortfolioEntryMockMvc.perform(get("/api/portfolio-entries/{id}", portfolioEntry.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(portfolioEntry.getId().intValue()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.intValue()))
            .andExpect(jsonPath("$.price").value(sameNumber(DEFAULT_PRICE)))
            .andExpect(jsonPath("$.bought").value(DEFAULT_BOUGHT.toString()))
            .andExpect(jsonPath("$.sold").value(DEFAULT_SOLD.toString()))
            .andExpect(jsonPath("$.customName").value(DEFAULT_CUSTOM_NAME))
            .andExpect(jsonPath("$.group1").value(DEFAULT_GROUP_1))
            .andExpect(jsonPath("$.group2").value(DEFAULT_GROUP_2))
            .andExpect(jsonPath("$.group3").value(DEFAULT_GROUP_3))
            .andExpect(jsonPath("$.group4").value(DEFAULT_GROUP_4));
    }

    @Test
    @Transactional
    void getNonExistingPortfolioEntry() throws Exception {
        // Get the portfolioEntry
        restPortfolioEntryMockMvc.perform(get("/api/portfolio-entries/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void updatePortfolioEntry() throws Exception {
        // Initialize the database
        portfolioEntryRepository.saveAndFlush(portfolioEntry);

        int databaseSizeBeforeUpdate = portfolioEntryRepository.findAll().size();

        // Update the portfolioEntry
        PortfolioEntry updatedPortfolioEntry = portfolioEntryRepository.findById(portfolioEntry.getId()).get();
        // Disconnect from session so that the updates on updatedPortfolioEntry are not directly saved in db
        em.detach(updatedPortfolioEntry);
        updatedPortfolioEntry
            .amount(UPDATED_AMOUNT)
            .price(UPDATED_PRICE)
            .bought(UPDATED_BOUGHT)
            .sold(UPDATED_SOLD)
            .customName(UPDATED_CUSTOM_NAME)
            .group1(UPDATED_GROUP_1)
            .group2(UPDATED_GROUP_2)
            .group3(UPDATED_GROUP_3)
            .group4(UPDATED_GROUP_4);

        restPortfolioEntryMockMvc.perform(put("/api/portfolio-entries").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedPortfolioEntry)))
            .andExpect(status().isOk());

        // Validate the PortfolioEntry in the database
        List<PortfolioEntry> portfolioEntryList = portfolioEntryRepository.findAll();
        assertThat(portfolioEntryList).hasSize(databaseSizeBeforeUpdate);
        PortfolioEntry testPortfolioEntry = portfolioEntryList.get(portfolioEntryList.size() - 1);
        assertThat(testPortfolioEntry.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testPortfolioEntry.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testPortfolioEntry.getBought()).isEqualTo(UPDATED_BOUGHT);
        assertThat(testPortfolioEntry.getSold()).isEqualTo(UPDATED_SOLD);
        assertThat(testPortfolioEntry.getCustomName()).isEqualTo(UPDATED_CUSTOM_NAME);
        assertThat(testPortfolioEntry.getGroup1()).isEqualTo(UPDATED_GROUP_1);
        assertThat(testPortfolioEntry.getGroup2()).isEqualTo(UPDATED_GROUP_2);
        assertThat(testPortfolioEntry.getGroup3()).isEqualTo(UPDATED_GROUP_3);
        assertThat(testPortfolioEntry.getGroup4()).isEqualTo(UPDATED_GROUP_4);

        // Validate the PortfolioEntry in Elasticsearch
        verify(mockPortfolioEntrySearchRepository).save(testPortfolioEntry);
    }

    @Test
    @Transactional
    void updateNonExistingPortfolioEntry() throws Exception {
        int databaseSizeBeforeUpdate = portfolioEntryRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPortfolioEntryMockMvc.perform(put("/api/portfolio-entries").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(portfolioEntry)))
            .andExpect(status().isBadRequest());

        // Validate the PortfolioEntry in the database
        List<PortfolioEntry> portfolioEntryList = portfolioEntryRepository.findAll();
        assertThat(portfolioEntryList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PortfolioEntry in Elasticsearch
        verify(mockPortfolioEntrySearchRepository, times(0)).save(portfolioEntry);
    }


    @Test
    @Transactional
    void partialUpdatePortfolioEntryWithPatch() throws Exception {

// Initialize the database
        portfolioEntryRepository.saveAndFlush(portfolioEntry);

        int databaseSizeBeforeUpdate = portfolioEntryRepository.findAll().size();

// Update the portfolioEntry using partial update
        PortfolioEntry partialUpdatedPortfolioEntry = new PortfolioEntry();
        partialUpdatedPortfolioEntry.setId(portfolioEntry.getId());

        partialUpdatedPortfolioEntry
            .amount(UPDATED_AMOUNT)
            .bought(UPDATED_BOUGHT)
            .customName(UPDATED_CUSTOM_NAME)
            .group1(UPDATED_GROUP_1)
            .group2(UPDATED_GROUP_2);

        restPortfolioEntryMockMvc.perform(patch("/api/portfolio-entries").with(csrf())
            .contentType("application/merge-patch+json")
            .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPortfolioEntry)))
            .andExpect(status().isOk());

// Validate the PortfolioEntry in the database
        List<PortfolioEntry> portfolioEntryList = portfolioEntryRepository.findAll();
        assertThat(portfolioEntryList).hasSize(databaseSizeBeforeUpdate);
        PortfolioEntry testPortfolioEntry = portfolioEntryList.get(portfolioEntryList.size() - 1);
        assertThat(testPortfolioEntry.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testPortfolioEntry.getPrice()).isEqualByComparingTo(DEFAULT_PRICE);
        assertThat(testPortfolioEntry.getBought()).isEqualTo(UPDATED_BOUGHT);
        assertThat(testPortfolioEntry.getSold()).isEqualTo(DEFAULT_SOLD);
        assertThat(testPortfolioEntry.getCustomName()).isEqualTo(UPDATED_CUSTOM_NAME);
        assertThat(testPortfolioEntry.getGroup1()).isEqualTo(UPDATED_GROUP_1);
        assertThat(testPortfolioEntry.getGroup2()).isEqualTo(UPDATED_GROUP_2);
        assertThat(testPortfolioEntry.getGroup3()).isEqualTo(DEFAULT_GROUP_3);
        assertThat(testPortfolioEntry.getGroup4()).isEqualTo(DEFAULT_GROUP_4);
    }

    @Test
    @Transactional
    void fullUpdatePortfolioEntryWithPatch() throws Exception {

// Initialize the database
        portfolioEntryRepository.saveAndFlush(portfolioEntry);

        int databaseSizeBeforeUpdate = portfolioEntryRepository.findAll().size();

// Update the portfolioEntry using partial update
        PortfolioEntry partialUpdatedPortfolioEntry = new PortfolioEntry();
        partialUpdatedPortfolioEntry.setId(portfolioEntry.getId());

        partialUpdatedPortfolioEntry
            .amount(UPDATED_AMOUNT)
            .price(UPDATED_PRICE)
            .bought(UPDATED_BOUGHT)
            .sold(UPDATED_SOLD)
            .customName(UPDATED_CUSTOM_NAME)
            .group1(UPDATED_GROUP_1)
            .group2(UPDATED_GROUP_2)
            .group3(UPDATED_GROUP_3)
            .group4(UPDATED_GROUP_4);

        restPortfolioEntryMockMvc.perform(patch("/api/portfolio-entries").with(csrf())
            .contentType("application/merge-patch+json")
            .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPortfolioEntry)))
            .andExpect(status().isOk());

// Validate the PortfolioEntry in the database
        List<PortfolioEntry> portfolioEntryList = portfolioEntryRepository.findAll();
        assertThat(portfolioEntryList).hasSize(databaseSizeBeforeUpdate);
        PortfolioEntry testPortfolioEntry = portfolioEntryList.get(portfolioEntryList.size() - 1);
        assertThat(testPortfolioEntry.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testPortfolioEntry.getPrice()).isEqualByComparingTo(UPDATED_PRICE);
        assertThat(testPortfolioEntry.getBought()).isEqualTo(UPDATED_BOUGHT);
        assertThat(testPortfolioEntry.getSold()).isEqualTo(UPDATED_SOLD);
        assertThat(testPortfolioEntry.getCustomName()).isEqualTo(UPDATED_CUSTOM_NAME);
        assertThat(testPortfolioEntry.getGroup1()).isEqualTo(UPDATED_GROUP_1);
        assertThat(testPortfolioEntry.getGroup2()).isEqualTo(UPDATED_GROUP_2);
        assertThat(testPortfolioEntry.getGroup3()).isEqualTo(UPDATED_GROUP_3);
        assertThat(testPortfolioEntry.getGroup4()).isEqualTo(UPDATED_GROUP_4);
    }

    @Test
    @Transactional
    void partialUpdatePortfolioEntryShouldThrown() throws Exception {
        // Update the portfolioEntry without id should throw
        PortfolioEntry partialUpdatedPortfolioEntry = new PortfolioEntry();

        restPortfolioEntryMockMvc.perform(patch("/api/portfolio-entries").with(csrf())
            .contentType("application/merge-patch+json")
            .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPortfolioEntry)))
            .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    void deletePortfolioEntry() throws Exception {
        // Initialize the database
        portfolioEntryRepository.saveAndFlush(portfolioEntry);

        int databaseSizeBeforeDelete = portfolioEntryRepository.findAll().size();

        // Delete the portfolioEntry
        restPortfolioEntryMockMvc.perform(delete("/api/portfolio-entries/{id}", portfolioEntry.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PortfolioEntry> portfolioEntryList = portfolioEntryRepository.findAll();
        assertThat(portfolioEntryList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the PortfolioEntry in Elasticsearch
        verify(mockPortfolioEntrySearchRepository, times(1)).deleteById(portfolioEntry.getId());
    }

    @Test
    @Transactional
    void searchPortfolioEntry() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        portfolioEntryRepository.saveAndFlush(portfolioEntry);
        when(mockPortfolioEntrySearchRepository.search(queryStringQuery("id:" + portfolioEntry.getId())))
            .thenReturn(Collections.singletonList(portfolioEntry));

        // Search the portfolioEntry
        restPortfolioEntryMockMvc.perform(get("/api/_search/portfolio-entries?query=id:" + portfolioEntry.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(portfolioEntry.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(sameNumber(DEFAULT_PRICE))))
            .andExpect(jsonPath("$.[*].bought").value(hasItem(DEFAULT_BOUGHT.toString())))
            .andExpect(jsonPath("$.[*].sold").value(hasItem(DEFAULT_SOLD.toString())))
            .andExpect(jsonPath("$.[*].customName").value(hasItem(DEFAULT_CUSTOM_NAME)))
            .andExpect(jsonPath("$.[*].group1").value(hasItem(DEFAULT_GROUP_1)))
            .andExpect(jsonPath("$.[*].group2").value(hasItem(DEFAULT_GROUP_2)))
            .andExpect(jsonPath("$.[*].group3").value(hasItem(DEFAULT_GROUP_3)))
            .andExpect(jsonPath("$.[*].group4").value(hasItem(DEFAULT_GROUP_4)));
    }
}