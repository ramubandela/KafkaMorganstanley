package com.paraport.kafkaapi.module.subscriber;

import java.time. LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation. Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind. annotation.GetMapping;
import org.springframework.web.bind.annotation. PostMapping;
import org.springframework.web.bind.annotation. RequestBody;
import org.springframework.web.bind.annotation. RequestMapping;



import org.springframework.web.bind. annotation. RestController;
import com.paraport.kafkaapi.core.model. APIResponse;
import com.paraport.kafkaapi.kafkaapi.swagger.ListSubscribersDoc;
import com. paraport.kafkaapi.module.common.jpa.entity. Subscriber;
import com. paraport.kafkaapi.module.domain. DomainHelper;
//import com.paraport.kafkaapi.swaggerdoc. ListSubscribersDoc;
//import com.paraport.kafkaapi. swaggerdoc. SubscriberDoc;
//import lombok.extern.slf4j.slf4j;
//@51f4=]
@RestController
@RequestMapping("/v1")
public class SubscriberApiController {
	
	@Autowired
	SubscriberService subscriberService;
	@Autowired
	SubscriberIdGenerationService subscriberIdGenerationService;
	@Autowired
	DomainHelper domainHelper;
	
	//@SubscriberDoc
	@PostMapping("/subscription")
	public ResponseEntity<APIResponse> subscribe(@RequestBody SubscriberRequest request) {
	//Log info("SubscriberApiController Subscriber Request: {}", request);
	String producerDomain = domainHelper.getoriginalDomainName (request.getProducerDomain());
	String subscriberDomain= domainHelper.getoriginalDomainName (request.getSubscriberDomain());
	Subscriber subscriber = Subscriber.builder().id(subscriberIdGenerationService.newAccountId())
	.producerDomainName (producerDomain).producerTopicName(request.getTopic())
	.subscriberDomainName(subscriberDomain)
	.subscriberTopicName("sub_" + request.getTopic ())
	.subscriptionDate(LocalDate.now()).subscriptionStatus(true).build();
	Subscriber newSubscriberCreated = subscriberService.addSubscriber (subscriber);
	APIResponse response = new APIResponse();
	response.setMessage("Subscribed to the topic successfully.");
	response. setData(newSubscriberCreated);
	return ResponseEntity. status(HttpStatus.CREATED). body (response);
}
	@ListSubscribersDoc
	@GetMapping("/subscribers")
	public ResponseEntity<APIResponse> listSubscribers() {
	
	//Log.info("SubscriberApiController - listSubscribe");
	List<Subscriber> subscribers = subscriberService.getSubscribers();
	APIResponse response = new APIResponse();
	response.setData(subscribers);
	return ResponseEntity.ok (response);
}
}