package com.eip.data.config;

import com.eip.data.Constant;
import com.eip.data.entity.MilkCollect;
import com.eip.data.repositories.IMilkCollectRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.voda.eip.Converter;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.mqttv5.client.IMqttDeliveryToken;
import org.eclipse.paho.mqttv5.client.IMqttToken;
import org.eclipse.paho.mqttv5.client.MqttCallback;
import org.eclipse.paho.mqttv5.client.MqttDisconnectResponse;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.eclipse.paho.mqttv5.common.packet.MqttProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EipMqttCallback implements MqttCallback {

    @Autowired
    IMilkCollectRepository milkCollectRepository;

    @Override
    public void disconnected(MqttDisconnectResponse mqttDisconnectResponse) {
        log.info("---------------------- disconnected ---------------");
    }

    @Override
    public void mqttErrorOccurred(MqttException e) {
        log.info("---------------------- mqttErrorOccurred ---------------");
    }

    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
//        System.out.println("topic: " + topic);
//        System.out.println("qos: " + message.getQos());
//        System.out.println("message content: " + new String(message.getPayload()));
        log.info("================== listen on: {}", topic);
        log.info("---------------------- messageArrived ---------------");
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
            if (topic.equalsIgnoreCase("ThuMuaSua")) {
                milkCollect = Converter.getObjectMapper().readValue(new String(mqttMessage.getPayload()), MilkCollect.class);
            } else {
                milkCollect = Converter.messageToDTO(new String(mqttMessage.getPayload()));
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
                log.info(" >>>>>>>>>>>>>>>>> Can not parse the msg <<<<<<<<<<<<<<< {}", mqttMessage);
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

    @Override
    public void deliveryComplete(IMqttToken iMqttToken) {
        log.info("---------------------- deliveryComplete ---------------");
    }

    @Override
    public void connectComplete(boolean b, String s) {
        log.info("---------------------- connectComplete ---------------");
    }

    @Override
    public void authPacketArrived(int i, MqttProperties mqttProperties) {
        log.info("---------------------- authPacketArrived ---------------");
    }

}
