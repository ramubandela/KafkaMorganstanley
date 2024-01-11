package com.paraport.kafkaapi.module.topic.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TopicDetails {
String topicName;
Integer partitions;
}
