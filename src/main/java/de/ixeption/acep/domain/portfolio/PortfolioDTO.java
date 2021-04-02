package de.ixeption.acep.domain.portfolio;

public class PortfolioDTO {

    private Portfolio portfolio;
    private PortfolioNumbers portfoliosNumbers;


    public Portfolio getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(Portfolio portfolio) {
        this.portfolio = portfolio;
    }

    public PortfolioNumbers getPortfoliosNumbers() {
        return portfoliosNumbers;
    }

    public void setPortfoliosNumbers(PortfolioNumbers portfoliosNumbers) {
        this.portfoliosNumbers = portfoliosNumbers;
    }
}
