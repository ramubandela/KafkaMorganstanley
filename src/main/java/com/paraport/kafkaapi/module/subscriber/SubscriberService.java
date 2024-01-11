package com.paraport.kafkaapi.module.subscriber;

import java.util.List;
import java.util. Optional;
import org.apache.kafka.clients.admin. TopicDescription;
import org.springframework.beans.factory. annotation. Autowired;
import org.springframework. stereotype. Service;

import com.paraport.kafkaapi.core.helper.AwsKafkaHelper;

import com.paraport.kafkaapi.module.common.jpa.entity. Subscriber;
import com.paraport.kafkaapi.module.common.jpa.repository. SubscriberRepository;
import com. paraport.kafkaapi.module.topic.model.CreateTopicRequest;
import com.paraport.kafkaapi.module.topic.service.CreateTopicService;
import com.paraport.kafkaapi.module.topic.service. TopicService;
@Service
public class SubscriberService {

@Autowired
private SubscriberRepository subscriberRepository;
@Autowired
private TopicService topicService;
@Autowired
private CreateTopicService createTopicService;
@Autowired
private AwsKafkaHelper awskafkaHelper;
public Optional<Subscriber> getSubscriberById(Long accountId) {
return subscriberRepository. findById(accountId);
}

public Subscriber addSubscriber (Subscriber subscriber) {
boolean isTopicExists=topicService.isTopicExists(subscriber.getProducerTopicName(), subscriber.getProducerDomainName());
if(isTopicExists == false) {
CreateTopicRequest createTopicRequest = new CreateTopicRequest();
createTopicRequest.setTopic (subscriber.getProducerTopicName());
createTopicService. createTopic (createTopicRequest, awskafkaHelper.getkafkaConnectionForDomain (subscriber.getProducerDomainName()));
}
TopicDescription topicDescription = topicService.getTopicDetails (subscriber.getProducerTopicName(), subscriber.getProducerDomainName());
CreateTopicRequest createTopicRequest = new CreateTopicRequest();
createTopicRequest.setTopic (subscriber.getProducerTopicName());

createTopicRequest.setPartitions (topicDescription. partitions().size());
createTopicRequest.setReplicas ((short) topicDescription. partitions().get (0).replicas ().size());
createTopicService.createTopic (createTopicRequest, awskafkaHelper.getkafkaConnectionForDomain(subscriber.getSubscriberDomainName()));
return subscriberRepository.save(subscriber);
}
public List<Subscriber> getSubscribers() {
return subscriberRepository. findAll();

}
}