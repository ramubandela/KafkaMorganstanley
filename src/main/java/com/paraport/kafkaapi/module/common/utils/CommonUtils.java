package com.paraport.kafkaapi.module.common.utils;

import java.util. HashMap;
import java.util.Map;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config. SaslConfigs;
import org.apache.kafka.common.serialization. StringDeserializer;
import org.apache.kafka.common.serialization. StringSerializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import com.paraport.kafkaapi.core.model. AwsSecrets;


public class CommonUtils {
public static Map<String, Object> getClientProperties(String brokerUrls, AwsSecrets awsSecrets) {
final String JASS_TEMPLATE="org.apache.kafka.common.security.scram. ScramLoginModule required username=\"%s\" password=\"%s\";";
Map<String, Object> properties= new HashMap<>();
properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokerUrls);
properties.put(ProducerConfig.CLIENT_ID_CONFIG, "KafkaAPI");
properties.put(ProducerConfig. KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
properties.put (ProducerConfig. VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class.getName());
properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class.getName());
properties.put(CommonClientConfigs. SECURITY_PROTOCOL_CONFIG, "SASL_SSL");
properties.put(SaslConfigs. SASL_MECHANISM, "SCRAM-SHA-512");
properties.put("ssl.endpoint.identification. algorithm", "");
properties.put(CommonClientConfigs.RETRY_BACKOFF_MS_CONFIG, 100);
properties.put(CommonClientConfigs.REQUEST_TIMEOUT_MS_CONFIG, 1000);
properties.put(ProducerConfig. DELIVERY_TIMEOUT_MS_CONFIG, 3000);
properties.put(ProducerConfig.ACKS_CONFIG, "1");
properties.put(SaslConfigs. SASL_JAAS_CONFIG,

String.format(JASS_TEMPLATE, awsSecrets.getUsername(), awsSecrets.getPassword()));
return properties;
}



public static Map<String, Object> getClientProperties(String brokerUrls, AwsSecrets awsSecrets,String acks) {
	
	Map<String, Object> clientProperties = getClientProperties(brokerUrls,awsSecrets);
	clientProperties.put(ProducerConfig.ACKS_CONFIG, acks);
	
	return clientProperties;
}
}