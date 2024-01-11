package com.paraport.kafkaapi.module.topic.service;

import java.util.Collections;
import java.util.Map;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.DescribeTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.admin.TopicDescription;
import org.apache.kafka.common.Uuid;

import org.apache.kafka.common.errors.TopicExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.paraport.kafkaapi.core.exception.APIException;
import com.paraport.kafkaapi.core.helper.AwsKafkaHelper;
import com.paraport.kafkaapi.core.helper.AwsSecretsHelper;
import com.paraport.kafkaapi.core.model.AwsSecrets;
import com.paraport.kafkaapi.module.common.utils.CommonUtils;
import com.paraport.kafkaapi.module.domain.DomainHelper;
import com.paraport.kafkaapi.module.topic.mapper.DescribeTopicMapper;
import com.paraport.kafkaapi.module.topic.model.CreateTopicRequest;
import com.paraport.kafkaapi.module.topic.model.DescribeTopicResponse;
//import lombok. extern.slf4j.slf4j;
import com.paraport.kafkaapi.module.topic.validation.CreateTopicValidation;

//import lombok. extern.slf4j.slf4j;
//@s1f41
@Service
public class CreateTopicService {
	@Autowired
	AwsKafkaHelper awskafkaHelper;
	@Autowired
	AwsSecretsHelper secretsHelper;
	@Autowired
	CreateTopicValidation topicValidation;
	@Autowired
	DomainHelper domainHelper;
	private static final Integer DEFAULT_PARTIONS = 3;
	private static final Short DEFAULT_REPLICAS = 1;

	public DescribeTopicResponse createTopic(CreateTopicRequest createTopicRequest) throws APIException {
//Log.info("Creating topic");
		try {
			topicValidation.validateRequest(createTopicRequest);
			String domain = domainHelper.getoriginalDomainName(createTopicRequest.getDomain());
//Log.info("Topic to be created under ", domain);

//Get cluster ARN
			String clusterArn = awskafkaHelper.getClusterArn(domain);
//Get Secrets
			AwsSecrets awsSecrets = secretsHelper.getSecrets(domain, clusterArn);
//Get brokers
			String brokers = awskafkaHelper.getKafkaBrokersString(domain, clusterArn);
			Map<String, Object> kafkaConnProps = CommonUtils.getClientProperties(brokers, awsSecrets);
			TopicDescription topicInfo = createTopic(createTopicRequest, kafkaConnProps);
			
			return DescribeTopicMapper.getTopicResponse(topicInfo);
		} catch (APIException e) {
			throw e;
		} catch (Exception e) {
//Log.error("Exception ", e);
			APIException.serverException(e.getLocalizedMessage());
		}
		return null;

	}

	public TopicDescription createTopic(CreateTopicRequest topicReq, Map<String, Object> kafkaProps)
			throws APIException {
		String topicName = topicReq.getTopic();
		try (AdminClient adminClient = getClientForCreateTopic(kafkaProps)) {
			NewTopic newTopic = topicWithDefaultConfigs(topicReq);

			CreateTopicsResult createTopicsResult = adminClient.createTopics(Collections.singleton(newTopic));
			Uuid topicId = createTopicsResult.topicId(topicName).get();
//Log.info("Topic Id for the newly created topic is + topicId);
			DescribeTopicsResult topicResult = adminClient.describeTopics(Collections.singleton(topicName));
			return topicResult.topicNameValues().get(topicName).get();
		} catch (Exception e) {
//Log.error("Exception in createTopic(): ", e);
			if (e.getCause() != null && (e.getCause() instanceof TopicExistsException
					|| (e.getCause().getCause() != null && e.getCause().getCause() instanceof TopicExistsException))) {
				throw APIException.badRequestException(topicName + " already exists");
			}
			throw APIException.serverException();
		}
	}

	public AdminClient getClientForCreateTopic(Map<String, Object> kafkaProps) {
		return AdminClient.create(kafkaProps);
	}
	
	private NewTopic topicWithDefaultConfigs(CreateTopicRequest topicReq) {
		return new NewTopic (topicReq.getTopic (),
		topicReq.getPartitions() == null? DEFAULT_PARTIONS: topicReq.getPartitions(),
		topicReq.getReplicas () == null? DEFAULT_REPLICAS: topicReq.getReplicas());
}

}