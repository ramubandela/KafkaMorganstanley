package com.paraport.kafkaapi.core.helper;

import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation. Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework. stereotype. Component;


import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.paraport.kafkaapi.core.cache.APICache;
//import com.paraport.kafkaapi.core.exception.APIException;
import com.paraport.kafkaapi.core.exception.APIException;
import com. paraport.kafkaapi.core.model.AwsSecrets;
//import lombok. extern.slf4j.slf4j;
import software.amazon. awssdk.services.kafka.KafkaClient;
import software.amazon.awssdk.services.kafka.model.ListScramSecretsRequest;
import software.amazon.awssdk.services.kafka.model.ListScramSecretsResponse;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;

//@slf4j
@Component
public class AwsSecretsHelper {

private static final String USERNAME= "username";
private static final String PASSWORD="password";
private static final String NO_SECRET_ARN_FOUND="Secret ARN not found";

@Autowired
@Qualifier ("caffeineCacheHelper")
APICache cache;
/*
 * Autowired KafkaClient awskafkaClient;
 */
@Autowired
KafkaClient awskafkaClient;

@Autowired
SecretsManagerClient secretsManagerClient;

public AwsSecrets getSecrets (String domain, String clusterArn) throws APIException {
//tog.info("Getting secrets for domain: {}", domain);
try {
AwsSecrets awsSecrets = cache.getSecret(clusterArn);
if (awsSecrets != null) {
	//Log.info("Secrets present in cache");
	return awsSecrets;
}

awsSecrets = getSecrets(clusterArn);
//Log.info("Setting Secrets in cache");
cache.setSecret(clusterArn, awsSecrets);
return awsSecrets;
} catch (APIException e) {
//Log.error("Kafka ApiException while getting secrets: ", e);
throw e;
}catch (Exception e) {
	// TODO: handle exception
	throw APIException.serverException();
}
}

public AwsSecrets getSecrets(String clusterArn) throws APIException {
String secretArn= getSecretsArn(clusterArn);
JsonObject jsonObject = getSecretsFromSts(secretArn);
isSecretsPresent(jsonObject);
return new AwsSecrets (jsonObject.get (USERNAME).getAsString(), jsonObject.get (PASSWORD).getAsString());
}
private String getSecretsArn(String clusterArn) throws APIException {
ListScramSecretsRequest secretsRequest = ListScramSecretsRequest.builder()
.clusterArn(clusterArn)
.build();
ListScramSecretsResponse listScramSecretsResponse = awskafkaClient.listScramSecrets(secretsRequest);
List<String> secretArnList = listScramSecretsResponse.secretArnList();
if (secretArnList.isEmpty()) {
throw APIException.badRequestException(NO_SECRET_ARN_FOUND);

}
return secretArnList.get(0);
}

private JsonObject getSecretsFromSts (String key) {
//tog.info("Getting secrets for key {}", key);
GetSecretValueRequest secretValueRequest = GetSecretValueRequest.builder().
secretId(key)
.build();
GetSecretValueResponse getSecretValueResponse = secretsManagerClient.getSecretValue(secretValueRequest);
return JsonParser.parseString(getSecretValueResponse. secretString()).getAsJsonObject();
}
private void isSecretsPresent (JsonObject jsonObject) throws APIException {
JsonElement usernameElement =jsonObject.get(USERNAME);
if (Objects.isNull(usernameElement)) {
//Log.info("Username is not present in the secrets");
throw APIException.serverException();
}
JsonElement passwordElement = jsonObject.get(PASSWORD);
if (Objects.isNull (passwordElement)) {
//Log.info("Password is not present in the secrets");
throw APIException.serverException();
}
}
}