package com.paraport.kafkaapi.module.common.jpa.repository;

import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework. data.jpa.repository.Query;
import org.springframework. data.repository.query.Param;
import com. paraport.kafkaapi.module.common.jpa.entity.Subscriber;
public interface SubscriberRepository extends JpaRepository<Subscriber, Long> {
@Query(value = "SELECT sub FROM Subscriber sub WHERE sub, producerTopicName = :topicName AND sub.producer DomainName in domains AND sub.subscriptionstatus= true")
List<Subscriber> findByTopicAndDomains (@Param("topicName") String topicName, @Param("domains") String domains);

}
