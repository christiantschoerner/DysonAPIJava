package org.tschoerner.christian.methods;

import org.eclipse.paho.client.mqttv3.*;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class DysonDevice475 implements MqttCallback {

    private String COMMAND_TOPIC;
    private String STATUS_TOPIC;

    private String serialNumber;
    private String ip;
    private String password;
    private MqttClient client;

    private DysonState dysonState;
    private DysonSensor dysonSensor;

    public DysonDevice475(String serialNumber, String ip, String password){
        this.serialNumber = serialNumber;
        this.ip = ip;
        this.password = password;

        this.COMMAND_TOPIC = "475/" + this.serialNumber + "/command";
        this.STATUS_TOPIC = "475/" + this.serialNumber + "/status/current";

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if(!client.isConnected()){
                    this.cancel();
                    return;
                }
                try {
                    requestData();
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }
        }, 1000*5, 1000*5);
    }


    public void connect(int port) throws MqttException {
        client = new MqttClient("tcp://" + ip + ":" + port, MqttClient.generateClientId());

        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName(serialNumber);
        options.setPassword(password.toCharArray());

        client.connect(options);
        client.setCallback(this);
        client.subscribe(STATUS_TOPIC);

        requestData();
    }

    public void connect() throws MqttException {
        connect(1883);
    }

    public void disconnect(boolean force) throws MqttException {
        if(!client.isConnected()){
            return;
        }

        if(force){
            client.disconnectForcibly();
        }else{
            client.disconnect();
        }
    }

    public void disconnect() throws MqttException {
        disconnect(false);
    }

    public void turnOn() throws MqttException { //time does not matter | Will be automatically corrected by the fan
        setConfiguration("fnst", "FAN");
        setConfiguration("fmod", "FAN");
    }

    public void turnOff() throws MqttException {
        setConfiguration("fnst", "OFF");
        setConfiguration("fmod", "OFF");
    }

    public void setNightmode(boolean nightmode) throws MqttException {
        setConfiguration("nmod", nightmode ? "ON" : "OFF");
    }

    public void setOscillation(boolean oscillation) throws MqttException {
        setConfiguration("oson", oscillation ? "ON" : "OFF");
    }

    public void setFanLevel(Integer level) throws MqttException {
        String stringLevel = level.toString();
        if(level < 10){
            stringLevel = 0 + level.toString();
        }

        setConfiguration("fnsp", "00" + stringLevel);
    }

    public void setConfiguration(String key, String value) throws MqttException {
        String message = "{ \"data\": { \"" + key +"\": \"" + value + "\" }, \"mode-reason\": \"LAPP\", \"time\": \"2018-07-01T15:27:05Z\", \"msg\": \"STATE-SET\" }";
        sendMqttMessage(COMMAND_TOPIC, message);
    }

    public void requestData() throws MqttException {
        String message = "{ \"mode-reason\": \"LAPP\", \"time\": \"2018-07-01T14:41:06Z\", \"msg\": \"REQUEST-CURRENT-STATE\" }";
        sendMqttMessage(STATUS_TOPIC, message);

        message = "{ \"mode-reason\": \"LAPP\", \"time\": \"2018-07-01T14:41:06Z\", \"msg\": \"REQUEST-PRODUCT-ENVIRONMENT-CURRENT-SENSOR-DATA\" }";
        sendMqttMessage(COMMAND_TOPIC, message);
    }

    public void sendMqttMessage(String topic, String message) throws MqttException {
        byte[] bytes = message.getBytes();
        MqttMessage mqttMessage = new MqttMessage(bytes);

        client.publish(topic, mqttMessage);
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public String getIp() {
        return ip;
    }

    public String getPassword() {
        return password;
    }

    public DysonState getState() {
        return dysonState;
    }

    public DysonSensor getSensor(){
        return dysonSensor;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setState(DysonState dysonState) {
        this.dysonState = dysonState;
    }

    public void setSensor(DysonSensor dysonSensor){
        this.dysonSensor = dysonSensor;
    }

    /*

        This is the callback Listener. Every time the fan changes any state it will be called

     */

    public void connectionLost(Throwable throwable) {

    }

    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
        String message = new String(mqttMessage.getPayload());

        if(message.contains("STATE-CHANGE")){
            JSONObject jsonObject = new JSONObject(message);
            JSONObject state = jsonObject.getJSONObject("product-state");

            String msg = jsonObject.getString("msg");
            String time = jsonObject.getString("time");
            String modeReason = jsonObject.getString("mode-reason");
            String stateReason = jsonObject.getString("state-reason");

            String mode = String.valueOf(state.getJSONArray("fmod").get(1));
            String status = String.valueOf(state.getJSONArray("fnst").get(1));
            String fanSpeed = String.valueOf(state.getJSONArray("fnsp").get(1));
            String airQuality = String.valueOf(state.getJSONArray("qtar").get(1));
            Boolean oscillation = Boolean.valueOf(String.valueOf(state.getJSONArray("oson").get(1)));
            String filterLifeRemaining = String.valueOf(state.getJSONArray("filf").get(1));
            Boolean nightMode = Boolean.valueOf(String.valueOf(state.getJSONArray("nmod").get(1)));


            DysonState dysonState = new DysonState(msg, time, modeReason, stateReason, mode, status, fanSpeed, airQuality, oscillation, filterLifeRemaining, nightMode);
            this.dysonState = dysonState;
        }else if(message.contains("ENVIRONMENTAL-CURRENT-SENSOR-DATA")){
            JSONObject jsonObject = new JSONObject(message);
            JSONObject data = jsonObject.getJSONObject("data");

            String msg = jsonObject.getString("msg");
            String time = jsonObject.getString("time");

            Double temperature = Double.valueOf(Double.valueOf(data.getString("tact")) / 10);
            String humidity = data.getString("hact");
            String dust = data.getString("pact");
            String volatilOrganicCompounds = data.getString("vact");
            String sleepTimer = data.getString("sltm");

            DysonSensor dysonSensor = new DysonSensor(msg, time, temperature, humidity, dust, volatilOrganicCompounds, sleepTimer);
            this.dysonSensor = dysonSensor;
        }
    }

    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
    }
}
