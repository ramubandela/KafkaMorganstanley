package com.paraport.kafkaapi.module.domain.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory. annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework. stereotype. Service;


import com.paraport.kafkaapi.core.cache.APICache;
import com.paraport.kafkaapi.core.exception.APIException;

import com.paraport.kafkaapi.module.domain.model. DomainInfoResponse;
//import lombok.extern.slf4j.slf4j;
import software.amazon.awssdk.services.kafka. KafkaClient;
import
software.amazon.awssdk.services.kafka.model.ListClustersResponse;

@Service
public class DomainInfoService {
	
		@Autowired
		private KafkaClient awskafkaClient;
		@Autowired
		@Qualifier("caffeineCacheHelper")
		private APICache cache;
		
		public DomainInfoResponse getDomainInformation (String domainName) throws APIException {
		//Log.info("Fetching domain info");
		DomainInfoResponse domainInfo = null;
		try {
		List<DomainInfoResponse> domainsInfo = cache.getDomains();
		if (domainsInfo.isEmpty()) {
		ListClustersResponse listClustersResponse = awskafkaClient.listClusters();
		domainsInfo = listClustersResponse. clusterInfoList().stream()
				.map(clusterInfo -> new DomainInfoResponse(clusterInfo.clusterName(),
				clusterInfo.creationTime(),
				clusterInfo.clientAuthentication(),
				clusterInfo.currenBrokerSoftwareInfo().kafkaversion(),
				clusterInfo. numberOfBrokerNodes (), clusterInfo.stateAsString()))
				.collect(Collectors.toList());
		
				//Log.info("Setting domains info cache");
				cache.setDomains (domainsInfo);
				for (DomainInfoResponse info: domainsInfo) {
				if (info.getClusterName().equals(domainName) || info.getClusterName().startsWith(domainName)) 
				{
				domainInfo info;
				}
				}
				if (domainInfo == null) {
				throw new Exception (domainName + " informat…not available");
				}
				return domainInfo;


		}catch (Exception e) {
			// TODO: handle exception
		}
}
