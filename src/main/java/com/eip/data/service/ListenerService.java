package com.eip.data.service;

import com.eip.data.Constant;
import com.eip.data.config.Mqtt;
import com.eip.data.entity.CanMqttMessage;
import com.eip.data.entity.MilkCollect;
import com.eip.data.repositories.IMilkCollectRepository;
import com.eip.data.repositories.IMqttPublishModelRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.voda.eip.Converter;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.mqttv5.client.*;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.eclipse.paho.mqttv5.common.packet.MqttProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ListenerService implements IMqttMessageListener {

    @Autowired
    IMqttPublishModelRepository mqttPublishModelRepository;

//    @Autowired
//    MilkCollectService milkCollectService;

    @Autowired
    IMilkCollectRepository milkCollectRepository;

    @Bean
    public  void loadCloudClient() {
//        IMqttAsyncClient mqttClient = Mqtt.getInstanceIntenal();
//        log.info("--------------- clientID: {}", mqttClient.getClientId());

        IMqttAsyncClient mqttClientCloud = Mqtt.getCloudInstance();
        log.info("--------------- mqttClientCloud: {}", mqttClientCloud.getClientId());


    }

    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage)  {

        log.info("================== listen on: {}", topic);
//        CanMqttMessage canMqttMessage = new CanMqttMessage();
//        canMqttMessage.setMessage(new String(mqttMessage.getPayload()));
//        canMqttMessage.setQos(mqttMessage.getQos());
//        canMqttMessage.setTopic(topic);
//        mqttPublishModelRepository.save(canMqttMessage);

        log.info("================== saved: {}", mqttMessage);
        boolean failed = false;
        // response to local the message received
        try {
            log.info("================== received: {}", mqttMessage);
            MilkCollect milkCollect = null;
            if (topic.equalsIgnoreCase("test")) {
                milkCollect = Converter.messageToDTO(new String(mqttMessage.getPayload()));
            } else  {
                milkCollect = Converter.getObjectMapper().readValue(new String(mqttMessage.getPayload()), MilkCollect.class);
            }

            if (milkCollect != null) {
                milkCollect.setMqttStatus(Constant.COMPLETED);
                milkCollectRepository.save(milkCollect);
                String json = Converter.getObjectMapper().writeValueAsString(milkCollect);
                log.info("================== json: {} ", json);
                String resTopic = "response/"+milkCollect.getId() + "/" + topic;
                Mqtt.getCloudInstance().publish(resTopic, mqttMessage);

                log.info("================== published: {} on topic: {}", mqttMessage, resTopic);
            } else {
                log.info("================== milkCollect null");
            }
        }
        catch( MqttException me) {
            log.info("response failed reason "+me.getReasonCode());
            log.info("msg "+me.getMessage());
            log.info("loc "+me.getLocalizedMessage());
            log.info("cause "+me.getCause());
            log.info("excep "+me);
            log.error(me.getMessage());
            failed = true;
        } catch (JsonProcessingException e) {
            log.info("cause "+e.getCause());
            log.error(e.getMessage());
            failed = true;
            throw new RuntimeException(e);
        } finally {
            if (failed) {
                Mqtt.restart();
            }
        }
    }

//    @Override
//    public void disconnected(MqttDisconnectResponse mqttDisconnectResponse) {
//        log.info("---------------------- disconnected ---------------");
//    }
//
//    @Override
//    public void mqttErrorOccurred(MqttException e) {
//        log.info("---------------------- mqttErrorOccurred ---------------");
//    }
//
//    @Override
//    public void deliveryComplete(IMqttToken iMqttToken) {
//        log.info("---------------------- deliveryComplete ---------------");
//    }
//
//    @Override
//    public void connectComplete(boolean b, String s) {
//        log.info("---------------------- connectComplete ---------------");
//    }
//
//    @Override
//    public void authPacketArrived(int i, MqttProperties mqttProperties) {
//        log.info("---------------------- authPacketArrived ---------------");
//    }
}
