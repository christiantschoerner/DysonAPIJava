package org.tschoerner.christian.methods;

import org.eclipse.paho.client.mqttv3.*;
import org.json.JSONObject;
import org.tschoerner.christian.interfaces.DysonSensorCallback;
import org.tschoerner.christian.interfaces.DysonStateCallback455;

public class DysonDevice455 implements MqttCallback {

    private String COMMAND_TOPIC;
    private String STATUS_TOPIC;

    private String serialNumber;
    private String ip;
    private String password;
    private MqttClient client;

    private DysonSensor dysonSensor;
    private DysonSensorCallback sensorCallback;

    private DysonState455 dysonState;
    private DysonStateCallback455 stateCallback;

    public DysonDevice455(String serialNumber, String ip, String password){
        this.serialNumber = serialNumber;
        this.ip = ip;
        this.password = password;

        this.COMMAND_TOPIC = "455/" + this.serialNumber + "/command";
        this.STATUS_TOPIC = "455/" + this.serialNumber + "/status/current";
    }

    public void connect(int port) throws MqttException {
        if(client != null){
            if(client.isConnected()){
                return;
            }
        }
        client = new MqttClient("tcp://" + ip + ":" + port, MqttClient.generateClientId());

        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName(serialNumber);
        options.setPassword(password.toCharArray());

        client.connect(options);
        client.setCallback(this);
        client.subscribe(STATUS_TOPIC);
    }

    public void connect() throws MqttException {
        connect(1883);
    }

    public void turnOff() throws MqttException {
        setConfiguration("fnst", "OFF");
        setConfiguration("fmod", "OFF");
    }

    public void turnOn() throws MqttException {
        setConfiguration("fnst", "FAN");
        setConfiguration("fmod", "FAN");
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

    public void setMode(DysonMode455 mode) throws MqttException {
        if(mode == DysonMode455.COOL){
            setConfiguration("fnst", "FAN");
            setConfiguration("fmod", "FAN");
            setConfiguration("hmod", "OFF");
        }else{
            setConfiguration("fnst", "HEAT");
            setConfiguration("fmod", "HEAT");
            setConfiguration("hmod", "HEAT");
        }
    }

    public void setContinuousMesurement(boolean continuousMesurement){

    }

    public void setHeaterFocus(boolean heaterFocus) throws MqttException {
        setConfiguration("ffoc", heaterFocus ? "ON" : "OFF");
    }

    public void setTargetTemperature(Integer degrees) throws MqttException {
        if(degrees < 1 || degrees > 37){
            throw new IndexOutOfBoundsException("Target temperature must be between 1-37 degrees celsius");
        }

        Integer targetTemperature = 2730 + (degrees * 10);
        setConfiguration("hmax", targetTemperature.toString());
    }

    /*

        REQUESTING AND CONFIGURATION

     */

    public void requestState(DysonStateCallback455 callback) throws MqttException {
        this.stateCallback = callback;
        String message = "{ \"mode-reason\": \"LAPP\", \"time\": \"2018-07-01T14:41:06Z\", \"msg\": \"REQUEST-CURRENT-STATE\" }";
        sendMqttMessage(STATUS_TOPIC, message);
    }

    public void requestSensorData(DysonSensorCallback callback) throws MqttException {
        this.sensorCallback = callback;
        String message = "{ \"mode-reason\": \"LAPP\", \"time\": \"2018-07-01T14:41:06Z\", \"msg\": \"REQUEST-PRODUCT-ENVIRONMENT-CURRENT-SENSOR-DATA\" }";
        sendMqttMessage(COMMAND_TOPIC, message);
    }


    public void setConfiguration(String key, String value) throws MqttException {
        String message = "{ \"data\": { \"" + key +"\": \"" + value + "\" }, \"mode-reason\": \"LAPP\", \"time\": \"2018-07-01T15:27:05Z\", \"msg\": \"STATE-SET\" }";
        sendMqttMessage(COMMAND_TOPIC, message);
    }

    public void sendMqttMessage(String topic, String message) throws MqttException {
        byte[] bytes = message.getBytes();
        MqttMessage mqttMessage = new MqttMessage(bytes);

        client.publish(topic, mqttMessage);
    }


    public void disconnect(final boolean force) {
        new Runnable() {
            public void run() {
                if(!client.isConnected()){
                    return;
                }

                try {
                    if(force){
                        client.disconnectForcibly();
                    }else{
                        client.disconnect();
                    }
                }catch (MqttException e){
                    e.printStackTrace();
                }

            }
        };

    }

    public void disconnect() {
        disconnect(false);
    }

    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
        String message = new String(mqttMessage.getPayload());

        System.out.println(message);
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
            boolean oscillation = Boolean.parseBoolean(String.valueOf(state.getJSONArray("oson").get(1)));
            boolean continuousMesurement = Boolean.parseBoolean(String.valueOf(state.getJSONArray("rhtm").get(1)));
            String filterLifeRemaining = String.valueOf(state.getJSONArray("filf").get(1));
            boolean nightMode = Boolean.parseBoolean(String.valueOf(state.getJSONArray("nmod").get(1)));
            String heaterMode = String.valueOf(state.getJSONArray("hmod").get(1));
            String targetTemperature = String.valueOf(state.getJSONArray("hmax").get(1));
            boolean heating = Boolean.parseBoolean(String.valueOf(state.getJSONArray("hsta").get(1)));
            boolean heaterFocus = Boolean.parseBoolean(String.valueOf(state.getJSONArray("ffoc").get(1)));

            DysonState455 dysonState = new DysonState455(message, time, modeReason, stateReason,
                    mode, status, fanSpeed, airQuality, oscillation, continuousMesurement, filterLifeRemaining,
                    nightMode, heaterMode, targetTemperature, heating, heaterFocus);


            this.dysonState = dysonState;
            if(this.stateCallback != null){
                this.stateCallback.onStateReceived(this.dysonState);
            }
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
            if(this.sensorCallback != null){
                this.sensorCallback.onSensorDataReceived(this.dysonSensor);
            }
        }
    }

    public void connectionLost(Throwable throwable) {

    }

    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }

    public enum DysonMode455 {
        COOL,
        HEAT;
    }
}
