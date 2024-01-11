package com.paraport.kafkaapi.module.topic.controller;

import java.util.List;

import org.springframework.beans.factory. annotation. Autowired;
import org. springframework.http.HttpStatus;
import org. springframework.http.ResponseEntity;
import org. springframework. web.bind.annotation.GetMapping;
import org. springframework.web.bind.annotation.PathVariable;
import org .springframework. web.bind.annotation. PostMapping;
import org. springframework. web.bind. annotation. RequestBody;
import org. springframework. web.bind.annotation. RequestMapping;
import org.springframework.web.bind.annotation. RestController;
import com. paraport.kafkaapi.core.exception. APIException;
import com. paraport.kafkaapi.module.domain. DomainHelper;
import com. paraport.kafkaapi.module.topic.model.CreateTopicRequest;
import com. paraport.kafkaapi.module.topic.model.DescribeTopicResponse;
import com. paraport.kafkaapi.module.topic. model. TopicDetails;
import com. paraport.kafkaapi.module.topic.service.CreateTopicService;
import com.paraport.kafkaapi. module.topic.service.GetTopicInfoService;
import com.paraport.kafkaapi.module.topic.service.ListTopicsService;
/*import com.paraport.kafkaapi.swaggerdoc.CreateTopicDoc;
import com.paraport.kafkaapi.suaggerdoc.GetTopicInfoDoc;
import com. paraport.kafkaapi.swaggerdoc.ListTopicsDoc;*/
//import lcmbok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/v1")
public class TopicAPIController {
@Autowired
private GetTopicInfoService getTopicInfoservice;
@Autowired
private CreateTopicService createTopicService;
@Autowired
private ListTopicsService listTopicsService;
@Autowired
private DomainHelper domainHelper;

//@ListTopicsDoc
@GetMapping("/topics/domain Name}")
public ResponseEntity<List<TopicDetails>> fetchListOfTopics (@PathVariable("domainName") String domainName)
throws APIException
{
//Log.info("list topics in given a domain: {}", domain Name);
String originalDomainName = domainHelper.getoriginalDomainName(domainName); //
List<TopicDetails> listofTopicsResponse = listTopicsService.getListOfTopics (originalDomainName);
return ResponseEntity.status(HttpStatus.OK). body (listofTopicsResponse);
}

//@GetTopicInfoDoc
@GetMapping("/topics/{domain Nam}/{topicName}")
public ResponseEntity<DescribeTopicResponse>
getTopicInfo(@PathVariable("domainName") String domainName,@PathVariable("topicName") String topicName) throws APIException


{
	
//Log.info("get TopicInfo topicName: {} and domains: {}", topicName, domain Name);
String originalDomainName = domainHelper.getoriginalDomainName(domainName);
DescribeTopicResponse response = getTopicInfoservice.getTopicInfo (originalDomainName, topicName);


return ResponseEntity. status(HttpStatus.OK). body (response);
}

//@CreateTopicDoc
@PostMapping("/topics")
public ResponseEntity<DescribeTopicResponse> createTopic (@RequestBody CreateTopicRequest createTopicRequest)
throws APIException {
//Log.info("create topic with details: {}", createTopicRequest);
DescribeTopicResponse createTopicResponse = createTopicService.createTopic(createTopicRequest);
return ResponseEntity.status(HttpStatus. CREATED). body (createTopicResponse);
}

}
