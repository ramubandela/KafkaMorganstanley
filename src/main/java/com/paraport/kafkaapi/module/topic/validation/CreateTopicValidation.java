package com.paraport.kafkaapi.module.topic.validation;


import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype. Component;
import com.paraport.kafkaapi.core.exception. APIException;
import com.paraport.kafkaapi.module.topic.model.CreateTopicRequest;

@Component
public class CreateTopicValidation {
private static final int MAXIMUM_NUMBER_OF_PARTITIONS = 10;

public void validateRequest(CreateTopicRequest topicReq) throws APIException {
if (Objects.isNull(topicReq)) {
throw APIException.badRequestException ("Request body is missing");
}
if (StringUtils.isBlank (topicReq. getDomain())) {
throw APIException.badRequestException ("Domain is missing in request body");
}
if (StringUtils.isBlank (topicReq.getTopic())) {
throw APIException.badRequestException ("Topic is missing in request body");
}

if (topicReq.getPartitions() != null ) {
if (topicReq.getPartitions() <= 0) {
throw APIException.badRequestException ("Topic partitions should be greater than zero");
}
if (topicReq.getPartitions() > MAXIMUM_NUMBER_OF_PARTITIONS) {
throw APIException.badRequestException ("Topic partitions shouldn't be greater than ten");
}
}
if (topicReq.getReplicas () != null && topicReq.getReplicas () <= 0) {
throw APIException.badRequestException ("Topic replicas should be greater than zero");
}
}
}