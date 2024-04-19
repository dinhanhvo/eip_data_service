package com.eip.data.service;

//import com.eip.data.repositories.IMqttPublishModelRepository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SubcribeService {

//    @Autowired
//    IMqttPublishModelRepository mqttPublishModelRepository;

    @Value("${can.topic}")
    String topic;

//    @Bean
//    public  void saveMsg() throws MqttException {
//        IMqttClient mqttClient = Mqtt.getInstance();
//        log.info("--------------- clientID: {}", mqttClient.getClientId());
//        log.info("================== listen on: {}", topic);
//        mqttClient.subscribeWithResponse(topic, (s, mqttMessage) -> {
//            MqttSubscribeModel mqttSubscribeModel = new MqttSubscribeModel();
//            mqttSubscribeModel.setId(mqttMessage.getId());
//            mqttSubscribeModel.setMessage(new String(mqttMessage.getPayload()));
//            mqttSubscribeModel.setQos(mqttMessage.getQos());
//
//            CanMqttMessage canMqttMessage = new CanMqttMessage();
//            canMqttMessage.setMessage(new String(mqttMessage.getPayload()));
//            canMqttMessage.setQos(mqttMessage.getQos());
//            canMqttMessage.setTopic(topic);
//            log.info("================== save msg to db: {}", canMqttMessage.getMessage());
//            mqttPublishModelRepository.save(canMqttMessage);
//        });
//    }
}
