package com.paraport.kafkaapi.module.topic.service;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.DescribeTopicsResult;
import org.apache.kafka.clients.admin.TopicDescription;
import org.apache.kafka.clients.admin.TopicListing;
import org.springframework.beans.factory.annotation. Autowired;
import org.springframework.beans.factory.annotation. Qualifier;
import org.springframework. stereotype. Service;

import com.paraport.kafkaapi.core.cache.APICache;
import com.paraport.kafkaapi.core.exception. APIException;
import com.paraport.kafkaapi.core.helper.AwsKafkaHelper;
import com. paraport.kafkaapi.core.helper.AwsSecretsHelper;
import com.paraport.kafkaapi.core.model. AwsSecrets;
import com.paraport.kafkaapi.module.common.utils.CommonUtils;
import com.paraport.kafkaapi.module.topic.mapper.DescribeTopicMapper;
import com.paraport.kafkaapi.module.topic.model. DescribeTopicResponse;
import com.paraport.kafkaapi.module.topic.validation. GetTopicInfovalidation;


@Service
public class GetTopicInfoService {
	
	@Autowired
	@Qualifier("caffeineCacheHelper")
	private APICache cache;
	
	
	@Autowired
	private AwsKafkaHelper awskafkaHelper;
	@Autowired
	private AwsSecretsHelper secretsHelper;
	@Autowired
	private GetTopicInfovalidation topicInfoValidation;
	public DescribeTopicResponse getTopicInfo(String domainName, String topicName) throws APIException {
	try {
	topicInfoValidation.validateRequestAttributes(domainName, topicName);
	// Get cluster ARN
	String clusterArn= awskafkaHelper.getClusterArn(domainName);
	// Get broker strings
	String brokers = awskafkaHelper.getKafkaBrokersString(domainName, clusterArn);
	// Get secrets
	AwsSecrets awsSecrets =secretsHelper.getSecrets (domainName, clusterArn);
	
	Map<String, Object> kafkaProps = CommonUtils.getClientProperties(brokers, awsSecrets);
	//to infal"Setting tonir information for " + tantellama)
	TopicDescription topicInfo = getTopicDescription (topicName, kafkaProps);
	return DescribeTopicMapper.getTopicResponse (topicInfo);
	}
	catch (APIException e) {
	//Log.error("API_EXCEPTION - ", e);
	throw e;
	} catch (Exception e) {
	//Log.error("Exception - ", e);
	throw
	APIException.serverException (e.getLocalizedMessage());
	}
	}
	private TopicDescription getTopicDescription (String topicName, Map<String, Object> kafkaProps) throws APIException {
		try (AdminClient adminClient = getClientForGetTopic(kafkaProps)) {
		Collection<TopicListing> topicListings = adminClient.listTopics ().listings().get();
		topicInfoValidation.validateTopic (topicName, topicListings);
		DescribeTopicsResult topicResult = adminClient.describeTopics (Collections. singleton (topicName));
		return topicResult.topicNameValues().get (topicName).get();
		} catch (APIException e) {
		throw e;
		} catch (Exception e) {
		//Log.error("Exception in get Topic Description(): ", e);
		throw APIException.serverException();
		}
	}
	
		public AdminClient getClientForGetTopic (Map<String, Object> kafkaConnProps) {
		return AdminClient.create(kafkaConnProps);
		}
}
