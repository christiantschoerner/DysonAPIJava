package org.tschoerner.christian.devices.states;

public enum DysonStateEnum {

    MESSAGE("msg"),
    TIME("time"),
    MODE_REASON("mode-reason"),
    STATE_REASON("state-reason"),
    MODE("fmod"),
    STATUS("fnst"),
    FANSPEED("fnsp"),
    AIRQUALITY("qtar"),
    OSCILLATION("oson"),
    FILTER_LIFE_REMAINING("filf"),
    NIGHTMODE("nmod"),
    CONTINUOUS_MEASUREMENT("rhtm"),
    HEATERMODE("hmod"),
    TARGET_TEMPERATURE("hmax"),
    HEATING("hsta"),
    HEATERFOCUS("ffoc");

    private final String code;

    DysonStateEnum(String code){
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static DysonStateEnum fromCode(String code){
        for(DysonStateEnum dysonStateEnum : DysonStateEnum.values()){
            if(dysonStateEnum.getCode().equalsIgnoreCase(code)){
                return dysonStateEnum;
            }
        }

        return null;
    }
}
