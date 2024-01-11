package com.paraport.kafkaapi.module.subscriber;

import com.paraport.kafkaapi.module.subscriber.model.Subscription;
import com.paraport.kafkaapi.module.topic.model.TopicDetails;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.NonNull;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscriberResponse {
@NonNull
ResponseCode responseCode;
Subscription subscription;
public enum ResponseCode {
SUCCESS,
SUBSCRIB_ON_SAME_DOMAIN,
TOPIC_DOES_NOT_EXIST,
ALREADY_SUBSCRIBED,
}

public static SubscriberResponse subscribeOnSameDomain() {
return SubscriberResponse.builder()
.responseCode (ResponseCode. SUBSCRIB_ON_SAME_DOMAIN)
.build();
}
public static SubscriberResponse topicDoesNotExist() {
return SubscriberResponse.builder()
.responseCode(ResponseCode.TOPIC_DOES_NOT_EXIST)
.build();
}
public static SubscriberResponse alreadySubscribed() {
return SubscriberResponse.builder()
.responseCode (ResponseCode.ALREADY_SUBSCRIBED)
.build();
}
public static SubscriberResponse success (Subscription subscription) {
return SubscriberResponse.builder()
.responseCode (ResponseCode. SUCCESS)
.subscription (subscription)
.build();
}
}
