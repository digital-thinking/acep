package de.ixeption.acep.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * A PortfolioEntry.
 */
@Entity
@Table(name = "portfolio_entry")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PortfolioEntry implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "amount", nullable = false)
    private Long amount;

    @NotNull
    @Column(name = "price", precision = 21, scale = 2, nullable = false)
    private BigDecimal price;

    @NotNull
    @Column(name = "bought", nullable = false)
    private LocalDateTime bought;

    @Column(name = "sold")
    private LocalDateTime sold;

    @Column(name = "custom_name")
    private String customName;

    @Column(name = "group_1")
    private String group1;

    @Column(name = "group_2")
    private String group2;

    @Column(name = "group_3")
    private String group3;

    @Column(name = "group_4")
    private String group4;

    @ManyToOne
    @JsonIgnoreProperties(value = {
        "portfolioEntries",
    }, allowSetters = true)
    private Portfolio portfolio;


    @ManyToOne
    @JsonIgnoreProperties(value = {
        "portfolioEntries",
    }, allowSetters = true)
    private Asset asset;


    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PortfolioEntry id(Long id) {
        this.id = id;
        return this;
    }

    public Long getAmount() {
        return this.amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public PortfolioEntry amount(Long amount) {
        this.amount = amount;
        return this;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public PortfolioEntry price(BigDecimal price) {
        this.price = price;
        return this;
    }

    public LocalDateTime getBought() {
        return this.bought;
    }

    public void setBought(LocalDateTime bought) {
        this.bought = bought;
    }

    public PortfolioEntry bought(LocalDateTime bought) {
        this.bought = bought;
        return this;
    }

    public LocalDateTime getSold() {
        return this.sold;
    }

    public void setSold(LocalDateTime sold) {
        this.sold = sold;
    }

    public PortfolioEntry sold(LocalDateTime sold) {
        this.sold = sold;
        return this;
    }

    public String getCustomName() {
        return this.customName;
    }

    public void setCustomName(String customName) {
        this.customName = customName;
    }

    public PortfolioEntry customName(String customName) {
        this.customName = customName;
        return this;
    }

    public String getGroup1() {
        return this.group1;
    }

    public void setGroup1(String group1) {
        this.group1 = group1;
    }

    public PortfolioEntry group1(String group1) {
        this.group1 = group1;
        return this;
    }

    public String getGroup2() {
        return this.group2;
    }

    public void setGroup2(String group2) {
        this.group2 = group2;
    }

    public PortfolioEntry group2(String group2) {
        this.group2 = group2;
        return this;
    }

    public String getGroup3() {
        return this.group3;
    }

    public void setGroup3(String group3) {
        this.group3 = group3;
    }

    public PortfolioEntry group3(String group3) {
        this.group3 = group3;
        return this;
    }

    public String getGroup4() {
        return this.group4;
    }

    public void setGroup4(String group4) {
        this.group4 = group4;
    }

    public PortfolioEntry group4(String group4) {
        this.group4 = group4;
        return this;
    }

    public Portfolio getPortfolio() {
        return this.portfolio;
    }

    public void setPortfolio(Portfolio portfolio) {
        this.portfolio = portfolio;
    }

    public PortfolioEntry portfolio(Portfolio portfolio) {
        this.setPortfolio(portfolio);
        return this;
    }

    public Asset getAsset() {
        return this.asset;
    }

    public void setAsset(Asset asset) {
        this.asset = asset;
    }

    public PortfolioEntry asset(Asset asset) {
        this.setAsset(asset);
        return this;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PortfolioEntry)) {
            return false;
        }
        return id != null && id.equals(((PortfolioEntry) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PortfolioEntry{" +
            "id=" + getId() +
            ", amount=" + getAmount() +
            ", price=" + getPrice() +
            ", bought='" + getBought() + "'" +
            ", sold='" + getSold() + "'" +
            ", customName='" + getCustomName() + "'" +
            ", group1='" + getGroup1() + "'" +
            ", group2='" + getGroup2() + "'" +
            ", group3='" + getGroup3() + "'" +
            ", group4='" + getGroup4() + "'" +
            "}";
    }
}
