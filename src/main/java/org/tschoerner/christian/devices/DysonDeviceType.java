package org.tschoerner.christian.devices;

import org.tschoerner.christian.devices.states.DysonStateEnum;

import java.util.Arrays;
import java.util.List;

public enum DysonDeviceType {

    PURECOOLLINK(475, Arrays.asList(DysonStateEnum.MESSAGE, DysonStateEnum.TIME, DysonStateEnum.MODE_REASON, DysonStateEnum.STATE_REASON, DysonStateEnum.MODE, DysonStateEnum.STATUS, DysonStateEnum.FANSPEED, DysonStateEnum.AIRQUALITY, DysonStateEnum.OSCILLATION, DysonStateEnum.FILTER_LIFE_REMAINING, DysonStateEnum.NIGHTMODE)),
    PUREHOTCOOLLINK(455, Arrays.asList(DysonStateEnum.MESSAGE, DysonStateEnum.TIME, DysonStateEnum.MODE_REASON, DysonStateEnum.STATE_REASON, DysonStateEnum.MODE, DysonStateEnum.STATUS, DysonStateEnum.FANSPEED, DysonStateEnum.AIRQUALITY, DysonStateEnum.OSCILLATION, DysonStateEnum.FILTER_LIFE_REMAINING, DysonStateEnum.NIGHTMODE, DysonStateEnum.CONTINUOUS_MEASUREMENT, DysonStateEnum.HEATERMODE, DysonStateEnum.TARGET_TEMPERATURE, DysonStateEnum.HEATING, DysonStateEnum.HEATERFOCUS));

    private final List<DysonStateEnum> fields;
    private final int code;

    DysonDeviceType(int code, List<DysonStateEnum> fields){
        this.code = code;
        this.fields = fields;
    }

    public List<DysonStateEnum> getFields() {
        return fields;
    }

    public int getCode() {
        return code;
    }
}
