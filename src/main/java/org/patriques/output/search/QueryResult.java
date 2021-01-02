package org.patriques.output.search;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.patriques.output.JsonParser;

public class QueryResult {
    public static QueryResultData from(String json) {
        QueryResultParser parser = new QueryResultParser();
        return parser.parseJson(json);
    }

    private static class QueryResultParser extends JsonParser<QueryResultData> {

        @Override
        protected QueryResultData resolve(JsonObject rootObject) {
            QueryResultData queryResultData = new QueryResultData();
            JsonArray bestMatches = rootObject.getAsJsonArray("bestMatches");
            for (JsonElement el : bestMatches) {
                QueryResultData.QueryResultEntry resultEntry = new QueryResultData.QueryResultEntry();
                resultEntry.setSymbol(((JsonObject) el).get("1. symbol").getAsString());
                resultEntry.setName(((JsonObject) el).get("2. name").getAsString());
                resultEntry.setType(((JsonObject) el).get("3. type").getAsString());
                resultEntry.setRegion(((JsonObject) el).get("4. region").getAsString());
                resultEntry.setMarketOpenTime(((JsonObject) el).get("5. marketOpen").getAsString());
                resultEntry.setMarketCloseTime(((JsonObject) el).get("6. marketClose").getAsString());
                resultEntry.setTimezone(((JsonObject) el).get("7. timezone").getAsString());
                resultEntry.setCurrency(((JsonObject) el).get("8. currency").getAsString());
                resultEntry.setScore(((JsonObject) el).get("9. matchScore").getAsDouble());
                queryResultData.addQueryResultEntry(resultEntry);
            }

            return queryResultData;
        }
    }


    //"bestMatches": [
    //        {
    //            "1. symbol": "TESO",
    //            "2. name": "Tesco Corporation USA",
    //            "3. type": "Equity",
    //            "4. region": "United States",
    //            "5. marketOpen": "09:30",
    //            "6. marketClose": "16:00",
    //            "7. timezone": "UTC-05",
    //            "8. currency": "USD",
    //            "9. matchScore": "0.8889"
    //        },


}
