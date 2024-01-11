package com.paraport.kafkaapi.module.topic.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.kafka.clients.consumer. Consumer;
import org.apache.kafka.clients.consumer. KafkaConsumer;
import org.apache.kafka.common.PartitionInfo;
import org.springframework.beans.factory. annotation. Autowired;
import org.springframework. stereotype. Service;
import com.paraport.kafkaapi.core.exception.APIException;
import com.paraport.kafkaapi.core.helper.AwsKafkaHelper;
import com.paraport.kafkaapi.core.helper.AwsSecretsHelper;
import com.paraport.kafkaapi.core.model.AwsSecrets;
import com.paraport.kafkaapi.module.common.utils.CommonUtils;
import com.paraport.kafkaapi.module.topic.model.TopicDetails;
import com.paraport.kafkaapi.module.topic.validation.ListTopicsValidation;

@Service
public class ListTopicsService {
@Autowired
private AwsKafkaHelper awskafkaHelper;
@Autowired
private AwsSecretsHelper secretsHelper;
@Autowired
private ListTopicsValidation topicsValidation;
public List<TopicDetails> getListOfTopics (String domain) throws APIException {
//Log.info("Listing topics");
try {
topicsValidation. validateRequest (domain);
// Get cluster ARN
String clusterArn= awskafkaHelper.getClusterArn (domain);
// Get Secrets
AwsSecrets awsSecrets = secretsHelper.getSecrets (domain, clusterArn);
// Get brokers.
String brokers =awskafkaHelper.getKafkaBrokersString(domain, clusterArn);
Map<String, Object> kafkaConnProps = CommonUtils.getClientProperties (brokers, awsSecrets);
return listTopics (kafkaConnProps);
}catch (APIException e) {
	throw e;
	
}catch (Exception e) {
	// TODO: handle exception
	throw APIException.serverException(e.getLocalizedMessage());
}
}

private List<TopicDetails> listTopics (Map<String, Object> kafkaProperties) throws Exception {
List<TopicDetails> listOfTopics = new ArrayList<>();
try (final Consumer<String, Object> consumer = getkafkaConsumer (kafkaProperties)) {
Map<String, List<PartitionInfo>> topiclist= consumer.listTopics();
//Log.info("Total number of topics {}", topiclist.size());
for (Entry<String, List<PartitionInfo>> entry :topiclist.entrySet()) {
TopicDetails topic = new TopicDetails();
topic.setTopicName(entry.getKey());
topic.setPartitions (consumer.partitionsFor (entry.getKey()).size());
listOfTopics.add(topic);
}
}
return listOfTopics;
}
public KafkaConsumer <String, Object> getkafkaConsumer (Map<String, Object> kafkaProperties) {
return new KafkaConsumer <> (kafkaProperties);
}
}