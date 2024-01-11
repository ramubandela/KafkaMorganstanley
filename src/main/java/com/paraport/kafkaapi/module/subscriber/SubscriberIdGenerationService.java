package com.paraport.kafkaapi.module.subscriber;

import org.springframework.stereotype.Service;

@Service
public class SubscriberIdGenerationService {
	
	public Long newAccountId() {
		return System.nanoTime();
	}

}
