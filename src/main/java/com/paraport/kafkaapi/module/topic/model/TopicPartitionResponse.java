package com.paraport.kafkaapi.module.topic.model;


import java.util.List;

import lombok. AllArgsConstructor;
import lombok.Data;
import lombok. NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TopicPartitionResponse {

private Integer partition;
private Integer leader;
private List<TopicReplicaResponse> replicas;
}