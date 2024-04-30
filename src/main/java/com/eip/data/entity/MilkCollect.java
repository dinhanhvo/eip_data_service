package com.eip.data.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

/**
 *
 * SELECT TOP (10)
 *        [id]
 *       ,[Serial_Weigher]
 *       ,[Code_Seller]
 *       ,[Name_Seller]
 *       ,[Code_Tank_Seller]
 *       ,[Tank_Tare_Weight] real
 *       ,[Tank_Gross_Weight] real
 *       ,[Tank_Net_Weight] real
 *       ,[Mqtt_Status]
 *       ,[created]
 *   FROM [CNHS_CheckWeigher].[dbo].[milk_collect]
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "milk_collect")
public class MilkCollect {
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @Column(name = "msg_id")
//    private  String msgId;

    @Column(name = "Serial_Weigher")
    private  String serialWeigher;

    @Column(name = "Code_Seller")
    private String codeSeller;

    @Column(name = "Name_Seller")
    private String nameSeller;

    @Column(name = "Code_Tank_Seller")
    private String codeTankSeller;

    @Column(name = "Tank_Tare_Weight")
    private double tankTareWeight;

    @Column(name = "Tank_Gross_Weight")
    private double tankGrossWeight;

    @Column(name = "Tank_Net_Weight")
    private double tankNetWeght;

    @Column(name = "Mqtt_Status")
    private String mqttStatus;

    @Column(name = "createdAt")
    private ZonedDateTime createdAt;

}
