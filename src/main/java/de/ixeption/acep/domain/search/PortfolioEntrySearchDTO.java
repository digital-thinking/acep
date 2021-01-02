package de.ixeption.acep.domain.search;

import de.ixeption.acep.domain.PortfolioEntry;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.Id;

@Document(indexName = "portfolioentry")
public class PortfolioEntrySearchDTO {

    @Id
    private Long id;
    private String customName;
    private String group1;
    private String group2;
    private String group3;
    private String group4;

    // needed for elastic search
    public PortfolioEntrySearchDTO() {
    }

    public PortfolioEntrySearchDTO(PortfolioEntry portfolioEntry) {
        this.id = portfolioEntry.getId();
        this.customName = portfolioEntry.getCustomName();
        this.group1 = portfolioEntry.getGroup1();
        this.group2 = portfolioEntry.getGroup2();
        this.group3 = portfolioEntry.getGroup3();
        this.group4 = portfolioEntry.getGroup4();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomName() {
        return customName;
    }

    public void setCustomName(String customName) {
        this.customName = customName;
    }

    public String getGroup1() {
        return group1;
    }

    public void setGroup1(String group1) {
        this.group1 = group1;
    }

    public String getGroup2() {
        return group2;
    }

    public void setGroup2(String group2) {
        this.group2 = group2;
    }

    public String getGroup3() {
        return group3;
    }

    public void setGroup3(String group3) {
        this.group3 = group3;
    }

    public String getGroup4() {
        return group4;
    }

    public void setGroup4(String group4) {
        this.group4 = group4;
    }
}
