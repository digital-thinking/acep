package de.ixeption.acep.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import de.ixeption.acep.domain.enumeration.AssetType;
import de.ixeption.acep.domain.enumeration.Currency;
import de.ixeption.acep.domain.enumeration.Source;
import de.ixeption.acep.domain.portfolio.PortfolioEntry;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Field;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Asset.
 */
@Entity
@Table(name = "asset")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "asset")
public class Asset implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Field(index = false, docValues = false)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "currency", nullable = false)
    private Currency currency;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "asset_type", nullable = false)
    private AssetType assetType;

    @NotNull
    @Column(name = "symbol", nullable = false)
    private String symbol;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "source", nullable = false)
    private Source source;

    @OneToMany(mappedBy = "asset")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = {
        "portfolio",
        "asset",
    }, allowSetters = true)
    private Set<PortfolioEntry> portfolioEntries = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Asset id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Asset name(String name) {
        this.name = name;
        return this;
    }

    public Currency getCurrency() {
        return this.currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Asset currency(Currency currency) {
        this.currency = currency;
        return this;
    }

    public AssetType getAssetType() {
        return this.assetType;
    }

    public void setAssetType(AssetType assetType) {
        this.assetType = assetType;
    }

    public Asset assetType(AssetType assetType) {
        this.assetType = assetType;
        return this;
    }

    public String getSymbol() {
        return this.symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Asset symbol(String symbol) {
        this.symbol = symbol;
        return this;
    }

    public Source getSource() {
        return this.source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public Asset source(Source source) {
        this.source = source;
        return this;
    }

    public Set<PortfolioEntry> getPortfolioEntries() {
        return this.portfolioEntries;
    }

    public void setPortfolioEntries(Set<PortfolioEntry> portfolioEntries) {
        if (this.portfolioEntries != null) {
            this.portfolioEntries.forEach(i -> i.setAsset(null));
        }
        if (portfolioEntries != null) {
            portfolioEntries.forEach(i -> i.setAsset(this));
        }
        this.portfolioEntries = portfolioEntries;
    }

    public Asset portfolioEntries(Set<PortfolioEntry> portfolioEntries) {
        this.setPortfolioEntries(portfolioEntries);
        return this;
    }

    public Asset addPortfolioEntry(PortfolioEntry portfolioEntry) {
        this.portfolioEntries.add(portfolioEntry);
        portfolioEntry.setAsset(this);
        return this;
    }

    public Asset removePortfolioEntry(PortfolioEntry portfolioEntry) {
        this.portfolioEntries.remove(portfolioEntry);
        portfolioEntry.setAsset(null);
        return this;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Asset)) {
            return false;
        }
        return id != null && id.equals(((Asset) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Asset{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", currency='" + getCurrency() + "'" +
            ", assetType='" + getAssetType() + "'" +
            ", symbol='" + getSymbol() + "'" +
            ", source='" + getSource() + "'" +
            "}";
    }
}
