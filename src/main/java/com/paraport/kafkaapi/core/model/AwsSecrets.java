package com.paraport.kafkaapi.core.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class AwsSecrets {

@Getter
private String username;
@Getter
private String password;

}