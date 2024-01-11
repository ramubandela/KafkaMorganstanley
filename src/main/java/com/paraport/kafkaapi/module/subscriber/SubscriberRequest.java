package com.paraport.kafkaapi.module.subscriber;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubscriberRequest {
	
	String topic;
	String producerDomain;
	String subscriberDomain;

}
