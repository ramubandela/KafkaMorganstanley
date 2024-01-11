package com.paraport.kafkaapi.module.domain.service;

import java.util. HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation. Autowired;
import org.springframework.beans.factory.annotation. Qualifier;
import org.springframework. stereotype.Service;



import com.paraport.kafkaapi.core.cache.APICache;
import com.paraport.kafkaapi.core.exception. APIException;
import com.paraport.kafkaapi.module.domain. DomainHelper;
import com.paraport.kafkaapi.module.domain.model.DomainInfoResponse;
//import lombok. extern.slf4j.slf4j;
import software.amazon.awssdk.services.kafka.KafkaClient;
import
software.amazon.awssdk.services.kafka.model.ListClustersResponse;

@Service
public class ListDomainsService {
@Autowired
private KafkaClient awskafkaClient;
@Autowired
@Qualifier("caffeineCacheHelper")
private APICache cache;
@Autowired
private DomainHelper helper;

public List<String> getListofDomains() throws APIException {
//Log.info("Fetching list of domains");
try {
List<String> domainNames = null;
List<DomainInfoResponse> domainsInfo=cache.getDomains();
if (domainsInfo.isEmpty()) {
ListClustersResponse listClustersResponse= awskafkaClient. listClusters();
domainsInfo =listClustersResponse.clusterInfoList().stream() //clusterInfolist()
.map(clusterInfo -> new DomainInfoResponse(clusterInfo.clusterName(), clusterInfo.creationTime(),
clusterInfo.clientAuthentication(),
clusterInfo. currentBroker.SoftwareInfo().kafkaversion (),
clusterInfo.numberOfBrokerNodes (), clusterInfo, stateAsString()))
.collect(Collectors.toList());
//ache.setDomains();
//tog.info("Setting domains cache");
cache.setDomains (domainsInfo);
}
HashMap<String, String> map = helper.getDomainShortAndFullNames();
domainNames =domainsInfo.stream().map(domainInfo->map.get (domainInfo.getClusterName())+":"+domainInfo.getClusterName()).collect (Collectors. toList());
//Log.info("Total number of domains (", domainNames.size());
return domainNames;
} catch (Exception ex) {
//Log.error("Exception occured while fetching list domain ", ex);
throw APIException.serverException (ex.getLocalizedMessage());
}
}

}