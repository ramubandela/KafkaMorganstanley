package com.paraport.kafkaapi.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.kafka.KafkaClient;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;


@Configuration
public class AwsClientsConfig {
Region region=Region.US_EAST_1;

@Bean
public SecretsManagerClient secretsManagerciient()
{
return SecretsManagerClient.builder().region(region).build();
}

@Bean
public KafkaClient awskafkaclient() {
return KafkaClient.builder().
region(region)
.build();
}

}