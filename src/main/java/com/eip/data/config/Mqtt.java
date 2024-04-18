package com.eip.data.config;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;

public class Mqtt {

    private static final String MQTT_PUBLISHER_ID = "spring-server";
    private static final String MQTT_SERVER_ADDRES= "tcp://broker.emqx.io:1883";
    private static IMqttClient instance;

    public static IMqttClient getInstance() {
        return getInstance(MQTT_SERVER_ADDRES, MQTT_PUBLISHER_ID);
    }
    public static IMqttClient getInstance(String clientID, String brokerURL) {
        try {
            if (instance == null) {
                instance = new MqttClient(clientID, brokerURL);
            }

            MqttConnectOptions options = new MqttConnectOptions();
            options.setAutomaticReconnect(true);
            options.setCleanSession(true);
            options.setConnectionTimeout(10);

            if (!instance.isConnected()) {
                instance.connect(options);
            }
        } catch (MqttException e) {
            e.printStackTrace();
        }

        return instance;
    }

    private Mqtt() {

    }
}
