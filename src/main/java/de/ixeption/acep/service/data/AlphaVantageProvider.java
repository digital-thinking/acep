package de.ixeption.acep.service.data;

import org.patriques.AlphaVantageConnector;
import org.patriques.TimeSeries;
import org.patriques.output.search.QueryResultData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class AlphaVantageProvider {
    AlphaVantageConnector apiConnector;

    public AlphaVantageProvider(@Value("${application.alphaVantageApiKey}") String key) {
        apiConnector = new AlphaVantageConnector(key, 4000);
    }

    public QueryResultData query(String queryString) {
        TimeSeries stockTimeSeries = new TimeSeries(apiConnector);
        return stockTimeSeries.query(queryString);
    }


}
