package com.pp.paris;

import static org. junit. jupiter.api. Assertions.assertEquals;
import static org. junit.jupiter.api. Assertions.assertThrows;
import static org.mockito.Mockito.when;
import java.util.List;
import org. junit.jupiter.api.BeforeEach;
import org. junit. jupiter.api. Test;
import org. junit.jupiter.api.extension.ExtendWith;
import org.mockito. InjectMocks;
import org.mockito.Mock;
import org.mockito. Mockito;
import org.springframework.test.context.junit.jupiter. SpringExtension;
import com.paraport.kafkaapi.core.cache. APICache;
import com.paraport.kafkaapi.core.exception. APIException;
import com.paraport.kafkaapi.core.helper. AwsKafkaHelper;
import software.amazon.awssdk.services.kafka.KafkaClient;
import software.amazon.awssdk.services.kafka.model.ClusterInfo;
import software.amazon.awssdk.services.kafka.model.GetBootstrapBrokersRequest;
import software.amazon.awssdk.services.kafka.model.GetBootstrapBrokersResponse;
import software.amazon.awssdk.services.kafka.model.ListClustersRequest;
import software.amazon.awssdk.services.kafka.model.ListClustersResponse;

@ExtendWith(SpringExtension.class)
class AwskafkaHelperTest {
@Mock
APICache cache;
@Mock
KafkaClient awskafkaClient;
@InjectMocks
AwsKafkaHelper awskafkaHelper;
@BeforeEach
void setup() {
	cache. evictAll();
}

@Test
void testGetClusterArn() throws APIException {
String expected = "cluster Arn";
ClusterInfo clusterInfo =ClusterInfo.builder ()
.clusterArn(expected)
.build();
List<ClusterInfo> clusterInfos = List. of(clusterInfo);
ListClustersResponse listClustersResponse = ListClustersResponse. builder()
.clusterInfoList (clusterInfos)
.build();

when (awskafkaClient.listClusters (Mockito. any (ListClustersRequest.class))). thenReturn (listClustersResponse);
assertEquals(expected, awskafkaHelper.getClusterArn("domain"));
}

@Test
void testGetClusterArn_CacheHit() throws APIException {
String expected = "cluster Arn";
String domainName = "domain";
when(cache.getCluster
(domainName)).thenReturn(expected);
assertEquals(expected, awskafkaHelper.getClusterArn(domainName));
}

@Test
void testGetClusterArn_NoClusterFound() throws APIException {
ListClustersResponse listClustersResponse = ListClustersResponse.builder().
build();
when (awskafkaClient.listClusters (Mockito.any(ListClustersRequest.class))).thenReturn (listClustersResponse);
APIException apiException = assertThrows(APIException.class,
()->awskafkaHelper.getClusterArn("domain"));
assertEquals(404, apiException.getError().getCode());
}

@Test
void testGetkafkaBrokersString_CacheHit() throws APIException {
String expected = "boostrapservers";
when(cache.getBroker (Mockito. anyString())).thenReturn (expected);
assertEquals(expected, awskafkaHelper.getKafkaBrokersString("domain", "cluster Arn"));
//awskafkaHelper.getKafkaBrokersString(domain, clusterArn)
}
@Test
void testGetkafkaBrokersString_BrokerNotFound () throws APIException {
GetBootstrapBrokersResponse bootstrapBrokersResponse = GetBootstrapBrokersResponse.builder()
.build();
when (awskafkaClient.getBootstrapBrokers (Mockito. any (GetBootstrapBrokersRequest.class)))
.thenReturn (bootstrapBrokersResponse);
APIException apiException= assertThrows(APIException.class,
() -> awskafkaHelper.getKafkaBrokersString("domain", "cluster Arn"));
assertEquals(404, apiException.getError().getCode());

}
}