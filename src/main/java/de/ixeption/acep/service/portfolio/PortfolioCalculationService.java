package de.ixeption.acep.service.portfolio;

import de.ixeption.acep.domain.enumeration.Currency;
import de.ixeption.acep.domain.portfolio.Portfolio;
import de.ixeption.acep.domain.portfolio.PortfolioEntry;
import de.ixeption.acep.domain.portfolio.PortfolioNumbers;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Set;

@Service
public class PortfolioCalculationService {

    private final AssetPriceService assetPriceService;

    public PortfolioCalculationService(AssetPriceService assetPriceService) {
        this.assetPriceService = assetPriceService;
    }

    public PortfolioNumbers calculatePortfolioNumbers(Portfolio portfolio) {
        Set<PortfolioEntry> portfolioEntries = portfolio.getPortfolioEntries();
        PortfolioNumbers numbers = new PortfolioNumbers();
        if (!portfolioEntries.isEmpty()) {
            BigDecimal sumPaid = new BigDecimal(0);
            BigDecimal sumNow = new BigDecimal(0);
            for (PortfolioEntry portfolioEntry :
                portfolioEntries) {
                AssetPrice currentPrice = assetPriceService.getCurrentPrice(portfolioEntry.getAsset());
                BigDecimal price = currentPrice.getPrice();
                BigDecimal entryValue = price.multiply(BigDecimal.valueOf(portfolioEntry.getAmount()));
                BigDecimal buyValue = portfolioEntry.getPrice().multiply(BigDecimal.valueOf(portfolioEntry.getAmount()));
                sumPaid = sumPaid.add(buyValue);
                sumNow = sumNow.add(entryValue);
            }

            numbers.setCurrency(Currency.EUR);
            numbers.setPerformanceAmount(sumNow.subtract(sumPaid));
            numbers.setPerformancePercent(sumNow.divide(sumPaid, RoundingMode.HALF_UP).doubleValue());
        }
        return numbers;
    }
}
