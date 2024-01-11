package com.paraport.kafkaapi.module.messaging.controller;

import java.util.Arrays;
import java.util.List;
import javax. validation. Valid;
import org.springframework.beans.factory. annotation. Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind. annotation. PostMapping;
import org.springframework.web.bind. annotation. RequestBody;
import org.springframework.web.bind. annotation. RequestMapping;
import org.springframework.web.bind. annotation. RestController;


import com. paraport.kafkaapi.module.domain.DomainHelper;
import com. paraport.kafkaapi.module.messaging.model.ProducerRequest;
import com.paraport.kafkaapi.module.messaging.model. ProducerResponse;
import com. paraport.kafkaapi.module.messaging. service. ProduceMessageService;
//import com.paraport.kafkaapi.swaggerdoc. ProducerDoc;
//import lombok. extern.slf4j.slf4j;
//@slf4j
@RestController
@RequestMapping("/v1")
public class ProducerController {

	
		@Autowired
		private ProduceMessageService produceMessageService;
		@Autowired
		private DomainHelper domainHelper;
		//@ProducerDoc
		@PostMapping("/producer")
		public ResponseEntity<ProducerResponse> produceMessage(
		@RequestBody @Valid ProducerRequest producerRequest) {
		//tog.info("Producer Request producer Request: {}", producerRequest);
		String originalDomainName = domainHelper.getoriginalDomainName (producerRequest.getDomainName());
		//tog.info("domain name: {}", originalDomainName);
		List<ProducerResponse> response = produceMessageService.produceMessage(
		Arrays.asList (originalDomainName),
		producerRequest.getTopicName(),
		producerRequest.getPartition (),
		producerRequest.getKey(),
		producerRequest.getMessage(),
		producerRequest.getAcks());
		return ResponseEntity.status(HttpStatus.OK). body (response.get(0));

}

		
}