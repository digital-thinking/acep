package de.ixeption.acep.domain.portfolio;

import de.ixeption.acep.domain.enumeration.Currency;

import java.math.BigDecimal;

public class PortfolioNumbers {

    private double performancePercent = 0.0;
    private BigDecimal performanceAmount = BigDecimal.ZERO;
    private Currency currency = Currency.EUR;

    public double getPerformancePercent() {
        return performancePercent;
    }

    public void setPerformancePercent(double performancePercent) {
        this.performancePercent = performancePercent;
    }

    public BigDecimal getPerformanceAmount() {
        return performanceAmount;
    }

    public void setPerformanceAmount(BigDecimal performanceAmount) {
        this.performanceAmount = performanceAmount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }
}
