package com.paraport.kafkaapi.module.topic.mapper;

import java.util.ArrayList;
import java.util.List;
import org.apache.kafka.clients.admin.TopicDescription;
import org.apache.kafka.common.Node;
import org.apache.kafka.common.TopicPartitionInfo;
import com.paraport.kafkaapi.module.topic.model.DescribeTopicResponse;
import com.paraport.kafkaapi.module.topic.model.TopicPartitionResponse;
import com.paraport.kafkaapi.module.topic.model.TopicReplicaResponse;

public class DescribeTopicMapper {

	public static DescribeTopicResponse getTopicResponse(TopicDescription topicDescription) {
		DescribeTopicResponse describeTopic = new DescribeTopicResponse();
		describeTopic.setTopicName(topicDescription.name());
		describeTopic.setInternalTopic(topicDescription.isInternal());
		List<TopicPartitionResponse> partitions = new ArrayList<>();

		topicDescription.partitions().forEach(p -> {
			partitions.add(getTopicPartition(p));
		});
		describeTopic.setPartitions(partitions);

		int noofPartitions = partitions.size();

		if (noofPartitions > 0) {
			describeTopic.setPartitionSize(noofPartitions);
			TopicPartitionResponse partitionResp = partitions.get(0);
			describeTopic.setReplicaSize(partitionResp.getReplicas().size());
		}
		return describeTopic;
	}

	public static TopicPartitionResponse getTopicPartition(TopicPartitionInfo partitionInfo) {
		TopicPartitionResponse topicPartition = new TopicPartitionResponse();
		topicPartition.setPartition(partitionInfo.partition());
		int leaderId = partitionInfo.leader().id();
		topicPartition.setLeader(leaderId);
		List<TopicReplicaResponse> replicas = new ArrayList<>();
		partitionInfo.replicas().forEach(r -> {
			replicas.add(getTopicReplicas(r, leaderId));
		});
		topicPartition.setReplicas(replicas);
		return topicPartition;
	}

	public static TopicReplicaResponse getTopicReplicas(Node node, Integer leaderId) {
		TopicReplicaResponse topicReplica = new TopicReplicaResponse();
		topicReplica.setBroker(node.host());
		topicReplica.setPort(node.port());
		boolean leader = node.id() == leaderId ? true : false;
		topicReplica.setLeader(leader);
		return topicReplica;
	}

}