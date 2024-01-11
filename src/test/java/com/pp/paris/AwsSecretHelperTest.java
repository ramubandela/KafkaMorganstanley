package com.pp.paris;

import static org. junit.jupiter.api. Assertions.assertEquals;
import static org. junit.jupiter.api. Assertions. assertThrows;
import static org.mockito.Mockito.when;
import java.util.List;
import org. junit.jupiter.api. BeforeEach;
import org. junit.jupiter.api. Test;
import org. junit. jupiter.api.extension. ExtendWith;
import org.mockito. InjectMocks;
import org.mockito.Mock;
import org.mockito. Mockito;
import org.springframework.test.context.junit.jupiter. SpringExtension;
import com.paraport.kafkaapi.core.cache. APICache;
import com. paraport.kafkaapi.core.exception. APIException;
import com.paraport.kafkaapi.core.helper. AwsSecretsHelper;
import com.paraport.kafkaapi.core.model. AwsSecrets;
import software.amazon.awssdk.services.kafka. KafkaClient;
import software.amazon.awssdk.services.kafka.model.ListScramSecretsRequest;
import software.amazon.awssdk.services.kafka.model.ListScramSecretsResponse;
import software.amazon.awssdk.services.secretsmanager. SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;

@ExtendWith (SpringExtension.class)
class AwsSecretHelperTest {

	@Mock
	APICache cache;
	
	@Mock
	KafkaClient awskafkaClient;
	
	@Mock
	SecretsManagerClient secretsManagerClient;
	@InjectMocks
	AwsSecretsHelper secretsHelper;
	
	@BeforeEach
	void setup() {
	cache.evictAll();
	}
	
	@Test
	void testGetSecrets() throws APIException {
	ListScramSecretsResponse listScramSecretsResponse = ListScramSecretsResponse.builder()
	.secretArnList (List.of ("secretArn"))
	.build();
	when (awskafkaClient.listScramSecrets(Mockito. any(ListScramSecretsRequest.class)))
	.thenReturn(listScramSecretsResponse);
	GetSecretValueResponse getSecretValueResponse = GetSecretValueResponse.builder().
	secretString("{\"username\":\"username\",\"password\":\"password\"}")
	.build();
	when (secretsManagerClient.getSecretValue (Mockito. any (GetSecretValueRequest.class))).
	thenReturn (getSecretValueResponse);
	AwsSecrets secrets = new AwsSecrets("username", "password");
	assertEquals(secrets, secretsHelper.getSecrets("domain", "cluster Arn"));

}

	@Test
	void testGetSecrets_CacheHit() throws APIException {
	AwsSecrets secrets = new AwsSecrets("username", "password");
	when(cache.getSecret (Mockito.anyString())).thenReturn (secrets);
	assertEquals(secrets, secretsHelper.getSecrets("domain", "cluster Arn"));
	}
	@Test
	void testGetSecretsNoSecretARNFound() throws APIException {
	ListScramSecretsResponse listScramSecretsResponse = ListScramSecretsResponse.builder()
	.build();
	when (awskafkaClient.listScramSecrets (Mockito. any (ListScramSecretsRequest.class)))
	.thenReturn (listScramSecretsResponse);
	APIException apiException = assertThrows(APIException.class,
	() -> secretsHelper.getSecrets("domain", "cluster Arn"));
	
	assertEquals(400, apiException.getError().getCode());
	
	}
}