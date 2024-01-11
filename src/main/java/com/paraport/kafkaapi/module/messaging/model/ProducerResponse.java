package com.paraport.kafkaapi.module.messaging.model;

import com. fasterxml.jackson. annotation.JsonInclude;
import com. paraport.kafkaapi.core.model. Error;
import lombok. AllArgsConstructor;
import lombok. Builder;
import lombok. Data;
import lombok. NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude. Include.NON_NULL)
public class ProducerResponse {
private String domain;
private String topic;
private Object key;
private Integer partition;
private Long offset;
private Error error;

}
