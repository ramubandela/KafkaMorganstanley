package com.paraport.kafkaapi.module.topic.model;

import lombok. AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CreateTopicRequest {
private String domain;
private String topic;
private Integer partitions;
private Short replicas;

}
