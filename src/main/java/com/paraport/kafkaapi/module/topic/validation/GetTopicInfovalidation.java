package com.paraport.kafkaapi.module.topic.validation;


import java.util. Collection;
import java.util.List;
import java.util.stream. Collectors;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.admin. TopicListing;
import org.springframework.stereotype.Component;
import com.paraport.kafkaapi.core.exception. APIException;

//import software.amazon.awssdk.utils.StringUtils;
//import lombok.extern.slf4j.slf4j;
@Component
//@slf4j
public class GetTopicInfovalidation {
	
public void validateRequestAttributes(String domainName, String topicName) throws APIException {
if (StringUtils.isBlank(domainName)) {
throw APIException.badRequestException ("domainName path parameter is missing");
}
if (StringUtils.isBlank (topicName)) {
throw APIException.badRequestException ("topicName path parameter is missing");
}
}
public void validateTopic (String topicName, Collection<TopicListing> topiclistings) throws APIException{
List<String> topics= topiclistings.stream().map (TopicListing::name).collect(Collectors.toList());
if (!topics.contains(topicName)) {
//Log.error("Exception will be thrown as topic is not available with specified list");
throw APIException.notFoundException (topicName + " not found");
}
}

}