package org.tschoerner.christian;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.tschoerner.christian.methods.DysonDevice475;

import java.util.Timer;
import java.util.TimerTask;

public class DysonAPI {

    public static void main(String[] args) throws MqttException {
        final DysonDevice475 dyson = new DysonDevice475("NN2-EU-JEA3438A",
                "192.168.86.46",
                "1SFkfgdXRNv5NYmHgUDVnB/mV6nMmu5/3wQourisD8ti6AK2J/GS/erdvYowCJMztkx7WUAGbhVXh9tc12T77Q==");
        dyson.connect();
        dyson.turnOn();
        dyson.setFanLevel(3);



        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    dyson.disconnect();

                    System.out.println("Fanspeed: " + dyson.getState().getFanSpeedInt());
                    System.out.println("Temperature Kevlin: " + dyson.getSensor().getTemperatureKelvin());
                    System.out.println("Temperature Celsius: " + dyson.getSensor().getTemperatureCelsius());
                    System.out.println("Temperature Fahrenheit: " + dyson.getSensor().getTemperatureFahrenheit());
                } catch (MqttException e) {
                    e.printStackTrace();
                }

            }
        }, 1000*2);
    }
}
