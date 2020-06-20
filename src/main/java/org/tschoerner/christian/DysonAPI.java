package org.tschoerner.christian;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.tschoerner.christian.devices.DysonDevice;
import org.tschoerner.christian.devices.DysonDeviceType;
import org.tschoerner.christian.devices.fields.*;
import org.tschoerner.christian.devices.sensors.DysonSensorEnum;
import org.tschoerner.christian.devices.states.DysonStateEnum;

public class DysonAPI {

    /**
     *
     * DysonAPI by Christian Tschörner
     * Example how to use the Library
     *
     */

    private static final String DYSON_IP = "192.168.86.XXX";
    private static final String DYSON_SERIALNUMBER = "NN2-EU-XXXXXXXX";
    private static final String DYSON_PASSWORD = "1SFkfgdXRXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX";

    public static void main(String[] args) throws MqttException {
        DysonDevice dysonDevice = new DysonDevice(DysonDeviceType.PURECOOLLINK, DYSON_IP,
                DYSON_PASSWORD,
                DYSON_SERIALNUMBER);

        dysonDevice.setField(DysonStateEnum.MODE, ModeField.ON);
        dysonDevice.setField(DysonStateEnum.STATUS, StatusField.ON); // not needed
        dysonDevice.setField(DysonStateEnum.FANSPEED, FanSpeedField.SPEED2);
        dysonDevice.setField(DysonStateEnum.NIGHTMODE, NightModeField.OFF);
        dysonDevice.setField(DysonStateEnum.OSCILLATION, OscillationField.ON);

        dysonDevice.getState(dysonState -> {
            int fanSpeed = dysonState.getInt(DysonStateEnum.FANSPEED);
            boolean nightMode = dysonState.getBoolean(DysonStateEnum.NIGHTMODE);

            System.out.println("FanSpeed: " + fanSpeed);
            System.out.println("NightMode: " + nightMode);
        });

        dysonDevice.getSensorData(dysonSensorData -> {
            Object temperature = dysonSensorData.get(DysonSensorEnum.TEMPERATURE);

            System.out.println("Temperature: " + temperature.toString());
        });

    }
}
