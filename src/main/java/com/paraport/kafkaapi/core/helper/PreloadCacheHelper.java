package com.paraport.kafkaapi.core.helper;
import java.util. Collection;
import java.util.List;
import java.util.concurrent. ConcurrentMap;
import javax. annotation. Post Construct;
import org.springframework.beans.factory. annotation. Autowired;
import org.springframework.beans.factory. annotation. Qualifier;
import org.springframework.stereotype.Component;
import com.paraport.kafkaapi.core.cache. APICache;
import com.paraport.kafkaapi.core.exception. APIException;
import com. paraport.kafkaapi.core.model. AwsSecrets;
import
import
import
MS1f41
lombok.extern.slf4j.slf4j;
software.amazon.awssdk.services.kafka.model.ClusterInfo;
software.amazon.awssdk.services.kafka.model. ListClustersRequest;

@Component
public class PreloadCacheHelper {
@Autowired
@Qualifier ("caffeineCacheHelper").
APICache cache;
@Autowired
AwskafkaHelper awskafkaHelper;
@Autowired
AwsSecretsHelper awsSecretsHelper;
@PostConstruct
public String preloadCache() {
Log.info("Loading Cluster Cache");
int clusterCacheSize = preloadClusterCache();
int brokerCacheSize = preload BrokerCache();
int secretCacheSize = preloadSecretCache();
String, cachePreloadedMsg = String.format("%s clusters, %s brokers and %s_preloaded", cluster CacheSize,
brokerCacheSize, secretCacheSize);
tog.info(cachePreloadedMsg);
return cachePreloadedMsg;

public int preloadClusterCache() {
try {
}
List<Cluster Info> cluster Infos auskafkaHelper.getClusters (List ClustersRequest.buttder().build(), true);
cluster Infos.stream().forEach(info -> {
try {
cache.setCluster (info.cluster Name(), info.cluster Arn());
} catch (APIException e) {
Log.error("Error while preloading cluster cache: {}", e.getLocalizedMessage());
}
});
} catch (APIException el) {
Log.error("Error while preloading cluster cache: {}", el.getLocalizedMessage());
return cache.getClusterCache().size();
public int preloadBrokerCache() {
ConcurrentMap<String, String> clusterCache = cache.getClusterCache();
Collection<String> preloaded Clusters = clusterCache.values();
if (preloaded Clusters.isEmpty()) {
} else {
}
Log.error("Clusters are not preloaded");
preloadedClusters.stream().forEach(arn -> {
String broker = awskafkaHelper.getBrokers (arn);
try {
});
cache.setBroker (arn, broker);
} catch (APIException e) {
Log.error("Error while preloading broker cache: {}", e.getLocalizedMessage());
}
return cache.getBrokerCache().size();
public int preloadSecretCache() {
ConcurrentMap<String, String> clusterCache = cache.getClusterCache();
Collection<String> preloaded Clusters = clusterCache.values();
if (preloaded Clusters.isEmpty()) {
tog.error("Clusters are not preloaded");
} else {
preloaded Clusters.stream().forEach(arn -> {
try {
AwsSecrets awsSecrets = awsSecretsHelper.getSecrets (arn);
cache.setSecret (arn, awsSecrets);
} catch (APIException e) {
tog.error("Error while preloading secrets cache: {}", e.getLocalizedMessage());
return cache.getSecretCache().size();
}
}
