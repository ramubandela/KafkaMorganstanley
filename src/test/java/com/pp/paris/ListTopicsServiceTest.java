package com.pp.paris;

import static org.junit.jupiter.api.Assertions. assertEquals;
import static org.junit.jupiter.api.Assertions. assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito. Mockito. doCallRealMethod;
import static org.mockito. Mockito. doReturn;



import static org.mockito. Mockito.when;
import java.util.ArrayList;
import java.util. HashMap;
import java.util.List;
import java.util.Map;
import org.apache.kafka.clients.consumer. Consumer;
import org.apache.kafka.clients.consumer. KafkaConsumer;
import org.apache.kafka.common.Node;
import org.apache.kafka.common.PartitionInfo;
import org. junit.jupiter.api. BeforeEach;
import org. junit.jupiter.api. Test;
import org. junit.jupiter.api.extension. ExtendWith;
import org.mockito. InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter. SpringExtension;
import com.paraport.kafkaapi.core.exception. APIException;


import com.paraport.kafkaapi.core.helper.AwsKafkaHelper;
import com.paraport.kafkaapi.core.helper.AwsSecretsHelper;
import com.paraport.kafkaapi.core.model. AwsSecrets;


import com.paraport.kafkaapi.module.topic.model. TopicDetails;
import com.paraport.kafkaapi.module.topic.service.ListTopicsService;
import com.paraport.kafkaapi.module.topic.validation.ListTopicsValidation;

@ExtendWith(SpringExtension.class)
public class ListTopicsServiceTest {
	
	
	
	@Mock
	AwsKafkaHelper awskafkaHelper;
	@Mock
	AwsSecretsHelper secretsHelper;
	@Mock
	private ListTopicsValidation topicsValidation;
	@InjectMocks
	ListTopicsService listTopicsService;
	private Consumer<String, Object> consumer = Mockito.mock (KafkaConsumer.class);
	@BeforeEach
	void before() throws APIException {
	when (awskafkaHelper.getClusterArn (Mockito. anyString())). thenReturn("cluster Arn");
	when (awskafkaHelper.getKafkaBrokersString(Mockito. anyString(), Mockito. anyString())). thenReturn("brokerUrl");
	when (secretsHelper.getSecrets (Mockito. anyString(), Mockito. anyString()))
	.thenReturn(new AwsSecrets("username", "password"));
	doCallRealMethod().when (topicsValidation). validateRequest (Mockito. any());

}

	@Test
	void TestGetListOfTopics() throws APIException {
	ListTopicsService spyListTopicsService =Mockito. spy (listTopicsService);
	doReturn (consumer). when (spyListTopicsService).getkafkaConsumer (Mockito. any());
	Node Leader = new Node(1, "localhost", 8080);
	Node[] nodel = new Node[2];
	nodel [0] = new Node(2, "localhost", 8080);
	nodel [1] = new Node(3, "localhost", 8080);
	Node[] node12 = new Node[2];
	node12[0]= new Node (2, "localhost", 8080);
	node12[1] = new Node(3, "localhost", 8080);
	List<PartitionInfo> partitions = new ArrayList<>();
	PartitionInfo info= new PartitionInfo("test", 1, Leader, node12, nodel);
	partitions.add(info);
	Map<String, List<PartitionInfo>> topicList = new HashMap<>();
	topicList.put("test", partitions);
	when (consumer.listTopics()). thenReturn(topicList);
	List<TopicDetails> listOfTopics= spyListTopicsService.getListOfTopics("test");
	assertNotNull(listOfTopics);
	}
}