package org.patriques.output.search;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;

public class QueryResultData {
    List<QueryResultEntry> resultEntryList = new ArrayList<>();

    public List<QueryResultEntry> getResultEntryList() {
        return resultEntryList;
    }

    public void addQueryResultEntry(QueryResultEntry queryResultEntry) {
        resultEntryList.add(queryResultEntry);
    }

    public static class QueryResultEntry {
        private String symbol;
        private String name;
        private String type;
        private String region;
        private String marketOpenTime;
        private String marketCloseTime;
        private String timezone;
        private String currency;
        private Double score;

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
        }

        public String getMarketOpenTime() {
            return marketOpenTime;
        }

        public void setMarketOpenTime(String marketOpenTime) {
            this.marketOpenTime = marketOpenTime;
        }

        public String getMarketCloseTime() {
            return marketCloseTime;
        }

        public void setMarketCloseTime(String marketCloseTime) {
            this.marketCloseTime = marketCloseTime;
        }

        public String getTimezone() {
            return timezone;
        }

        public void setTimezone(String timezone) {
            this.timezone = timezone;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public Double getScore() {
            return score;
        }

        public void setScore(Double score) {
            this.score = score;
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                .append("symbol", symbol)
                .append("name", name)
                .append("type", type)
                .append("region", region)
                .append("marketOpenTime", marketOpenTime)
                .append("marketCloseTime", marketCloseTime)
                .append("timezone", timezone)
                .append("currency", currency)
                .append("score", score)
                .toString();
        }
    }
}
