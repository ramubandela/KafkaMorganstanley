package com.paraport.kafkaapi.core.helper;

import java.util.Map;
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
//import lombok. extern.slf4j.slf4j;
import com.paraport.kafkaapi.module.common.utils.Constants;

import lombok.NonNull;

public class KafkaTemplateHelper {
	
	@Autowired
	@Qualifier("caffeineCacheHelper")
	APICache apiCache;
	@Autowired
	AwsKafkaHelper awskafkaHelper;
	@Autowired
	AwsSecretsHelper secretsHelper;
	 public KafkaTemplate<String, Object> getkafkaTemplate(String domain) throws APIException {
	final String clusterArn= awskafkaHelper.getClusterArn(domain);
	return getkafkaTemplate(domain, clusterArn);
	 }
	 
		/*
		 * public KafkaTemplate<String, Object> getkafkaTemplate(String domain, String
		 * clusterArn) throws APIException { KafkaTemplate<String, Object> kafkaTemplate
		 * = apiCache.getkafkaTemplate(clusterArn); if (kafkaTemplate != null) {
		 * //Log.info("Getting kafkatemplate from cache"); return kafkaTemplate; }
		 * 
		 * //Log.info("fetching brokerString"); final String brokers =
		 * awskafkaHelper.getKafkaBrokersString(domain, clusterArn);
		 * //Log.info("fetching aus secrets"); //final AwsSecrets assecrets =
		 * secretsHelper.getSecrets (domain, clusterArn); //final String brokers =
		 * awskafkaHelper.getKafkaBrokersString(domain, clusterArn);
		 * //tog.info("fetching aws secrets"); final AwsSecrets awsSecrets =
		 * secretsHelper.getSecrets (domain, clusterArn); final Map<String, Object>
		 * kafkaConfigProp = CommonUtils.getClientProperties (brokers, awsSecrets);
		 * ProducerFactory<String, Object> factory = new DefaultKafkaProducerFactory<>
		 * (kafkaConfigProp); kafkaTemplate = new KafkaTemplate<>(factory, true);
		 * //Log.info("Setting Kafka Template for {} in cache", clusterArn);
		 * apiCache.setkafkaTemplate(clusterArn, kafkaTemplate); return kafkaTemplate;
		 * 
		 * }
		 */	public KafkaTemplate<String, Object> getKafkaTemplate(@NonNull String domain, @NonNull String acks) throws APIException {
			
			//Log.info("fetching cluster arn");
			String clusterArn= awskafkaHelper.getClusterArn(domain);
			String cacheKey = String.join("-", clusterArn, acks);
			KafkaTemplate<String, Object> kafkaTemplate = apiCache.getkafkaTemplate(cacheKey);
			if (kafkaTemplate != null) {
			//Log.info("Getting kafkatemplate from cache for key {}", cacheKey);
			return kafkaTemplate;
			}
			Map<String, Object> kafkaConfigProp = getProducerConfigs(domain, clusterArn, acks);
			ProducerFactory<String, Object> factory = new DefaultKafkaProducerFactory<> (kafkaConfigProp);
			kafkaTemplate = new KafkaTemplate<>(factory, Constants.DEFAULT_PRODUCER_AUTO_FLUSH);
			//Log.info("Setting Kafka Template for {} in cache", cacheKey);
			apiCache. setkafkaTemplate(cacheKey, kafkaTemplate);
			return kafkaTemplate;	
	
}
	
	
	private Map<String, Object> getProducerConfigs (@NonNull String domain, @NonNull String clusterArn,
	@NonNull String acks) {
	//tog.info("fetching brokerString");
	String brokers = awskafkaHelper.getKafkaBrokersString(domain, clusterArn);
	//tog.info("fetching aws secrets");
	AwsSecrets awsSecrets = secretsHelper.getSecrets (domain, clusterArn);
	return CommonUtils.getClientProperties(brokers, awsSecrets, acks);
	
	}

	
}