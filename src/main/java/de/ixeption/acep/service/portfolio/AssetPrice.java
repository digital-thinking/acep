package de.ixeption.acep.service.portfolio;

import java.math.BigDecimal;
import java.util.Date;

public class AssetPrice {
    private final BigDecimal ask;
    private final BigDecimal bid;
    private final BigDecimal low;
    private final BigDecimal high;
    private final BigDecimal volume;
    private final BigDecimal price;

    private final Date date;

    public AssetPrice(BigDecimal ask, BigDecimal bid, BigDecimal low, BigDecimal high, BigDecimal volume, BigDecimal price, Date date) {
        this.ask = ask;
        this.bid = bid;
        this.low = low;
        this.high = high;
        this.volume = volume;
        this.price = price;
        this.date = date;
    }

    public BigDecimal getAsk() {
        return ask;
    }

    public BigDecimal getBid() {
        return bid;
    }

    public BigDecimal getLow() {
        return low;
    }

    public BigDecimal getHigh() {
        return high;
    }

    public Date getDate() {
        return date;
    }

    public BigDecimal getVolume() {
        return volume;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
