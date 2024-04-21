package com.eip.data.bean;

import com.eip.data.config.Mqtt;
import com.eip.data.entity.MilkCollect;
import com.eip.data.service.ListenerService;
import com.eip.data.service.MilkCollectService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.voda.eip.Converter;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@Slf4j
public class Cronjob {

    @Autowired
    MilkCollectService milkCollectService;

    @Autowired
    ListenerService listenerService;

//    @Scheduled(fixedRate = 10000)
    public void scheduleTaskWithFixedRate() {
        List<MilkCollect> milkCollects = milkCollectService.getMilkCollectsByStatus("Processing");
        log.info("-------- cronjob running ----- num uncompleted msg {}", milkCollects.size());
        milkCollects.forEach(msg -> {
            try {
                String sTopic = "response/" +msg.getId()

                        + "/ThuMuaSua";
                Mqtt.controlSubscribe(Mqtt.getCloudInstance(), sTopic, listenerService);
                log.info("----------- subscribed on {}", sTopic);

                MqttMessage mqttMessage = new MqttMessage();
                String json = Converter.getObjectMapper().writeValueAsString(msg);
                mqttMessage.setPayload(json.getBytes(StandardCharsets.UTF_8));
                Mqtt.controlPublish(Mqtt.getCloudInstance(), "ThuMuaSua", mqttMessage);
                log.info("--------- published {} to cloud", msg);
            } catch (JsonProcessingException | MqttException e) {
                log.error(e.getMessage());
                throw new RuntimeException(e);
            }

        });
    }

    public void scheduleTaskWithFixedDelay() {
    }

    public void scheduleTaskWithInitialDelay() {
    }

    public void scheduleTaskWithCronExpression() {
    }
}
