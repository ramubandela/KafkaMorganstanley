package com.paraport.kafkaapi.module.messaging.controller;

import java.util.List;
import javax. validation. Valid;
import org.springframework.beans.factory. annotation. Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind. annotation. PostMapping;
import org.springframework.web.bind. annotation. RequestBody;
import org.springframework.web.bind. annotation. RequestMapping;
import org.springframework.web.bind. annotation. RestController;
import com.paraport.kafkaapi.module.domain. DomainHelper;
import com.paraport.kafkaapi.module.messaging.model.ProducerRequest;
import com.paraport.kafkaapi.module.messaging.model.ProducerResponse;
import com. paraport.kafkaapi.module.messaging.service.BroadcastMessageService;
//import com.paraport.kafkaapi.swaggerdoc.BroadcastDoc;
//import lombok. extern.slf4j.slf4j;
//@slf4j
@RestController
@RequestMapping("/v1")
public class BroadcastController {



@Autowired
private BroadcastMessageService messageService;
@Autowired
private DomainHelper domainHelper;


@PostMapping("/broadcast")
public ResponseEntity<List<ProducerResponse>> produceMessage(@RequestBody @Valid ProducerRequest producerRequest) {

//tog.info("Producer Request: (), producerRequest: () and acks: {}", producerRequest);
String originalDomainName = domainHelper.getoriginalDomainName (producerRequest.getDomainName());
//Log.info("list of resolved domain name : ()", originalDomainName);
List<ProducerResponse> response=messageService. broadcastMessages (
originalDomainName,
producerRequest.getTopicName(),
producerRequest.getPartition (),
producerRequest.getKey(),
producerRequest.getMessage(),
producerRequest.getAcks());

HttpStatus status = response.parallelStream().anyMatch(resp -> resp.getError() != null)? HttpStatus. INTERNAL_SERVER_ERROR: HttpStatus.OK;
return ResponseEntity. status(status). body (response);

}

}