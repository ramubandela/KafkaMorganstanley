package com.pp.paris;

import static org.junit.jupiter.api. Assertions.assertEquals;
import static org. junit.jupiter.api. Assertions.assertNotNull;
import static org.junit.jupiter.api. Assertions.assertThrows;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito. doReturn;
import static org.mockito.Mockito.when;
import java.util.Collection;
import java.util. Collections;
import java.util. HashMap;
import java.util. Map;
import java.util.concurrent. ExecutionException;
import org.apache.kafka.clients.admin. AdminClient;
import org.apache.kafka.clients.admin. DescribeTopicsResult;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.clients.admin. MockAdminClient;
import org.apache.kafka.clients.admin. TopicDescription;
import org.apache.kafka.clients.admin. TopicListing;
import org.apache.kafka.common.KafkaFuture;
import org.apache.kafka.common. Node;
import org.apache.kafka.common. TopicPartitionInfo;
import org.apache.kafka.common. Uuid;
import org. junit.jupiter.api. BeforeEach;
import org. junit.jupiter.api. Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito. InjectMocks;
import org.mockito.Mock;
import org.mockito. Mockito;
import
org.springframework.test.context.junit.jupiter.SpringExtension;

import com.paraport.kafkaapi.core.exception. APIException;



import com. paraport.kafkaapi.core.helper.AwsKafkaHelper;
import com.paraport.kafkaapi.core.helper.AwsSecretsHelper;
import com.paraport.kafkaapi.core.model. AwsSecrets;
import com.paraport.kafkaapi.module.topic.model.DescribeTopicResponse;
import com.paraport.kafkaapi.module.topic.service. GetTopicInfoService;

import com. paraport.kafkaapi.module.topic.validation.GetTopicInfovalidation;
@ExtendWith(SpringExtension.class)
class GetTopicInfoServiceTest {


	@Mock
	AwsKafkaHelper awskafkaHelper;
	@Mock
	AwsSecretsHelper secretsHelper;
	@Mock
	private GetTopicInfovalidation topicInfovalidation;
	@InjectMocks
	GetTopicInfoService getTopicInfoservice;
	
	
	private AdminClient mockAdminClient=Mockito.mock(MockAdminClient.class);
	
	@BeforeEach
	void before() throws APIException {
	when (awskafkaHelper.getClusterArn(Mockito. anyString())).thenReturn("cluster Arn");
	when (awskafkaHelper.getKafkaBrokersString(Mockito. anyString(), Mockito. anyString())).thenReturn("brokerUrl");
	when (secretsHelper.getSecrets (Mockito. anyString(), Mockito. anyString())).
	thenReturn(new AwsSecrets("username", "password"));
	doCallRealMethod().when (topicInfovalidation).validateRequestAttributes (Mockito. anyString(),
	Mockito.anyString());
}

	@Test
	void getTopicInfoTest() throws APIException, InterruptedException, ExecutionException
	{
	
		
	GetTopicInfoService spyGetTopicInfoService = Mockito. spy (getTopicInfoservice);
	doReturn (mockAdminClient).when (spyGetTopicInfoService).getClientForGetTopic( Mockito. any());
	TopicListing topicListing = new TopicListing("topicName", Uuid.randomUuid(), false);
	ListTopicsResult listTopicsResult = Mockito.mock (ListTopicsResult.class);
	KafkaFuture<Collection<TopicListing>> listingsFuture = KafkaFuture
	.completedFuture (Collections. singletonList (topicListing));
	when (listTopicsResult.listings()).thenReturn(listingsFuture);
	when (mockAdminClient.listTopics()).thenReturn (listTopicsResult);
	DescribeTopicsResult topicResult = Mockito.mock (DescribeTopicsResult.class);
	Map<String, KafkaFuture<TopicDescription>> map = new HashMap<>();
	
	Node leader = new Node(1, "localhost", 9062);
	TopicPartitionInfo partitionInfo = new TopicPartitionInfo(1, leader, Collections. singletonList (leader),
	Collections. singletonList(leader));
	TopicDescription description = new TopicDescription("topicName", false,
	Collections. singletonList (partitionInfo));
	KafkaFuture<TopicDescription> descriptionFuture = KafkaFuture.completedFuture(description);
	map.put("topicName", descriptionFuture);
	when (topicResult.topicNameValues()).thenReturn (map);
	
	when (mockAdminClient.describeTopics (Mockito. anyCollection ())).thenReturn (topicResult);
	DescribeTopicResponse topicInfo = spyGetTopicInfoService.getTopicInfo(" domainName", "topicName");
	assertNotNull(topicInfo);
	
	}
}