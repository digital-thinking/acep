package de.ixeption.acep.web.rest;

import de.ixeption.acep.IntegrationTest;
import de.ixeption.acep.domain.Portfolio;
import de.ixeption.acep.domain.PortfolioEntry;
import de.ixeption.acep.domain.User;
import de.ixeption.acep.repository.PortfolioEntryRepository;
import de.ixeption.acep.repository.PortfolioRepository;
import de.ixeption.acep.repository.UserRepository;
import de.ixeption.acep.repository.search.PortfolioEntrySearchRepository;
import de.ixeption.acep.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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

    private static final LocalDateTime DEFAULT_BOUGHT = LocalDateTime.ofEpochSecond(100, 0, ZoneOffset.MIN);
    private static final LocalDateTime UPDATED_BOUGHT = LocalDateTime.now();

    private static final LocalDateTime DEFAULT_SOLD = LocalDateTime.ofEpochSecond(100, 0, ZoneOffset.MIN);
    private static final LocalDateTime UPDATED_SOLD = LocalDateTime.now();

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
    @MockBean
    UserService userService;

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
    @Autowired
    private PortfolioRepository portfolioRepository;
    @Autowired
    private UserRepository userRepository;

    private PortfolioEntry portfolioEntry;
    private Portfolio portfolio;


    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PortfolioEntry createEntity(EntityManager em) {
        return new PortfolioEntry()
            .amount(DEFAULT_AMOUNT)
            .price(DEFAULT_PRICE)
            .bought(DEFAULT_BOUGHT)
            .sold(DEFAULT_SOLD)
            .customName(DEFAULT_CUSTOM_NAME)
            .group1(DEFAULT_GROUP_1)
            .group2(DEFAULT_GROUP_2)
            .group3(DEFAULT_GROUP_3)
            .group4(DEFAULT_GROUP_4);
    }

    /**
     * Create an updated entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PortfolioEntry createUpdatedEntity(EntityManager em) {
        return new PortfolioEntry()
            .amount(UPDATED_AMOUNT)
            .price(UPDATED_PRICE)
            .bought(UPDATED_BOUGHT)
            .sold(UPDATED_SOLD)
            .customName(UPDATED_CUSTOM_NAME)
            .group1(UPDATED_GROUP_1)
            .group2(UPDATED_GROUP_2)
            .group3(UPDATED_GROUP_3)
            .group4(UPDATED_GROUP_4);
    }

    @BeforeEach
    public void initTest() {
        User user = UserResourceIT.initTestUser(userRepository, em);
        userRepository.saveAndFlush(user);
        portfolio = PortfolioResourceIT.createEntity(em).user(user);
        portfolioEntry = createEntity(em).portfolio(portfolio);
        portfolioRepository.saveAndFlush(portfolio);
        when(userService.getUserByName(UserResourceIT.DEFAULT_LOGIN)).thenReturn(Optional.of(user));
    }

    @Test
    @Transactional
    @WithMockUser(username = UserResourceIT.DEFAULT_LOGIN, authorities = "ROLE_USER")
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
    @WithMockUser(username = UserResourceIT.DEFAULT_LOGIN, authorities = "ROLE_USER")
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
    @WithMockUser(username = UserResourceIT.DEFAULT_LOGIN, authorities = "ROLE_USER")
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
    @WithMockUser(username = UserResourceIT.DEFAULT_LOGIN, authorities = "ROLE_USER")
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
    @WithMockUser(username = UserResourceIT.DEFAULT_LOGIN, authorities = "ROLE_USER")
    void getNonExistingPortfolioEntry() throws Exception {
        // Get the portfolioEntry
        restPortfolioEntryMockMvc.perform(get("/api/portfolio-entries/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    @WithMockUser(username = UserResourceIT.DEFAULT_LOGIN, authorities = "ROLE_USER")
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
            .group4(UPDATED_GROUP_4)
            .portfolio(portfolio);

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
    @WithMockUser(username = UserResourceIT.DEFAULT_LOGIN, authorities = "ROLE_USER")
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
    @WithMockUser(username = UserResourceIT.DEFAULT_LOGIN, authorities = "ROLE_USER")
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
    @WithMockUser(username = UserResourceIT.DEFAULT_LOGIN, authorities = "ROLE_USER")
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
            .group4(UPDATED_GROUP_4)
            .portfolio(portfolio);

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
    @WithMockUser(username = UserResourceIT.DEFAULT_LOGIN, authorities = "ROLE_USER")
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
    @WithMockUser(username = UserResourceIT.DEFAULT_LOGIN, authorities = "ROLE_USER")
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
    @WithMockUser(username = UserResourceIT.DEFAULT_LOGIN, authorities = "ROLE_USER")
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


    @Test
    @Transactional
    @WithMockUser(username = "Other", authorities = "ROLE_USER")
    void restrictUpdateAccess() throws Exception {
        portfolioEntryRepository.saveAndFlush(portfolioEntry);
        int databaseSizeBeforeCreate = portfolioEntryRepository.findAll().size();

        User otherUser = createOtherUser();
        Portfolio portfolio = PortfolioResourceIT.createEntity(em).user(otherUser);
        portfolioRepository.saveAndFlush(portfolio);

        PortfolioEntry updateIt = portfolioEntryRepository.findById(portfolioEntry.getId()).get();
        updateIt.setPortfolio(portfolio);
        em.detach(updateIt);

        restPortfolioEntryMockMvc.perform(put("/api/portfolio-entries").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(portfolioEntry)))
            .andExpect(status().isBadRequest());

        assertThat(databaseSizeBeforeCreate).isEqualTo(portfolioEntryRepository.findAll().size());

    }

    @Test
    @Transactional
    @WithMockUser(username = "Other", authorities = "ROLE_USER")
    void restrictDeleteAccess() throws Exception {
        portfolioEntryRepository.saveAndFlush(portfolioEntry);
        int databaseSizeBeforeCreate = portfolioEntryRepository.findAll().size();

        createOtherUser();

        // Delete the portfolioEntry
        restPortfolioEntryMockMvc.perform(delete("/api/portfolio-entries/{id}", portfolioEntry.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());

        assertThat(databaseSizeBeforeCreate).isEqualTo(portfolioEntryRepository.findAll().size());

    }

    @Test
    @Transactional
    @WithMockUser(username = "Other", authorities = "ROLE_USER")
    void restrictGetAccess() throws Exception {
        // Initialize the database
        portfolioEntryRepository.saveAndFlush(portfolioEntry);
        createOtherUser();

        // Get the portfolioEntry
        restPortfolioEntryMockMvc.perform(get("/api/portfolio-entries/{id}", portfolioEntry.getId()))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    @WithMockUser(username = "Other", authorities = "ROLE_USER")
    void restrictSearchAccess() throws Exception {
        portfolioEntryRepository.saveAndFlush(portfolioEntry);
        int databaseSizeBeforeCreate = portfolioEntryRepository.findAll().size();
        createOtherUser();

        when(mockPortfolioEntrySearchRepository.search(queryStringQuery("id:" + portfolioEntry.getId())))
            .thenReturn(Collections.singletonList(portfolioEntry));

        // Search the portfolioEntry
        restPortfolioEntryMockMvc.perform(get("/api/_search/portfolio-entries?query=id:" + portfolioEntry.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("[]"))
            .andExpect(jsonPath("$.[*].id").isEmpty());


        assertThat(databaseSizeBeforeCreate).isEqualTo(portfolioEntryRepository.findAll().size());

    }

    private User createOtherUser() {
        User otherUser = UserResourceIT.initTestUser(userRepository, em);
        otherUser.setLogin("Other");
        otherUser.setEmail("test@test.de");
        userRepository.saveAndFlush(otherUser);
        when(userService.getUserByName("Other")).thenReturn(Optional.of(otherUser));
        return otherUser;
    }
}
