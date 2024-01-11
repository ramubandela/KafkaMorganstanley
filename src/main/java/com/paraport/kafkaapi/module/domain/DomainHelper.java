package com.paraport.kafkaapi.module.domain;

import java.util.ArrayList;
import java.util. HashMap;
import java.util.List;
//import
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.Collectors;
import org.springframework.beans.factory. annotation. Autowired;
import org.springframework.core.env. Environment;
import org.springframework. stereotype. Component;
//import lombok. extern.slf4j.slf4j;

@Component
	
	public class DomainHelper {
		@Autowired
		private Environment env;
		public String getoriginalDomainName(String shortName) {
		String originalName = env.getProperty("cluster.name.mapping." + shortName, shortName);
		//tog.info("original domain name: } for short name: [", originalName, shortName);
		return originalName;
		}
		public List<String> getoriginalDomainNames (List<String> domains) {
		List<String> originalDomains = new ArrayList<>();
		for(String domain: domains)
		originalDomains.add(getoriginalDomainName (domain)) ;
		//Log.info("original domain names: {} for short names: {}", originalDomains, domains);
		return originalDomains;
		}
		public List<String> getoriginalDomainNames (String commaSeparatedDomains) {
		List<String> listofDomains = getDomainListFromCommaSeperatedDomains (commaSeparatedDomains);
		return getoriginalDomainNames (listofDomains);
		}
		public List<String> getDomainListFromCommaSeperatedDomains (String domains) {
		return Stream. of (domains.split(",")).map (String::trim).collect(Collectors.toList());
		}
		
		public HashMap<String, String> getDomainShortAndFullNames () {
			HashMap<String, String> map = new HashMap<>();
			map.put("msk-inv-index-platform-cluster-dev", "index");
			map.put("msk-inv-ops-cluster-dev", "ops");
			map.put("msk-inv-eica-cluster-dev", "eica");
			map.put("msk-inv-trd-cluster-dev", "trd");
			map.put("msk-inv-indx-platform-cluster-dev", "indx");
			map.put("msk-inv-test-cluster-dev", "test");
			return map;
		}
}
