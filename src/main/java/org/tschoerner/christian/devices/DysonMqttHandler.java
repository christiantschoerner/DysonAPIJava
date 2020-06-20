package org.tschoerner.christian.devices;

import org.eclipse.paho.client.mqttv3.*;
import org.json.JSONObject;
import org.tschoerner.christian.devices.sensors.DysonSensorData;
import org.tschoerner.christian.devices.sensors.DysonSensorEnum;
import org.tschoerner.christian.devices.states.DysonState;
import org.tschoerner.christian.devices.states.DysonStateEnum;

import java.util.HashMap;
import java.util.function.Consumer;

public class DysonMqttHandler implements MqttCallback {

    private final DysonDeviceType dysonDeviceType;
    private final Consumer<DysonState> stateCallback;
    private final Consumer<DysonSensorData> sensorCallback;

    private final MqttClient client;

    public DysonMqttHandler(MqttClient client, DysonDeviceType dysonDeviceType,
                            Consumer<DysonState> stateCallback, Consumer<DysonSensorData> sensorCallback){
        this.client = client;

        this.dysonDeviceType = dysonDeviceType;
        this.stateCallback = stateCallback;
        this.sensorCallback = sensorCallback;
    }

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws MqttException {
        try {
            String message = new String(mqttMessage.getPayload());

            if(message.contains("CURRENT-STATE")){
                JSONObject jsonObject = new JSONObject(message);
                JSONObject state = jsonObject.getJSONObject("product-state");

                HashMap<DysonStateEnum, Object> hashMap = new HashMap<>();
                for(DysonStateEnum field : this.dysonDeviceType.getFields()){
                    String code = field.getCode();

                    if(state.has(code)){
                        hashMap.put(field, state.get(code));
                    }else if(jsonObject.has(code)){
                        hashMap.put(field, jsonObject.get(code));
                    }
                }

                DysonState dysonState = new DysonState(hashMap);
                if(this.stateCallback != null){
                    this.stateCallback.accept(dysonState);
                }
            }else if(message.contains("ENVIRONMENTAL-CURRENT-SENSOR-DATA")){
                JSONObject jsonObject = new JSONObject(message);
                JSONObject data = jsonObject.getJSONObject("data");

                HashMap<DysonSensorEnum, Object> hashMap = new HashMap<>();
                for(DysonSensorEnum field : DysonSensorEnum.values()){
                    String code = field.getCode();

                    if(data.has(code)){
                        hashMap.put(field, data.get(code));
                    }else if(jsonObject.has(code)){
                        hashMap.put(field, hashMap.get(code));
                    }
                }

                DysonSensorData dysonSensorData = new DysonSensorData(hashMap);
                if(this.sensorCallback != null){
                    this.sensorCallback.accept(dysonSensorData);
                }
            }
            if(client.isConnected()){
                client.disconnect();
            }
        }catch (Exception e){
            if(client.isConnected()){
                client.disconnect();
            }
        }

    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }

    @Override
    public void connectionLost(Throwable throwable) {

    }
}
