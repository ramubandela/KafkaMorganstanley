package com.paraport.kafkaapi.module.messaging.validation;


import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.common. PartitionInfo;
import org.springframework.beans.factory. annotation.Autowired;
import org.springframework.kafka.core. KafkaTemplate;
import org.springframework.stereotype. Component;
import org.springframework.util.CollectionUtils;
import com.paraport.kafkaapi.core.exception. APIException;
import com.paraport.kafkaapi.core.helper.KafkaTemplateHelper;

@Component
public class ProduceMessageValidation {
private static final String TOPIC_NOT_FOUND= "The given topic %s is not present in domain %s";
@Autowired
private KafkaTemplateHelper kafkaTemplateHelper;

public void validate(final List<String> domains, final String topicName, final Object messageBody)
throws APIException {
if (CollectionUtils.isEmpty (domains)) {
throw APIException.badRequestException ("Domain name is required");
} else if (domains.contains (null)) {
throw APIException
.badRequestException ("One or more domain name is not present in the specified Kafka cluster");
}
if (StringUtils.isBlank (topicName)) {
throw APIException.badRequestException ("Topic name is required");
}
if (ObjectUtils.isEmpty (messageBody)) {
throw APIException.badRequestException ("message body required");
}
domains. forEach(domain ->checkIfTopicExist(topicName, domain));
}

private void checkIfTopicExist(final String topicName, String domain) throws APIException {
//Log.info("check if topic exist for domain: () and topic: ()", domain, topicName);
final KafkaTemplate<String, Object> kafkaTemplate = kafkaTemplateHelper.getkafkaTemplate(domain);
//Check if topic exists
final List<PartitionInfo> listPartitionInfo= kafkaTemplate.partitionsFor (topicName);
if (CollectionUtils.isEmpty (listPartitionInfo)) {
throw APIException.notFoundException (String.format(TOPIC_NOT_FOUND, topicName, domain));


}
}
}