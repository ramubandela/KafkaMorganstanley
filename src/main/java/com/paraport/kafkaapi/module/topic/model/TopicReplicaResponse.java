package com.paraport.kafkaapi.module.topic.model;


import lombok. AllArgsConstructor;
import lombok.Data;
import lombok. NoArgsConstructor;
import lombok. ToString;
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TopicReplicaResponse {
private String broker;
private Integer port;
private Boolean leader;


}
