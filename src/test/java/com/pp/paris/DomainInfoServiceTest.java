package com.pp.paris;

import static org. junit. jupiter.api. Assertions.assertEquals;
import static org. junit. jupiter. api. Assertions.assertNotNull;
import static org. junit. jupiter.api. Assertions.assertThrows;
import static org.mockito. Mockito.when;
import java.util.ArrayList;
import java.util.List;
import org. junit.jupiter.api. Test;
import org. junit.jupiter.api.extension. ExtendWith;
import org.mockito. InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter. SpringExtension;
import com. paraport.kafkaapi.core.cache. APICache;
import com. paraport.kafkaapi.core.exception. APIException;
import com. paraport.kafkaapi.module.domain.model.DomainInfoResponse;
import com. paraport.kafkaapi.module.domain.service.DomainInfoService;


import software.amazon. awssdk.services.kafka.KafkaClient;
import software.amazon.awssdk.services.kafka.model.ClusterInfo;
import software.amazon. awssdk. services.kafka.model.ListClustersResponse;

@ExtendWith (SpringExtension.class)
public class DomainInfoServiceTest {

	@Mock
	private KafkaClient awskafkaClient;
	@Mock
	private APICache cache;
	@InjectMocks
	private DomainInfoService domainInfoService;
	@Test
	void getDomainInfo_whenGivenDomainNotFound() throws APIException
	{
	ListClustersResponse clustersResponse=Mockito.mock (ListClustersResponse.class);
	List<ClusterInfo> clusterInfoList = Mockito.mock (List.class);
	when (awskafkaClient.listClusters()).thenReturn (clustersResponse);
	when(clustersResponse. clusterInfoList()).thenReturn(clusterInfoList);
	APIException apiException = assertThrows (APIException.class, () -> {
		domainInfoService.getDomainInformation ("test"); });
	assertEquals(500, apiException.getError().getCode());

}
}
