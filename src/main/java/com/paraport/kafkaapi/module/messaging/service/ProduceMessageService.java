package com.paraport.kafkaapi.module.messaging.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import com.paraport.kafkaapi.core.exception.APIException;
import com.paraport.kafkaapi.core.helper.KafkaTemplateHelper;
import com.paraport.kafkaapi.core.model.Error;
import com.paraport.kafkaapi.module.messaging.model.ProducerResponse;
import com.paraport.kafkaapi.module.messaging.validation.ProduceMessageValidation;

@Service
public class ProduceMessageService {

	private static final int THREAD_POOL_SIZE = 3;
	private static final int AWAIT_TERMINATION_OF_EXECUTOR = 1000;
	@Autowired
	private KafkaTemplateHelper kafkaTemplateHelper;
	@Autowired
	private ProduceMessageValidation publishMessageValidation;
	

	public List<ProducerResponse> produceMessage(final Map<String, String> domainTopicMap, Integer partition,
			String key, final Object message, Integer acks) throws APIException {
		try {
			List<Callable<ProducerResponse>> tasks = new ArrayList();
			return sendParallelTaskExecution(domainTopicMap, partition, key, message, acks, tasks);
		} catch (Exception e) {
			// Log.error("Exception while producing messages ", e);
			throw APIException.serverException(e.getLocalizedMessage());
		}
	}

	private List<ProducerResponse> sendParallelTaskExecution (final Map<String, String> domainTopicMap,
	Integer partition, String key, final Object message, Integer acks,
	List<Callable<ProducerResponse>> tasks) {
	domainTopicMap.forEach((domain, topic) ->{
	//tog.info("domain and topic {}", domain, topic);
	Callable<ProducerResponse> publishTask = () -> {
	KafkaTemplate<String, Object> kafkaTemplate = kafkaTemplateHelper.getKafkaTemplate(domain, String.valueOf(acks));
	return sendMessage(kafkaTemplate, domain, topic, partition, key, message);
	};
	tasks.add(publishTask);
	
	});
	return sentEventConcurrently(tasks);
}

	public List<ProducerResponse> produceMessage(final List<String> listDomain, final String topicName,
			Integer partition, String key, final Object message, Integer acks) throws APIException {
		try {
			publishMessageValidation.validate(listDomain, topicName, message);
			Map<String, String> domainTopicMap = new HashMap<>();
			listDomain.stream().forEach(domain -> domainTopicMap.put(domain, topicName));
//Log.info("final domain to topic map will be :: {}", domainTopicMap);
			return produceMessage(domainTopicMap, partition, key, message, acks);
		} catch (APIException e) {
			throw e;
		} catch (Exception e) {
			throw APIException.serverException(e.getLocalizedMessage());
		}
	}

	private ProducerResponse sendMessage(KafkaTemplate<String, Object> kafkaTemplate, String domain, String topic,
			Integer partition, String key, Object message)
			throws InterruptedException, ExecutionException, APIException {
		try {
			ListenableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topic, partition, key, message);
			SendResult<String, Object> result = future.get();
			RecordMetadata metadata = result.getRecordMetadata();
//Log.info("Message Sent to topic: {}", topic);
			return ProducerResponse.builder().domain(domain).topic(topic).key(result.getProducerRecord().key())
					.partition(metadata.partition()).offset(metadata.offset()).build();
		} catch (Exception e) {
//Log.error("Exception occured while sending message ", e);
			final Error error = new Error();
			error.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			error.setMessage(e.getMessage());
			return ProducerResponse.builder().domain(domain).topic(topic).error(error).build();
		}

	}

	private List<ProducerResponse> sentEventConcurrently(List<Callable<ProducerResponse>> tasks) throws APIException {
//Log.info("Invoking {} tasks parallely", tasks.size());
		List<ProducerResponse> responseDtos = new ArrayList<>();
		try {
			List<Future<ProducerResponse>> futures = executeAllTasks(tasks);
			for (Future<ProducerResponse> future : futures) {
				if (future.isDone()) {
					ProducerResponse producerResponse = future.get();
					responseDtos.add(producerResponse);
				}
			}
		} catch (InterruptedException ie) {
//Log.error("Executors task was interrupted", ie);
			Thread.currentThread().interrupt();
			throw APIException.serverException(ie.getLocalizedMessage());
		} catch (ExecutionException ee) {
//Log.error("ExecutionException while getting publishResponse from future", ee);
			throw APIException.serverException(ee.getLocalizedMessage());
		}
		return responseDtos;

	}

private List<Future<ProducerResponse>> executeAllTasks(List<Callable<ProducerResponse>> tasks) throws APIException {
ExecutorService executorService = Executors.newFixedThreadPool (THREAD_POOL_SIZE);
try {
return executorService.invokeAll(tasks);
} catch (InterruptedException ie) {
//Log.error("Executors task was interrupted", ie);
Thread.currentThread().interrupt();
throw APIException.serverException (ie.getLocalizedMessage());
} finally {
executorService.shutdown();
try {
if (executorService. awaitTermination (AWAIT_TERMINATION_OF_EXECUTOR, TimeUnit.MILLISECONDS)) {
executorService. shutdownNow();
}
} catch (InterruptedException e) {
executorService. shutdownNow();
}
}
}
}