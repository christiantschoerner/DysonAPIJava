package org.tschoerner.christian.methods;

public class DysonState455 {

    private String message;
    private String time;
    private String modeReason;
    private String stateReason;
    private String mode;
    private String status;
    private String fanSpeed;
    private int fanSpeedInt;
    private String airQuality;
    private boolean oscillation;
    private boolean continuousMesurement;
    private String filterLifeRemaining;
    private boolean nightMode;
    private String heaterMode;
    private String targetTemperature;
    private boolean heating;
    private boolean heaterFocus;

    public DysonState455(String message, String time, String modeReason, String stateReason, String mode, String status,
                         String fanSpeed, String airQuality, boolean oscillation, boolean continuousMesurement, String filterLifeRemaining,
                         boolean nightMode, String heaterMode, String targetTemperature, boolean heating, boolean heaterFocus){

        this.message = message;
        this.time = time;
        this.modeReason = modeReason;
        this.stateReason = stateReason;
        this.mode = mode;
        this.status = status;
        this.fanSpeed = fanSpeed;
        this.airQuality = airQuality;
        this.oscillation = oscillation;
        this.continuousMesurement = continuousMesurement;
        this.filterLifeRemaining = filterLifeRemaining;
        this.nightMode = nightMode;
        this.heaterMode = heaterMode;
        this.targetTemperature = targetTemperature;
        this.heating = heating;
        this.heaterFocus = heaterFocus;
    }

    public boolean isNightMode() {
        return nightMode;
    }

    public boolean isOscillation() {
        return oscillation;
    }

    public int getFanSpeedInt() {
        if(fanSpeed.charAt(2) == '0'){ // != 10
            fanSpeedInt = Integer.parseInt(fanSpeed.replaceAll("0", ""));
        }else{
            fanSpeedInt = 10;
        }
        return fanSpeedInt;
    }

    public String getAirQuality() {
        return airQuality;
    }

    public int getAirQualityInt(){
        return Integer.valueOf(String.valueOf(airQuality).replaceAll("0", ""));
    }

    public String getFanSpeed() {
        return fanSpeed;
    }

    public String getFilterLifeRemaining() {
        return filterLifeRemaining;
    }

    public String getMessage() {
        return message;
    }

    public String getMode() {
        return mode;
    }

    public String getModeReason() {
        return modeReason;
    }

    public String getStateReason() {
        return stateReason;
    }

    public String getTime() {
        return time;
    }

    public String getStatus() {
        return status;
    }

    public String getHeaterMode() {
        return heaterMode;
    }

    public String getTargetTemperature() {
        return targetTemperature;
    }

    public boolean isHeating() {
        return heating;
    }

    public boolean isContinuousMesurement() {
        return continuousMesurement;
    }

    public boolean isHeaterFocus() {
        return heaterFocus;
    }
}
