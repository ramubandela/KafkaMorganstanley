package com.paraport.kafkaapi.module.subscriber.model;

import com. fasterxml.jackson. annotation.JsonIgnore;
import lombok. AllArgsConstructor;
import lombok.Builder;
import lombok. Data;
import lombok. Getter;
import lombok. NoArgsConstructor;
import lombok. NonNull;
import org.apache.commons.lang3.StringUtils;
import software.amazon.awssdk. enhanced. dynamodb.extensions. annotations. DynamoDbVersionAttribute;
import software.amazon.awssdk. enhanced. dynamodb.mapper.annotations. DynamoDbBean;
import software.amazon.awssdk. enhanced. dynamodb.mapper. annotations. DynamoDbPartitionKey;
import software.amazon.awssdk. enhanced.dynamodb.mapper. annotations. DynamoDbSortKey;

@DynamoDbBean
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Subscription {
private static final String PARTITION_KEY_FORMAT = "%s:%s";
@NonNull
private String producerDomain;
@NonNull
private String producerTopic;

@NonNull
@Getter(onMethod = @__({@DynamoDbSortKey}))
private String subscriberDomain;
@NonNull
private String subscriberTopic;
private long subscriptionEpochTimestamp;

@Getter(onMethod = @__({@DynamoDbVersionAttribute}))
@JsonIgnore
private Long version;

@DynamoDbPartitionKey
public String getProducerDomainTopic() {
if (StringUtils.isBlank (producerDomain) || StringUtils.isBlank (producerTopic)) {
return null;
}
return String.format(PARTITION_KEY_FORMAT, producerDomain, producerTopic);
}

/*
*NO-OP setter to avoid exception when DynamoDB deserialize the object.
*@param ignore ignored String
*/
public void setProducerDomainTopic (String ignore) {
}
/*Returns partition key used in the table based on given producer domain/topic
* @param producer Domain name of the domain
*@param producerTopic name of the topic
* @return partition key used in the table based on given producer domain/topic
*/
public static String createPartitionkey (@NonNull String producerDomain, @NonNull String producerTopic) {
return String.format(PARTITION_KEY_FORMAT, producerDomain, producerTopic);
}

}