package com.eip.data.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MqttSubscribeModel {

    private String message;
    private Integer qos;
    private Integer id;

}
