package org.tschoerner.christian.devices.sensors;

public enum DysonSensorEnum {

    MESSAGE("msg"),
    TIME("time"),
    TEMPERATURE("tact"),
    HUMIDITY("hact"),
    DUST("pact"),
    VOLATIL_ORGANIC_COMPOUNDS("vact"),
    SLEEP_TIMER("sltm");

    private final String code;

    DysonSensorEnum(String code){
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static DysonSensorEnum fromCode(String code){
        for(DysonSensorEnum dysonSensorEnum : DysonSensorEnum.values()){
            if(dysonSensorEnum.getCode().equalsIgnoreCase(code)){
                return dysonSensorEnum;
            }
        }

        return null;
    }
}
