package de.ixeption.acep.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A Portfolio.
 */
@Entity
@Table(name = "portfolio")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "portfolio")
public class Portfolio implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @Column(name = "created")
    private Instant created;

    @OneToMany(mappedBy = "portfolio")
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

    public Portfolio id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Portfolio name(String name) {
        this.name = name;
        return this;
    }

    public Instant getCreated() {
        return this.created;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public Portfolio created(Instant created) {
        this.created = created;
        return this;
    }

    public Set<PortfolioEntry> getPortfolioEntries() {
        return this.portfolioEntries;
    }

    public void setPortfolioEntries(Set<PortfolioEntry> portfolioEntries) {
        if (this.portfolioEntries != null) {
            this.portfolioEntries.forEach(i -> i.setPortfolio(null));
        }
        if (portfolioEntries != null) {
            portfolioEntries.forEach(i -> i.setPortfolio(this));
        }
        this.portfolioEntries = portfolioEntries;
    }

    public Portfolio portfolioEntries(Set<PortfolioEntry> portfolioEntries) {
        this.setPortfolioEntries(portfolioEntries);
        return this;
    }

    public Portfolio addPortfolioEntry(PortfolioEntry portfolioEntry) {
        this.portfolioEntries.add(portfolioEntry);
        portfolioEntry.setPortfolio(this);
        return this;
    }

    public Portfolio removePortfolioEntry(PortfolioEntry portfolioEntry) {
        this.portfolioEntries.remove(portfolioEntry);
        portfolioEntry.setPortfolio(null);
        return this;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Portfolio)) {
            return false;
        }
        return id != null && id.equals(((Portfolio) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Portfolio{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", created='" + getCreated() + "'" +
            "}";
    }
}
