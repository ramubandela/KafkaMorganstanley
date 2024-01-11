package com.paraport.kafkaapi.core.cache;

import java.util.List;
import java.util.concurrent.ConcurrentMap;
import org.springframework.kafka.core.KafkaTemplate;
import com.paraport.kafkaapi.core.exception.APIException;
import com.paraport.kafkaapi.core.model.AwsSecrets;
import com.paraport.kafkaapi.module.domain.model.DomainInfoResponse;

public interface APICache {

	static final String DEFAULT_DOMAINS_KEY = "domains";
	static final String CLUSTER_CACHE_EXCEPTION = "Exception while setting Cluster cache";
	static final String BROKER_CACHE_EXCEPTION="Exception while setting Broker cache";
	
	static final String SECRETS_CACHE_EXCEPTION = "Exception while setting Secret cache";
	static final String KAFKATEMPLATE_CACHE_EXCEPTION = "Exception while setting KafkaTemplate cache";

	default boolean iskeyPresent(Object key) {
		return key != null;
	}

	default boolean iskeyValuePresent(Object key, Object value) {
		return key != null && value != null;
	}

	ConcurrentMap<String, String> getClusterCache();

	String getCluster(String domain);

	String setCluster(String domain, String clusterArn) throws APIException;

	ConcurrentMap<String, String> getBrokerCache();

	String getBroker(String clusterArn);

	String setBroker(String clusterArn, String brokerString) throws APIException;

	ConcurrentMap<String, AwsSecrets> getSecretCache();

	//String getCluster(String domain);

//String setCluster (String domain, String cluster Arn) throws APIException;
//ConcurrentMap<String, String> getBroker Cache();

//String getBroker(String cluster Arn);

//String setBroker (String cluster Arn, String brokerString) throws APIException;
//ConcurrentMap<String, AwsSecrets> getSecretCache();

AwsSecrets getSecret (String clusterArn);
AwsSecrets setSecret(String clusterArn, AwsSecrets secrets) throws APIException;

ConcurrentMap<String, KafkaTemplate<String, Object>> getkafkaTemplateCache();

KafkaTemplate<String, Object> getkafkaTemplate(String clusterArn);

KafkaTemplate<String, Object> setkafkaTemplate(String clusterArn, KafkaTemplate<String, Object> kafkaTemplate) throws APIException;

List<DomainInfoResponse> getDomains();


List<DomainInfoResponse> setDomains (List<DomainInfoResponse> domains);



void evictAll();

}
