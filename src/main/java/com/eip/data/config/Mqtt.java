package com.eip.data.config;


import com.eip.data.service.ListenerService;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.mqttv5.client.IMqttAsyncClient;
import org.eclipse.paho.mqttv5.client.IMqttToken;
import org.eclipse.paho.mqttv5.client.MqttAsyncClient;
import org.eclipse.paho.mqttv5.client.MqttConnectionOptions;
import org.eclipse.paho.mqttv5.client.persist.MemoryPersistence;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.eclipse.paho.mqttv5.common.MqttSubscription;
import org.eclipse.paho.mqttv5.common.packet.MqttProperties;
import org.springframework.beans.factory.annotation.Value;

import java.util.Arrays;

@Slf4j
public class Mqtt {

//    @Value("${JAVA_HOME}")
//    private static String javaHome;
    private static final String MQTT_PUB_ID = "data-client-pub";
    private static final String MQTT_CLOUD_SERVER_ADDRES= "tcp://broker.emqx.io:1883";

    private static final String MQTT_SUB_ID = "data-client-sub";
    private static final String MQTT_LOCAL_SERVER_ADDRES= "tcp://localhost:1883";
    private static IMqttAsyncClient instanceInternal;

    private static IMqttAsyncClient cloudInstance;

    public static IMqttAsyncClient getInstanceInternal() {

        MemoryPersistence persistence = new MemoryPersistence();

        try {
            if (instanceInternal == null) {
                instanceInternal = new MqttAsyncClient(MQTT_LOCAL_SERVER_ADDRES, MQTT_SUB_ID, persistence);

                MqttConnectionOptions connOpts = new MqttConnectionOptions();
                connOpts.setCleanStart(false);
                instanceInternal.setCallback(new EipMqttCallback());
                log.info("Connecting to broker: " + MQTT_LOCAL_SERVER_ADDRES);
                IMqttToken token = instanceInternal.connect(connOpts);
                token.waitForCompletion();
                log.info("Connected");

            }
        } catch(MqttException me) {
            log.info("reason "+me.getReasonCode());
            log.info("msg "+me.getMessage());
            log.info("loc "+me.getLocalizedMessage());
            log.info("cause "+me.getCause());
            log.info("excep "+me);
            log.error(me.getMessage());
        }

        return instanceInternal;
    }

    public static IMqttAsyncClient getCloudInstance() {

        MemoryPersistence persistence = new MemoryPersistence();

        try {
            if (cloudInstance == null) {
                cloudInstance = new MqttAsyncClient(MQTT_CLOUD_SERVER_ADDRES, MQTT_PUB_ID, persistence);
                cloudInstance.setCallback(new EipMqttCallback());
                log.info("Connecting to broker: " + MQTT_CLOUD_SERVER_ADDRES);
//                log.info(System.getProperty("broker"));
//                System.getenv().forEach((e, v) -> {
//                    log.info(e + "=" + v);
//                });
                MqttConnectionOptions connOpts =createOptions();
                IMqttToken token = cloudInstance.connect(connOpts);
                token.waitForCompletion();

            }

        } catch(MqttException me) {
            log.info("reason "+me.getReasonCode());
            log.info("msg "+me.getMessage());
            log.info("loc "+me.getLocalizedMessage());
            log.info("cause "+me.getCause());
            log.info("MqttException: "+me);
        } catch (Exception e) {
            log.info("excep :"+e);
        }

        return cloudInstance;
    }

    public  static  MqttConnectionOptions createOptions() {
        MqttConnectionOptions connOpts = new MqttConnectionOptions();
        connOpts.setCleanStart(false);
        connOpts.setKeepAliveInterval(5);
        connOpts.setAutomaticReconnect(true);
        connOpts.setConnectionTimeout(30);
        return  connOpts;
    }

    public static void restart() {
        instanceInternal = null;
        cloudInstance = null;
        getCloudInstance();
        getInstanceInternal();
    }
    public static void controlPublish(IMqttAsyncClient mqttClient, String topic, MqttMessage mqttMessage) {
        try {
            log.info("================== received: {}", mqttMessage);
            mqttClient.publish(topic, mqttMessage);
            log.info("================== published: {}", mqttMessage);
        }
        catch( MqttException me) {
            log.info("response failed reason "+me.getReasonCode());
            log.info("msg "+me.getMessage());
            log.info("loc "+me.getLocalizedMessage());
            log.info("cause "+me.getCause());
            log.info("excep "+me);
            log.error(me.getMessage());
        }
    }

    public static void controlSubscribe(IMqttAsyncClient mqttClient, String topic, ListenerService bridgerService) throws MqttException {

        MqttProperties props = new MqttProperties();
        props.setSubscriptionIdentifiers(Arrays.asList(new Integer[] { 0 }));
        mqttClient.subscribe(new MqttSubscription(topic, 2), null, null, bridgerService, props);

    }

    public static void controlSubscribe(IMqttAsyncClient mqttClient, String topic ) throws MqttException {

        MqttProperties props = new MqttProperties();
        props.setSubscriptionIdentifiers(Arrays.asList(new Integer[] { 0 }));
        mqttClient.subscribe(topic, 2);

    }

    public static void controlUnSubscribe(IMqttAsyncClient mqttClient, String topic) throws MqttException {
        mqttClient.unsubscribe(topic);
    }


    private Mqtt() {

    }
}
