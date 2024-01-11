package com.paraport.kafkaapi.module.messaging.model;



import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax. validation.constraints. NotNull;
import lombok. AllArgsConstructor;
import lombok. Builder;
import lombok. Data;
import lombok. NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProducerRequest {
@NotNull
private String topicName;
@NotNull(message = "Domain name is required")
private String domainName;
private Integer acks;
private String key;
@NotNull(message = "Message is required")
private Integer partition;
private Object message;

}
