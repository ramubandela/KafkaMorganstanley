package com.paraport.kafkaapi.module.domain.model;

import java.time. Instant;
import lombok. AllArgsConstructor;
import lombok. Data;
import lombok. NoArgsConstructor;
import software.amazon.awssdk.services.kafka.model.ClientAuthentication;
import software.amazon.awssdk.services.kafka.model.Sasl;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class DomainInfoResponse {
private String clusterName;
private Instant creationTime;
private  DomainAuthentication authentication;
private String kafkaversion;

private Integer numberOfBrokerNodes;
private String state;
public DomainInfoResponse(String clusterName, Instant creationTime, ClientAuthentication clientAuthentication,
String kafkaversion, Integer numberOfBrokerNodes, String state) {
super();
this.clusterName = clusterName;
this.creationTime = creationTime;
this.kafkaversion= kafkaversion;
this. numberOfBrokerNodes = numberOfBrokerNodes;
this.state = state;
Sasl sasl= clientAuthentication.sasl();
this. authentication = new DomainAuthentication(sasl.scram().enabled(), sasl.iam().enabled());
	
}

}