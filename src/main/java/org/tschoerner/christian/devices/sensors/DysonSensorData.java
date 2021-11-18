package org.tschoerner.christian.devices.sensors;

import java.util.HashMap;

public class DysonSensorData {
    
    private final HashMap<DysonSensorEnum, Object> sensorData;
    
    public DysonSensorData(HashMap<DysonSensorEnum, Object> sensorData){
        this.sensorData = sensorData;
    }

    public Object get(DysonSensorEnum field){
        if(!sensorData.containsKey(field)){
            throw new NullPointerException("This field is not available for the device type you specified");
        }

        return sensorData.get(field);
    }

    public String getString(DysonSensorEnum DysonSensorEnum){
        return get(DysonSensorEnum).toString();
    }

    public Boolean getBoolean(DysonSensorEnum DysonSensorEnum){
        Object object = get(DysonSensorEnum);
        if(object instanceof Boolean){
            return (Boolean) object;
        }

        String covertedString = object.toString();
        if(covertedString.equalsIgnoreCase("on") || covertedString.equalsIgnoreCase("off")){
            return Boolean.parseBoolean(covertedString);
        }

        throw new ClassCastException(String.format("The field %s cannot be casted to Boolean",
                DysonSensorEnum.name()));
    }

    public Integer getInt(DysonSensorEnum DysonSensorEnum){
        Object object = get(DysonSensorEnum);
        if(object instanceof Integer){
            return (Integer) object;
        }

        String convertedString = object.toString()
                .replaceAll("000", "")
                .replaceAll("00", "");
        if(convertedString.matches("-?\\d+(\\.\\d+)?")){ // is number
            return Integer.parseInt(convertedString);
        }

        throw new ClassCastException(String.format("The field %s cannot be casted to Integer",
                DysonSensorEnum.name()));
    }
}
