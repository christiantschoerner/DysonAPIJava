package org.tschoerner.christian.methods;

public class DysonState475 {

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
    private String filterLifeRemaining;
    private boolean nightMode;

    public DysonState475(String message, String time, String modeReason, String stateReason, String mode, String status,
                         String fanSpeed, String airQuality, boolean oscillation, String filterLifeRemaining, boolean nightMode){

        this.message = message;
        this.time = time;
        this.modeReason = modeReason;
        this.stateReason = stateReason;
        this.mode = mode;
        this.status = status;
        this.fanSpeed = fanSpeed;
        this.airQuality = airQuality;
        this.oscillation = oscillation;
        this.filterLifeRemaining = filterLifeRemaining;
        this.nightMode = nightMode;
    }

    public boolean isNightMode() {
        return nightMode;
    }

    public boolean isOscillation() {
        return oscillation;
    }

    public int getFanSpeedInt() {
        if(fanSpeed.charAt(2) == '0'){ // != 10
            fanSpeedInt = Integer.valueOf(fanSpeed.replaceAll("0", ""));
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
}
