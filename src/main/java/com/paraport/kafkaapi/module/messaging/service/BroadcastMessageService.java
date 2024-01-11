package com.paraport.kafkaapi.module.messaging.service;

import java.util. HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory. annotation. Autowired;
import org.springframework.stereotype. Service;
import org.springframework.util.CollectionUtils;
import com.paraport.kafkaapi.core.exception. APIException;
import com. paraport.kafkaapi.module.common.jpa.entity. Subscriber;
import com.paraport.kafkaapi.module.common.jpa.repository. SubscriberRepository;
import com.paraport.kafkaapi.module.messaging.model. ProducerResponse;
import lombok. NonNull;
//import lombok.extern.slf4j.slf4j;
//@slf4j
@Service
public class BroadcastMessageService {

	@Autowired
	private ProduceMessageService produceMessageService;
	@Autowired
	private SubscriberRepository repository;
	
	/* 
	 * /**
* Send the message to the given topic present in given domain. Also, check the
subscriber table for any subscriptions and broadcast the messages to those
topics too.
@param domain
* @param topic
The producer domain
The producer topic
The partition number to which message needs to be sent for
all the topics
The key that should be used to send the message.
@param key
@param event
The event that needs be sent. As of now, this should a 350N
The value that is used to set the "acks" Producer property
The event that needs be sent. As of now, this should a 350N
The value that is used to set the "acks" Producer property
@return (@link List<Producer Response>) List of (@link Producer Response)
@param acks
@param partition


	 * 
	 * */
	
	public List<ProducerResponse> broadcastMessages (@NonNull String domain, @NonNull String topic,
			Integer partition, String key, @NonNull final Object event, final Integer acks) throws APIException {
			//Log.info("Broadcasting messages for topic () in domain ()", topic, domain);
			Map<String, String> domainTopicMap = getSubscribers(domain, topic);
			domainTopicMap.put(domain, topic);
			//Log.info("Broadcasting message to { topics", domainTopicMap.size());
			return produceMessageService.produceMessage(domainTopicMap, partition, key, event, acks);

}

/**
 * Get the subscriptions for the given topic.
 * 
 * @param domain The domain name where the producer topic is present
 * @param topic  The topic for which Subscribers need to be found.
 * @return (@link Map) The map with subscribed domain as the key and subscribed
 *         topic as value
 */


	private Map<String, String> getSubscribers (String domain, String topic) {
		List<Subscriber> subscribers=repository. findByTopicAndDomains(topic, domain);
		Map<String, String> subDomainTopicMap = new HashMap<>();
		if (!CollectionUtils.isEmpty (subscribers)) {
		//tog.debug("Subscribers present for topic {} and domain {}", topic, domain);
		
		subDomainTopicMap = subscribers.stream().collect(
		Collectors.toMap (Subscriber::getSubscriberDomainName, Subscriber::getSubscriberTopicName));
		}
		return subDomainTopicMap;
}
	}
