package com.eip.data.model;

import com.eip.data.entity.MilkCollect;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MqttPublishModel {

    @NotNull
    @Size(min = 1,max = 255)
    private String topic;


    @NotNull
    private Boolean retained;

    @NotNull
    private Integer qos;

    @NotNull
    @Size(min = 1,max = 255)
    private MilkCollect message;

}
