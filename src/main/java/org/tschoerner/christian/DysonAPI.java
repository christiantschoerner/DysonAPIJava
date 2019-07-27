package org.tschoerner.christian;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.tschoerner.christian.methods.DysonDevice475;

public class DysonAPI {

    public static void main(String[] args) throws MqttException {
        if(true){
            return;
        }

        final DysonDevice475 dyson = new DysonDevice475("XXX-XX-XXXXXXXX",
                "1.1.1.1",
                "ABCDEFGHIJKLMNOPQRTUVWXYZ");
        dyson.connect();
        dyson.turnOn();
        dyson.setFanLevel(3);
        dyson.setOscillation(true);
        dyson.setNightmode(false);
        dyson.disconnect();
    }
}
