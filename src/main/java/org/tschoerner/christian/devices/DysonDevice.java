package org.tschoerner.christian.devices;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONObject;
import org.tschoerner.christian.devices.sensors.DysonSensorData;
import org.tschoerner.christian.devices.states.DysonState;
import org.tschoerner.christian.devices.states.DysonStateEnum;

import java.util.function.Consumer;

public class DysonDevice {

    private final int DEFAULT_PORT = 1883;

    private final String COMMAND_TOPIC;
    private final String STATUS_TOPIC;

    private final DysonDeviceType dysonDeviceType;
    private final String ip;
    private final String password;
    private final String serialNumber;


    public DysonDevice(DysonDeviceType dysonDeviceType, String ip, String password, String serialNumber){
        this.dysonDeviceType = dysonDeviceType;
        this.ip = ip;
        this.password = password;
        this.serialNumber = serialNumber;

        this.COMMAND_TOPIC = String.format("%s/%s/command", dysonDeviceType.getCode(), serialNumber);
        this.STATUS_TOPIC = String.format("%s/%s/status/current", dysonDeviceType.getCode(), serialNumber);
    }

    public void setField(DysonStateEnum field, Object object) throws MqttException {
        if(!this.dysonDeviceType.getFields().contains(field)){
            throw new IllegalArgumentException(String.format("The field %s is not valid for the device type %s",
                    field.name(), this.dysonDeviceType.name()));
        }

        String payload = generateCommandBody(field.getCode(), object)
                .toString();

        MqttClient mqttClient = getMqttClient();
        MqttMessage mqttMessage = new MqttMessage(payload.getBytes());

        mqttClient.publish(COMMAND_TOPIC, mqttMessage);
        mqttClient.disconnect();
    }

    public MqttClient getMqttClient() throws MqttException {
        MqttClient client = new MqttClient(String.format("tcp://%s:%s", ip, DEFAULT_PORT),
                MqttClient.generateClientId());

        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName(serialNumber);
        options.setPassword(password.toCharArray());

        client.connect(options);

        return client;
    }

    public void getState(Consumer<DysonState> callback) throws MqttException {
        MqttClient client = getMqttClient();
        client.setCallback(new DysonMqttHandler(client, this.dysonDeviceType, callback, null));
        client.subscribe(STATUS_TOPIC);

        String payload = generateRequestBody(DysonRequestType.STATE)
                .toString();
        MqttMessage mqttMessage = new MqttMessage(payload.getBytes());

        client.publish(COMMAND_TOPIC, mqttMessage);
    }

    public void getSensorData(Consumer<DysonSensorData> callback) throws MqttException {
        MqttClient client = getMqttClient();
        client.setCallback(new DysonMqttHandler(client, this.dysonDeviceType, null, callback));
        client.subscribe(STATUS_TOPIC);

        String payload = generateRequestBody(DysonRequestType.SENSORDATA)
                .toString();
        MqttMessage mqttMessage = new MqttMessage(payload.getBytes());

        client.publish(COMMAND_TOPIC, mqttMessage);
    }

    public DysonDeviceType getDysonDeviceType() {
        return dysonDeviceType;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    private JSONObject generateCommandBody(String key, Object value){
        return new JSONObject()
                .put("mode-reason", "LAPP")
                .put("time", "2018-07-01T14:41:06Z")
                .put("msg", "STATE-SET")
                .put("data", new JSONObject()
                        .put(key, value));
    }

    private JSONObject generateRequestBody(DysonRequestType dysonRequestType){
        return new JSONObject()
                .put("mode-reason", "LAPP")
                .put("time", "2018-07-01T14:41:06Z")
                .put("msg", dysonRequestType.getQuery());
    }
}
