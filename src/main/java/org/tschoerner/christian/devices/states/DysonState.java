package org.tschoerner.christian.devices.states;

import java.util.HashMap;

public class DysonState {

    private final HashMap<DysonStateEnum, Object> state;

    public DysonState(HashMap<DysonStateEnum, Object> state){
        this.state = state;
    }

    public Object get(DysonStateEnum field){
        if(!state.containsKey(field)){
            throw new NullPointerException("This field is not available of the device type you specified");
        }

        return state.get(field);
    }

    public String getString(DysonStateEnum dysonStateEnum){
        return get(dysonStateEnum).toString();
    }

    public Boolean getBoolean(DysonStateEnum dysonStateEnum){
        Object object = get(dysonStateEnum);
        if(object instanceof Boolean){
            return (Boolean) object;
        }

        String covertedString = object.toString();
        if(covertedString.equalsIgnoreCase("on") || covertedString.equalsIgnoreCase("off")){
            return Boolean.parseBoolean(covertedString);
        }

        throw new ClassCastException(String.format("The field %s cannot be casted to Boolean",
                dysonStateEnum.name()));
    }

    public Integer getInt(DysonStateEnum dysonStateEnum){
        Object object = get(dysonStateEnum);
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
                dysonStateEnum.name()));
    }
}
