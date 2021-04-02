package de.ixeption.acep.service.portfolio;

import de.ixeption.acep.domain.Asset;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

@Service
public class AssetPriceService {
    public AssetPrice getCurrentPrice(Asset asset) {
        return new AssetPrice(BigDecimal.ONE, BigDecimal.ZERO, BigDecimal.ONE, BigDecimal.ZERO, BigDecimal.TEN, BigDecimal.ONE, new Date());

    }
}
