package org.tschoerner.christian;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.tschoerner.christian.methods.DysonDevice475;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class DysonAPI {

    public static void main(String[] args) throws MqttException {
        final DysonDevice475 dyson = new DysonDevice475("XXX-XX-XXXXXXXX",
                "1.1.1.1",
                "ABCDEFGHIJKLMNOPQRTUVWXYZ");
        dyson.connect();
        dyson.turnOn();
        dyson.setFanLevel(3);
        dyson.setOscillation(true);
        dyson.setNightmode(false);


        Executors.newScheduledThreadPool(5).schedule(new Runnable() {
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
        }, 1, TimeUnit.SECONDS);
    }
}
