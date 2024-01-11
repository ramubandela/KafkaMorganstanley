package com.paraport.kafkaapi.module.topic.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class DescribeTopicResponse {
private String topicName;
private Boolean internalTopic;
private Integer partitionSize;
private Integer replicaSize;
private List<TopicPartitionResponse> partitions;
}