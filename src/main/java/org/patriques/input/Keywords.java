package org.patriques.input;

/**
 * The symbol parameter for the technical indicators/time series api call.
 */
public class Keywords implements ApiParameter {
    private String keywords;

    public Keywords(String keywords) {
        this.keywords = keywords;
    }

    @Override
    public String getKey() {
        return "keywords";
    }

    @Override
    public String getValue() {
        return keywords;
    }
}
