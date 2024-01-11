package com.pp.paris;

import static org. junit.jupiter.api. Assertions. assertEquals;
import static org. junit.jupiter.api. Assertions.assertNotNull;
import static org. junit.jupiter.api. Assertions.assertThrows;
import static org.mockito. Mockito. doReturn;
import static org.mockito. Mockito.when;
import java.util.ArrayList;
import java.util. HashMap;
import java.util.Map;
import java.util.concurrent. ExecutionException;
import org.apache.kafka.clients.admin. AdminClient;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.DescribeTopicsResult;
import org.apache.kafka.clients.admin.MockAdminClient;
import org.apache.kafka.clients.admin.TopicDescription;
import org.apache.kafka.common. KafkaException;
import org.apache.kafka.common. KafkaFuture;
import org.apache.kafka.common. TopicPartitionInfo;



import org.apache.kafka.common. Uuid;
import org.apache.kafka.common. errors. InvalidReplicationFactorException;
import org.apache.kafka.common.errors. TopicExistsException;
import org. junit. jupiter.api. BeforeEach;
import org. junit. jupiter. api. Test;
import org. junit.jupiter.api.extension. ExtendWith;
import org.mockito. InjectMocks;
import org.mockito.Mock;
import org.mockito. Mockito;
import org.springframework.test.context.junit.jupiter. SpringExtension;

import com.paraport.kafkaapi.core.cache.APICache;
import com. paraport.kafkaapi.core.exception. APIException;
import com.paraport.kafkaapi.core.helper.AwsKafkaHelper;
import com. paraport.kafkaapi.core.helper.AwsSecretsHelper;



import com. paraport.kafkaapi.core.model.AwsSecrets;
import com.paraport.kafkaapi.module.domain.DomainHelper;
import com.paraport.kafkaapi.module.topic.model.CreateTopicRequest;
import com.paraport.kafkaapi.module.topic.model.DescribeTopicResponse;
import com. paraport.kafkaapi.module.topic.service.CreateTopicService;
import com.paraport.kafkaapi.module.topic.validation. CreateTopicValidation;

@ExtendWith(SpringExtension.class)
class CreateTopicServiceTest {

@Mock
APICache cacheHelper;
@Mock
AwsKafkaHelper awskafkaHelper;
@Mock
AwsSecretsHelper secretsHelper;
@Mock
CreateTopicValidation topicvalidation;



@Mock
DomainHelper domainHelper;
@InjectMocks
CreateTopicService createTopicService;
private AdminClient mockAdminClient = Mockito.mock (MockAdminClient.class);
@BeforeEach
void before() throws APIException {
when (awskafkaHelper.getClusterArn (Mockito. anyString())).thenReturn("cluster Arn");
when (awskafkaHelper.getKafkaBrokersString(Mockito.anyString(), Mockito.anyString())).thenReturn("brokerUrl");
when (secretsHelper.getSecrets (Mockito. anyString(), Mockito. anyString())).
thenReturn(new AwsSecrets ("username", "password"));
when( domainHelper.getoriginalDomainName (Mockito. anyString())).thenReturn("domainName");
}

@Test
void testCreateTopic()
throws APIException {
CreateTopicService spyCreateTopicService = Mockito. spy (createTopicService);
doReturn (mockAdminClient).when (spyCreateTopicService).getClientForCreateTopic (Mockito. any());
CreateTopicsResult createTopicsResult = Mockito.mock (CreateTopicsResult.class);
KafkaFuture<Uuid> uuidFuture = KafkaFuture.completedFuture(Uuid. randomUuid());
when(createTopicsResult.topicId(Mockito. anyString())).thenReturn (uuidFuture);
when (mockAdminClient.createTopics (Mockito. anyCollection ())).thenReturn (createTopicsResult);
DescribeTopicsResult topicResult = Mockito.mock (DescribeTopicsResult.class);
Map<String, KafkaFuture<TopicDescription>> map = new HashMap<>();
TopicDescription description = new TopicDescription("topicName", false, new ArrayList<TopicPartitionInfo>());
KafkaFuture<TopicDescription> descriptionFuture = KafkaFuture.completedFuture(description);
map.put("topicName", descriptionFuture);
when (topicResult.topicNameValues()).thenReturn
(map);
when (mockAdminClient.describeTopics (Mockito. anyCollection())).thenReturn (topicResult);
CreateTopicRequest createTopicRequest = new CreateTopicRequest();
createTopicRequest.setDomain ("domain");
createTopicRequest.setTopic ("topicName");
createTopicRequest.setPartitions (3);
createTopicRequest.setReplicas (Short.valueOf("1"));
DescribeTopicResponse apiResponse = spyCreateTopicService.createTopic (createTopicRequest);
assertNotNull (apiResponse);

}

}