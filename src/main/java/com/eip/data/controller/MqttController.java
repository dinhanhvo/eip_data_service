package com.eip.data.controller;

import com.eip.data.config.Mqtt;
import com.eip.data.model.MqttPublishModel;
import com.eip.data.service.ListenerService;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.mqttv5.client.IMqttAsyncClient;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping(value = "/api/mqtt")
public class MqttController {

    @Autowired
    ListenerService bridgerService;

    @PostMapping("cloud/pub")
    public ResponseEntity<?> cloudPublishMessage(@RequestBody @Valid MqttPublishModel messagePublishModel) throws MqttException {

        MqttMessage mqttMessage = new MqttMessage(messagePublishModel.getMessage().getBytes());
        mqttMessage.setQos(messagePublishModel.getQos());
        mqttMessage.setRetained(messagePublishModel.getRetained());

        Mqtt.getCloudInstance().publish(messagePublishModel.getTopic(), mqttMessage);
        return ResponseEntity.ok().body(messagePublishModel.getMessage());
    }

    @PostMapping("internal/pub")
    public ResponseEntity<?> internalPublishMessage(@RequestBody @Valid MqttPublishModel messagePublishModel) throws MqttException {

        MqttMessage mqttMessage = new MqttMessage(messagePublishModel.getMessage().getBytes());
        mqttMessage.setQos(messagePublishModel.getQos());
        mqttMessage.setRetained(messagePublishModel.getRetained());
        Mqtt.getInstanceInternal().publish(messagePublishModel.getTopic(), mqttMessage);
        return ResponseEntity.ok().body(messagePublishModel.getMessage());
    }

    @GetMapping("internal/sub")
    public boolean subscribeEIP(@RequestParam(value = "topic") String topic) throws MqttException {

        IMqttAsyncClient mqttClient = Mqtt.getInstanceInternal();
        log.info("--------------- clientID: {}, subscribed on topic {}", mqttClient.getClientId(), topic);

        Mqtt.controlSubscribe(mqttClient, topic, bridgerService);

        return true;
    }

    @GetMapping("cloud/sub")
    public boolean cloudSubscribeEIP(@RequestParam(value = "topic") String topic) throws MqttException {

        IMqttAsyncClient mqttClient = Mqtt.getCloudInstance();
        log.info("--------------- clientID: {}, subscribed on topic {}", mqttClient.getClientId(), topic);

        Mqtt.controlSubscribe(mqttClient, topic, bridgerService);
        return true;
    }

}
