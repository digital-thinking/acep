package de.ixeption.acep.web.rest;

import de.ixeption.acep.IntegrationTest;
import de.ixeption.acep.domain.Asset;
import de.ixeption.acep.domain.enumeration.AssetType;
import de.ixeption.acep.domain.enumeration.Currency;
import de.ixeption.acep.domain.enumeration.Source;
import de.ixeption.acep.repository.AssetRepository;
import de.ixeption.acep.repository.search.AssetSearchRepository;
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
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link AssetResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class AssetResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Currency DEFAULT_CURRENCY = Currency.AED;
    private static final Currency UPDATED_CURRENCY = Currency.USD;

    private static final AssetType DEFAULT_ASSET_TYPE = AssetType.Stock;
    private static final AssetType UPDATED_ASSET_TYPE = AssetType.Forex;

    private static final String DEFAULT_SYMBOL = "AAAAAAAAAA";
    private static final String UPDATED_SYMBOL = "BBBBBBBBBB";

    private static final Source DEFAULT_SOURCE = Source.AlphaVantage;
    private static final Source UPDATED_SOURCE = Source.Binance;

    @Autowired
    private AssetRepository assetRepository;

    /**
     * This repository is mocked in the de.ixeption.acep.repository.search test package.
     *
     * @see de.ixeption.acep.repository.search.AssetSearchRepositoryMockConfiguration
     */
    @Autowired
    private AssetSearchRepository mockAssetSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAssetMockMvc;

    private Asset asset;

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Asset createEntity(EntityManager em) {
        Asset asset = new Asset()
            .name(DEFAULT_NAME)
            .currency(DEFAULT_CURRENCY)
            .assetType(DEFAULT_ASSET_TYPE)
            .symbol(DEFAULT_SYMBOL)
            .source(DEFAULT_SOURCE);
        return asset;
    }

    /**
     * Create an updated entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Asset createUpdatedEntity(EntityManager em) {
        Asset asset = new Asset()
            .name(UPDATED_NAME)
            .currency(UPDATED_CURRENCY)
            .assetType(UPDATED_ASSET_TYPE)
            .symbol(UPDATED_SYMBOL)
            .source(UPDATED_SOURCE);
        return asset;
    }

    @BeforeEach
    public void initTest() {
        asset = createEntity(em);
    }

    @Test
    @Transactional
    void createAsset() throws Exception {
        int databaseSizeBeforeCreate = assetRepository.findAll().size();
        // Create the Asset
        restAssetMockMvc.perform(post("/api/assets").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(asset)))
            .andExpect(status().isCreated());

        // Validate the Asset in the database
        List<Asset> assetList = assetRepository.findAll();
        assertThat(assetList).hasSize(databaseSizeBeforeCreate + 1);
        Asset testAsset = assetList.get(assetList.size() - 1);
        assertThat(testAsset.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testAsset.getCurrency()).isEqualTo(DEFAULT_CURRENCY);
        assertThat(testAsset.getAssetType()).isEqualTo(DEFAULT_ASSET_TYPE);
        assertThat(testAsset.getSymbol()).isEqualTo(DEFAULT_SYMBOL);
        assertThat(testAsset.getSource()).isEqualTo(DEFAULT_SOURCE);

        // Validate the Asset in Elasticsearch
        verify(mockAssetSearchRepository, times(1)).save(testAsset);
    }

    @Test
    @Transactional
    void createAssetWithExistingId() throws Exception {
        // Create the Asset with an existing ID
        asset.setId(1L);

        int databaseSizeBeforeCreate = assetRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAssetMockMvc.perform(post("/api/assets").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(asset)))
            .andExpect(status().isBadRequest());

        // Validate the Asset in the database
        List<Asset> assetList = assetRepository.findAll();
        assertThat(assetList).hasSize(databaseSizeBeforeCreate);

        // Validate the Asset in Elasticsearch
        verify(mockAssetSearchRepository, times(0)).save(asset);
    }


    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = assetRepository.findAll().size();
        // set the field null
        asset.setName(null);

        // Create the Asset, which fails.


        restAssetMockMvc.perform(post("/api/assets").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(asset)))
            .andExpect(status().isBadRequest());

        List<Asset> assetList = assetRepository.findAll();
        assertThat(assetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCurrencyIsRequired() throws Exception {
        int databaseSizeBeforeTest = assetRepository.findAll().size();
        // set the field null
        asset.setCurrency(null);

        // Create the Asset, which fails.


        restAssetMockMvc.perform(post("/api/assets").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(asset)))
            .andExpect(status().isBadRequest());

        List<Asset> assetList = assetRepository.findAll();
        assertThat(assetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAssetTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = assetRepository.findAll().size();
        // set the field null
        asset.setAssetType(null);

        // Create the Asset, which fails.


        restAssetMockMvc.perform(post("/api/assets").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(asset)))
            .andExpect(status().isBadRequest());

        List<Asset> assetList = assetRepository.findAll();
        assertThat(assetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSymbolIsRequired() throws Exception {
        int databaseSizeBeforeTest = assetRepository.findAll().size();
        // set the field null
        asset.setSymbol(null);

        // Create the Asset, which fails.


        restAssetMockMvc.perform(post("/api/assets").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(asset)))
            .andExpect(status().isBadRequest());

        List<Asset> assetList = assetRepository.findAll();
        assertThat(assetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSourceIsRequired() throws Exception {
        int databaseSizeBeforeTest = assetRepository.findAll().size();
        // set the field null
        asset.setSource(null);

        // Create the Asset, which fails.


        restAssetMockMvc.perform(post("/api/assets").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(asset)))
            .andExpect(status().isBadRequest());

        List<Asset> assetList = assetRepository.findAll();
        assertThat(assetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAssets() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        // Get all the assetList
        restAssetMockMvc.perform(get("/api/assets?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(asset.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].currency").value(hasItem(DEFAULT_CURRENCY.toString())))
            .andExpect(jsonPath("$.[*].assetType").value(hasItem(DEFAULT_ASSET_TYPE.toString())))
            .andExpect(jsonPath("$.[*].symbol").value(hasItem(DEFAULT_SYMBOL)))
            .andExpect(jsonPath("$.[*].source").value(hasItem(DEFAULT_SOURCE.toString())));
    }

    @Test
    @Transactional
    void getAsset() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        // Get the asset
        restAssetMockMvc.perform(get("/api/assets/{id}", asset.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(asset.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.currency").value(DEFAULT_CURRENCY.toString()))
            .andExpect(jsonPath("$.assetType").value(DEFAULT_ASSET_TYPE.toString()))
            .andExpect(jsonPath("$.symbol").value(DEFAULT_SYMBOL))
            .andExpect(jsonPath("$.source").value(DEFAULT_SOURCE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingAsset() throws Exception {
        // Get the asset
        restAssetMockMvc.perform(get("/api/assets/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void updateAsset() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        int databaseSizeBeforeUpdate = assetRepository.findAll().size();

        // Update the asset
        Asset updatedAsset = assetRepository.findById(asset.getId()).get();
        // Disconnect from session so that the updates on updatedAsset are not directly saved in db
        em.detach(updatedAsset);
        updatedAsset
            .name(UPDATED_NAME)
            .currency(UPDATED_CURRENCY)
            .assetType(UPDATED_ASSET_TYPE)
            .symbol(UPDATED_SYMBOL)
            .source(UPDATED_SOURCE);

        restAssetMockMvc.perform(put("/api/assets").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedAsset)))
            .andExpect(status().isOk());

        // Validate the Asset in the database
        List<Asset> assetList = assetRepository.findAll();
        assertThat(assetList).hasSize(databaseSizeBeforeUpdate);
        Asset testAsset = assetList.get(assetList.size() - 1);
        assertThat(testAsset.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAsset.getCurrency()).isEqualTo(UPDATED_CURRENCY);
        assertThat(testAsset.getAssetType()).isEqualTo(UPDATED_ASSET_TYPE);
        assertThat(testAsset.getSymbol()).isEqualTo(UPDATED_SYMBOL);
        assertThat(testAsset.getSource()).isEqualTo(UPDATED_SOURCE);

        // Validate the Asset in Elasticsearch
        verify(mockAssetSearchRepository).save(testAsset);
    }

    @Test
    @Transactional
    void updateNonExistingAsset() throws Exception {
        int databaseSizeBeforeUpdate = assetRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAssetMockMvc.perform(put("/api/assets").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(asset)))
            .andExpect(status().isBadRequest());

        // Validate the Asset in the database
        List<Asset> assetList = assetRepository.findAll();
        assertThat(assetList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Asset in Elasticsearch
        verify(mockAssetSearchRepository, times(0)).save(asset);
    }


    @Test
    @Transactional
    void partialUpdateAssetWithPatch() throws Exception {

// Initialize the database
        assetRepository.saveAndFlush(asset);

        int databaseSizeBeforeUpdate = assetRepository.findAll().size();

// Update the asset using partial update
        Asset partialUpdatedAsset = new Asset();
        partialUpdatedAsset.setId(asset.getId());

        partialUpdatedAsset
            .currency(UPDATED_CURRENCY)
            .assetType(UPDATED_ASSET_TYPE);

        restAssetMockMvc.perform(patch("/api/assets").with(csrf())
            .contentType("application/merge-patch+json")
            .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAsset)))
            .andExpect(status().isOk());

// Validate the Asset in the database
        List<Asset> assetList = assetRepository.findAll();
        assertThat(assetList).hasSize(databaseSizeBeforeUpdate);
        Asset testAsset = assetList.get(assetList.size() - 1);
        assertThat(testAsset.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testAsset.getCurrency()).isEqualTo(UPDATED_CURRENCY);
        assertThat(testAsset.getAssetType()).isEqualTo(UPDATED_ASSET_TYPE);
        assertThat(testAsset.getSymbol()).isEqualTo(DEFAULT_SYMBOL);
        assertThat(testAsset.getSource()).isEqualTo(DEFAULT_SOURCE);
    }

    @Test
    @Transactional
    void fullUpdateAssetWithPatch() throws Exception {

// Initialize the database
        assetRepository.saveAndFlush(asset);

        int databaseSizeBeforeUpdate = assetRepository.findAll().size();

// Update the asset using partial update
        Asset partialUpdatedAsset = new Asset();
        partialUpdatedAsset.setId(asset.getId());

        partialUpdatedAsset
            .name(UPDATED_NAME)
            .currency(UPDATED_CURRENCY)
            .assetType(UPDATED_ASSET_TYPE)
            .symbol(UPDATED_SYMBOL)
            .source(UPDATED_SOURCE);

        restAssetMockMvc.perform(patch("/api/assets").with(csrf())
            .contentType("application/merge-patch+json")
            .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAsset)))
            .andExpect(status().isOk());

// Validate the Asset in the database
        List<Asset> assetList = assetRepository.findAll();
        assertThat(assetList).hasSize(databaseSizeBeforeUpdate);
        Asset testAsset = assetList.get(assetList.size() - 1);
        assertThat(testAsset.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAsset.getCurrency()).isEqualTo(UPDATED_CURRENCY);
        assertThat(testAsset.getAssetType()).isEqualTo(UPDATED_ASSET_TYPE);
        assertThat(testAsset.getSymbol()).isEqualTo(UPDATED_SYMBOL);
        assertThat(testAsset.getSource()).isEqualTo(UPDATED_SOURCE);
    }

    @Test
    @Transactional
    void partialUpdateAssetShouldThrown() throws Exception {
        // Update the asset without id should throw
        Asset partialUpdatedAsset = new Asset();

        restAssetMockMvc.perform(patch("/api/assets").with(csrf())
            .contentType("application/merge-patch+json")
            .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAsset)))
            .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    void deleteAsset() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        int databaseSizeBeforeDelete = assetRepository.findAll().size();

        // Delete the asset
        restAssetMockMvc.perform(delete("/api/assets/{id}", asset.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Asset> assetList = assetRepository.findAll();
        assertThat(assetList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Asset in Elasticsearch
        verify(mockAssetSearchRepository, times(1)).deleteById(asset.getId());
    }

    @Test
    @Transactional
    void searchAsset() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        assetRepository.saveAndFlush(asset);
        when(mockAssetSearchRepository.search(queryStringQuery("id:" + asset.getId())))
            .thenReturn(Collections.singletonList(asset));

        // Search the asset
        restAssetMockMvc.perform(get("/api/_search/assets?query=id:" + asset.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(asset.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].currency").value(hasItem(DEFAULT_CURRENCY.toString())))
            .andExpect(jsonPath("$.[*].assetType").value(hasItem(DEFAULT_ASSET_TYPE.toString())))
            .andExpect(jsonPath("$.[*].symbol").value(hasItem(DEFAULT_SYMBOL)))
            .andExpect(jsonPath("$.[*].source").value(hasItem(DEFAULT_SOURCE.toString())));
    }
}
