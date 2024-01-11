package com.paraport.kafkaapi.module.domain.model;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DomainAuthentication {
private Boolean saslScramEnabled;
private Boolean iamEnabled;
}