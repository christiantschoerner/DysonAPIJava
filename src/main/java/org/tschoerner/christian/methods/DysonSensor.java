package org.tschoerner.christian.methods;

import java.text.NumberFormat;

public class DysonSensor {

    private String message;
    private String time;
    private double temperature;
    private String humidity;
    private String dust;
    private String volatilOrganicCompounds;
    private String sleepTimer;

    public DysonSensor(String message, String time, double temperature, String humidity, String dust, String volatilOrganicCompounds, String sleepTimer){
        this.message = message;
        this.time = time;

        this.temperature = temperature;
        this.humidity = humidity;
        this.dust = dust;
        this.volatilOrganicCompounds = volatilOrganicCompounds;
        this.sleepTimer = sleepTimer;
    }

    public String getTime() {
        return time;
    }

    public String getMessage() {
        return message;
    }

    public double getTemperatureKelvin() {
        return temperature;
    }

    public double getTemperatureCelsius(){
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(2);
        numberFormat.setMinimumFractionDigits(2);

        Double celsius = getTemperatureKelvin() - 273.15;

        return Double.valueOf(numberFormat.format(celsius).replaceAll(",", "."));
    }

    public double getTemperatureFahrenheit(){
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(2);
        numberFormat.setMinimumFractionDigits(2);

        Double fahrenheit = (getTemperatureCelsius() * 9/5) + 32;
        return Double.valueOf(numberFormat.format(fahrenheit).replaceAll(",", "."));
    }

    public String getDust() {
        return dust;
    }

    public String getHumidity() {
        return humidity;
    }

    public String getSleepTimer() {
        return sleepTimer;
    }

    public String getVolatilOrganicCompounds() {
        return volatilOrganicCompounds;
    }
}
