package com.paraport.kafkaapi.core.helper;

import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.stereotype.Component;

import com.paraport.kafkaapi.core.cache.APICache;
import com.paraport.kafkaapi.core.exception.APIException;
import com.paraport.kafkaapi.core.model.AwsSecrets;

import com.paraport.kafkaapi.module.common.utils.CommonUtils;
//import lombok.extern.slf4j.slf4j;

import software.amazon.awssdk.services.kafka.model.GetBootstrapBrokersRequest;
import software.amazon.awssdk.services.kafka.model.GetBootstrapBrokersResponse;
import software.amazon.awssdk.services.kafka.model.ListClustersRequest;
import software.amazon.awssdk.services.kafka.model.ListClustersResponse;
import software.amazon.awssdk.services.kafka.KafkaClient;
import software.amazon.awssdk.services.kafka.model.ClusterInfo;

@Component
public class AwsKafkaHelper {
	private static final String SASL_SCRAM_BROKERS_NOT_FOUND = "SASL SCRAM broker string was not found for %s";

	@Autowired

	@Qualifier("caffeineCacheHelper")
	APICache apiCache;
	@Autowired
	KafkaClient awskafkaClient;
	@Autowired
	private AwsSecretsHelper secretsHelper;

public String getClusterArn(String domain) {

	
//log.info("Getting Cluster ARN for {}", domain);|
String clusterArn= apiCache.getCluster(domain);
if (!StringUtils.isBlank (clusterArn))
{
//Log.info("Cluster ARN present in Cache");
return clusterArn;
}
ListClustersRequest clustersRequest =ListClustersRequest.builder()
.clusterNameFilter (domain)
.build(); 
List<ClusterInfo> clusterInfos =getClusters(clustersRequest, false);

clusterArn= clusterInfos.get(0).clusterArn();

	
//log.info("Setting Cluster ARN in Cache");apicache.setCluster(domain,

	apiCache.setCluster(domain, clusterArn);
	return clusterArn;
	
}

	public List<ClusterInfo> getClusters(ListClustersRequest clustersRequest, boolean fetchAll) throws APIException {
   ListClustersResponse listClustersResponse =awskafkaClient.listClusters(clustersRequest);

	List<ClusterInfo> clusterInfos = listClustersResponse.clusterInfoList();
	
	if(clusterInfos.isEmpty())
	{
throw APIException.notFoundException ("No HSK Cluster found");
}
	if(!fetchAll&&clusterInfos.size()>1) {
		throw APIException.badRequestException("Multiple MSK Clusters found");
	}
return clusterInfos;

	}

public String getKafkaBrokersString(String domain, String clusterArn) throws APIException {
//Log.info("Getting broker strings for ", cluster Arn);
String boostrapBrokers = apiCache.getBroker(clusterArn);
if(!StringUtils.isBlank(boostrapBrokers))
	{
return boostrapBrokers;

}

boostrapBrokers =getBrokers(clusterArn);
if (StringUtils.isBlank (boostrapBrokers)) {
	throw APIException.notFoundException (String.format(SASL_SCRAM_BROKERS_NOT_FOUND, domain));
}
//tog.info("Setting Broker string in cache");
apiCache.setBroker(clusterArn, boostrapBrokers);
return boostrapBrokers;
}


public String getBrokers(String clusterArn) {
GetBootstrapBrokersRequest brokersRequest = GetBootstrapBrokersRequest.builder()
.clusterArn(clusterArn)
.build();

GetBootstrapBrokersResponse bootstrapBrokersResponse = awskafkaClient.getBootstrapBrokers(brokersRequest);

return bootstrapBrokersResponse.bootstrapBrokerStringPublicSaslScram();
}
public KafkaTemplate<String, Object> getkafkaTemplate(String clusterArn, Map<String, Object> properties)
throws APIException {
KafkaTemplate<String, Object> kafkaTemplate = apiCache.getkafkaTemplate(clusterArn);
if (kafkaTemplate != null) {
//Log.info("Getting kafkatemplate from cache");
return kafkaTemplate;
}
ProducerFactory<String, Object> factory = new DefaultKafkaProducerFactory<>(properties);


kafkaTemplate = new KafkaTemplate<> (factory, true);
//Log.info("Setting Kafka Template for {} in cache", cluster Arn);
apiCache.setkafkaTemplate(clusterArn, kafkaTemplate);
return kafkaTemplate;
}

public Map<String, Object> getkafkaConnectionForDomain(String domainName){
String clusterArn= getClusterArn(domainName);
AwsSecrets awsSecrets = secretsHelper.getSecrets(domainName, clusterArn);
String brokers = getKafkaBrokersString(domainName, clusterArn);
return CommonUtils.getClientProperties(brokers, awsSecrets);
}
public Consumer<String, Object> getkafkaConsumerForDomain(String domainName) {
Map<String, Object> kafkaConnProps= getkafkaConnectionForDomain (domainName);
return new KafkaConsumer <> (kafkaConnProps);
}
public AdminClient getAdminClientForDomain(String domainName) {
Map<String, Object> kafkaConnProps = getkafkaConnectionForDomain(domainName);
return AdminClient.create(kafkaConnProps);
}



}