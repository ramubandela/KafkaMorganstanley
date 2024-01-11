package com.paraport.kafkaapi.core.cache;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.paraport.kafkaapi.core.exception.APIException;
import com.paraport.kafkaapi.core.model.AwsSecrets;
import com.paraport.kafkaapi.module.domain.model.DomainInfoResponse;

@Component("caffeineCacheHelper")
public final class CaffeineCache implements APICache {
	private static final int MAX_CACHE_SIZE = 100;
	private static long TTL_IN_MINS = 30;

	private static final Cache<String, String> CLUSTER_CACHE = Caffeine.newBuilder().maximumSize(MAX_CACHE_SIZE)
			.expireAfterWrite(Duration.ofMinutes(TTL_IN_MINS)).build();

	private static final Cache<String, String> BROKER_CACHE = Caffeine.newBuilder().maximumSize(MAX_CACHE_SIZE)
			.expireAfterWrite(Duration.ofMinutes(TTL_IN_MINS)).build();
	private static final Cache<String, AwsSecrets> SECRETS_CACHE = Caffeine.newBuilder().maximumSize(MAX_CACHE_SIZE)
			.expireAfterWrite(Duration.ofMinutes(TTL_IN_MINS)).build();

	private static final Cache<String, KafkaTemplate<String, Object>> KAFKATEMPLATE_CACHE = Caffeine.newBuilder()
			.maximumSize(MAX_CACHE_SIZE).expireAfterWrite(Duration.ofMinutes(TTL_IN_MINS)).build();

	private static final Cache<String, List<DomainInfoResponse>> DOMAINS_CACHE = Caffeine.newBuilder()
			.maximumSize(MAX_CACHE_SIZE).expireAfterWrite(Duration.ofMinutes(TTL_IN_MINS)).build();

	@Override
	public ConcurrentMap<String, String> getClusterCache() {
		return CLUSTER_CACHE.asMap();
	}

	@Override
	public String getCluster(String domain) {
		return iskeyPresent(domain) ? CLUSTER_CACHE.getIfPresent(domain) : null;
	}

	@Override
	public String setCluster(String domain, String clusterArn) throws APIException {

		if (!iskeyValuePresent(domain, clusterArn)) {
			throw APIException.serverException(CLUSTER_CACHE_EXCEPTION);
		}
		String previousValue = CLUSTER_CACHE.getIfPresent(domain);
		CLUSTER_CACHE.put(domain, clusterArn);
		return previousValue;
	}

	@Override
	public ConcurrentMap<String, String> getBrokerCache() {
		return BROKER_CACHE.asMap();
	}

	@Override
	public String getBroker(String clusterArn) {
		return iskeyPresent(clusterArn) ? BROKER_CACHE.getIfPresent(clusterArn) : null;
	}

@Override
public String setBroker(String clusterArn, String brokerString) throws APIException {
if (!iskeyValuePresent (clusterArn, brokerString)) {
throw APIException.serverException(BROKER_CACHE_EXCEPTION);
}
String previousValue = BROKER_CACHE.getIfPresent(clusterArn);
BROKER_CACHE. put (clusterArn, brokerString);
return previousValue;
}

@Override
public ConcurrentMap<String, AwsSecrets> getSecretCache() {
return SECRETS_CACHE. asMap();
}

@Override
public AwsSecrets getSecret (String clusterArn) {
return
	iskeyPresent (clusterArn) ? SECRETS_CACHE.getIfPresent (clusterArn) : null;

}


@Override
public AwsSecrets setSecret(String clusterArn, AwsSecrets secrets) throws APIException {
if (!iskeyValuePresent (clusterArn, secrets)) {
throw APIException.serverException (SECRETS_CACHE_EXCEPTION);
}
AwsSecrets previousValue = SECRETS_CACHE.getIfPresent (clusterArn);
SECRETS_CACHE.put(clusterArn, secrets);
return previousValue;
}

@Override
public ConcurrentMap<String, KafkaTemplate<String, Object>> getkafkaTemplateCache() {
return KAFKATEMPLATE_CACHE.asMap();
}

@Override
public KafkaTemplate<String, Object> getkafkaTemplate(String clusterArn) {
return iskeyPresent (clusterArn)? KAFKATEMPLATE_CACHE.getIfPresent (clusterArn): null;
}


public KafkaTemplate<String,Object> setkafkaTemplate(String clusterArn, KafkaTemplate<String,Object> kafkaTemplate) throws APIException
	
{


if (!iskeyValuePresent (clusterArn, kafkaTemplate)) {
throw APIException.serverException (KAFKATEMPLATE_CACHE_EXCEPTION);
}
KafkaTemplate<String, Object> previousValue = KAFKATEMPLATE_CACHE.getIfPresent (clusterArn);
KAFKATEMPLATE_CACHE.put(clusterArn, kafkaTemplate);
return previousValue;

}
@Override
public List<DomainInfoResponse> getDomains() {
List<DomainInfoResponse> domains= DOMAINS_CACHE.getIfPresent(DEFAULT_DOMAINS_KEY);
if (domains == null) {
domains =new ArrayList<>();
}
return domains;
}
@Override
public List<DomainInfoResponse> setDomains(List<DomainInfoResponse> domains) {
List<DomainInfoResponse> previousValue= getDomains();
DOMAINS_CACHE. put (DEFAULT_DOMAINS_KEY, domains);
return previousValue;
}
@Override
public void evictAll() {
CLUSTER_CACHE. invalidateAll();
BROKER_CACHE. invalidateAll();
SECRETS_CACHE. invalidateAll();
KAFKATEMPLATE_CACHE.invalidateAll();
DOMAINS_CACHE. invalidateAll();
}
}