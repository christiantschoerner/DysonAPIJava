package org.tschoerner.christian.devices;

public enum DysonRequestType {

    STATE("REQUEST-CURRENT-STATE"),
    SENSORDATA("REQUEST-PRODUCT-ENVIRONMENT-CURRENT-SENSOR-DATA");

    private final String query;

    DysonRequestType(String query){
        this.query = query;
    }

    public String getQuery() {
        return query;
    }
}
