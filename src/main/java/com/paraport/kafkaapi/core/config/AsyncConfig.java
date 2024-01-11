package com.paraport.kafkaapi.core.config;

import java.util.concurrent.Executor;
import org.springframework.context. annotation.Configuration;
import org.springframework.scheduling. annotation. AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

//annotation.EnableAsync;
@EnableAsync
@Configuration
public class AsyncConfig implements AsyncConfigurer {
	
	
	  private static final int MAX_POOL_SIZE =10;
	  private static final int  MAX_QUEUE_CAPACITY = 1000;
	 

@Override
public Executor getAsyncExecutor() {
ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
executor.setMaxPoolSize(MAX_POOL_SIZE);
executor.setQueueCapacity (MAX_QUEUE_CAPACITY);
executor.setThreadNamePrefix("KafkaAPIExecutor-");
executor.initialize();
return executor;

}
}