package com.paraport.kafkaapi.module.subscriber.dao;

import com.paraport.kafkaapi.module.subscriber.model.Subscription;
import lombok.NonNull;
import java.util.List;
	

public interface SubscriptionDao {
Subscription getSubscription (@NonNull String producerDomain, @NonNull String producerTopic, @NonNull String subscriberDomain);
List<Subscription> getSubscriptions(@NonNull String producerDomain, @NonNull String producerTopic);
List<Subscription> getAllSubscriptions();
void saveSubscription (@NonNull Subscription subscription);
void deleteSubscription (@NonNull Subscription subscription);


}
