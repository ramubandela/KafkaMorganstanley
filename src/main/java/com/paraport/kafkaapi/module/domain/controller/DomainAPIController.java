package com.paraport.kafkaapi.module.domain.controller;

import java.util.List;
import org.springframework.beans.factory. annotation. Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http. ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation. PathVariable;
import org.springframework.web.bind.annotation. RequestMapping;
import org.springframework.web.bind.annotation. RestController;


import com. paraport.kafkaapi.core.exception.APIException;
import com. paraport.kafkaapi.module.domain.DomainHelper;
import com. paraport.kafkaapi.module.domain.model.DomainInfoResponse;  
import com.paraport.kafkaapi.module.domain.service.DomainInfoService;
import com.paraport.kafkaapi.module.domain.service.ListDomainsService;
/*import com.paraport.kafkaapi.swaggerdoc.DomainInfoDoc;
import com.paraport.kafkaapi.swaggerdoc.ListDomainsDoc;*/
//import lombok.extern.slf4j.slf4j;



@RestController
@RequestMapping("/v1")
public class DomainAPIController {
@Autowired
private ListDomainsService domainsService;
@Autowired
private DomainHelper producerHelper;
@Autowired
private DomainInfoService domainInfoService;

//@ListDomainsDoc
@GetMapping("/domains")
public ResponseEntity<List<String>> fetchListOfDomains () throws APIException {
//tog.info("listing all domains");
List<String> apiResponse = domainsService.getListofDomains();
return ResponseEntity.status(HttpStatus.OK). body (apiResponse);
}
//@DomainInfoDoc
@GetMapping("/domains/domainName}")
public ResponseEntity<DomainInfoResponse> getDomainInformation (@PathVariable String domainName) throws APIException {
//Log.info("fetching domain Information for ", domainName);
String originalDomainName = producerHelper.getoriginalDomainName(domainName);
DomainInfoResponse domainInformation =domainInfoService.getDomainInformation (originalDomainName);
return ResponseEntity.status(HttpStatus.OK). body (domainInformation);
}
}