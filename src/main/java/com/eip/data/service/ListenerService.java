package com.eip.data.service;

import com.eip.data.config.Mqtt;
import com.eip.data.entity.CanMqttMessage;
import com.eip.data.repositories.IMqttPublishModelRepository;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ListenerService implements IMqttMessageListener {

    @Autowired
    IMqttPublishModelRepository mqttPublishModelRepository;

    @Bean
    public  void loadCloudClient() throws MqttException {
        IMqttClient mqttClient = Mqtt.getInstance();
        log.info("--------------- clientID: {}", mqttClient.getClientId());

    }

    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) {

        log.info("================== listen on: {}", topic);
        CanMqttMessage canMqttMessage = new CanMqttMessage();
        canMqttMessage.setMessage(new String(mqttMessage.getPayload()));
        canMqttMessage.setQos(mqttMessage.getQos());
        canMqttMessage.setTopic(topic);
        mqttPublishModelRepository.save(canMqttMessage);
        log.info("================== saved {} to db", canMqttMessage.getMessage());

    }
}
