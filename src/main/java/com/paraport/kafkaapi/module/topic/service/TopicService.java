package com.paraport.kafkaapi.module.topic.service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.kafka.clients.admin. AdminClient;
import org.apache.kafka.clients.admin. DescribeTopicsResult;
import org.apache. kafka.clients.admin. TopicDescription;

import org.apache.kafka.clients.consumer.Consumer;

import org.apache.kafka.common.PartitionInfo;
import org.springframework.beans.factory. annotation. Autowired;
import org.springframework. stereotype. Service;
import com.paraport.kafkaapi.core.helper.AwsKafkaHelper;
//import lombok.extern.slf4j.slf4j;
//@slf4j

@Service
public class TopicService {
@Autowired
private AwsKafkaHelper awskafkaHelper;

public boolean isTopicExists(String topic, String domain) {
boolean topicExists = false;
Consumer<String, Object> consumer=awskafkaHelper.getkafkaConsumerForDomain(domain);//getKafkaConsumerForDomain
Map<String, List<PartitionInfo>> topiclist = consumer.listTopics();
for (Entry<String, List<PartitionInfo>> entry: topiclist.entrySet()) {
if (entry.getKey().equalsIgnoreCase(topic))
{
topicExists = true;
break;
}
}
return topicExists;
}

public TopicDescription getTopicDetails(String topicName, String domainName) { 
AdminClient adminClient = awskafkaHelper.getAdminClientForDomain (domainName);
DescribeTopicsResult topicResult =adminClient.describeTopics (Collections.singleton(topicName));
try {
return topicResult.topicNameValues().get(topicName).get();
} catch (Exception e) {
//Log-debug("Error occurred while retrieving the topic details.", e);
}

return null;
}

}