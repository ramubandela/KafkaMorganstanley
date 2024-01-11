package com.paraport.kafkaapi.module.subscriber.dao;

import com.paraport.kafkaapi.module.subscriber.model.Subscription;
import lombok. NonNull;
import lombok. RequiredArgsConstructor;
import org.springframework.beans.factory. annotation. Autowired;
import org.springframework. stereotype. Component;

import software.amazon.awssdk. enhanced. dynamodb. DynamoDbTable;
import software.amazon. awssdk. enhanced. dynamodb.Key;
import software.amazon.awssdk. enhanced. dynamodb.model.PageIterable;
import software.amazon.awssdk. enhanced. dynamodb.model.QueryConditional;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component("DynamoDbSubscriptionDao")
public class DynamoDbSubscriptionDao implements SubscriptionDao {
	
	
	
private final DynamoDbTable<Subscription> dynamoDbTable;

public Subscription getSubscription (@NonNull String producerDomain, @NonNull String producerTopic, @NonNull String subscriberDomain) {
Key partitionAndRangeKey= Key.builder()
.partitionValue(Subscription.createPartitionkey (producerDomain, producerTopic))
.sortValue(subscriberDomain)
.build();
return dynamoDbTable.getItem(partitionAndRangeKey);

}

@Override
public List<Subscription> getSubscriptions (@NonNull String producerDomain, @NonNull String producerTopic) {
	
Key partitionkey =Key.builder().partitionValue(Subscription.createPartitionkey (producerDomain, producerTopic)).build();
QueryConditional queryConditional = QueryConditional.keyEqualTo( partitionkey);
PageIterable<Subscription> pageIterable= dynamoDbTable.query(queryConditional);
return pageIterable. items().stream().collect (Collectors.toList());
}
@Override
public List<Subscription> getAllSubscriptions() {
PageIterable<Subscription> pageIterable=dynamoDbTable.scan();
return pageIterable. items ().stream().collect (Collectors.toList());
}
@Override
public void saveSubscription (@NonNull Subscription subscription) {
dynamoDbTable.putItem(subscription);
}
@Override
public void deleteSubscription (@NonNull Subscription subscription) {
dynamoDbTable. deleteItem(subscription);
}

}