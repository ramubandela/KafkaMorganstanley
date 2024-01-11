package com.paraport.kafkaapi.module.topic.validation;


import org.apache.commons.lang3.StringUtils;
import org.springframework. stereotype.Component;
import com. paraport.kafkaapi.core.exception. APIException;
@Component
public class ListTopicsValidation {
public void validateRequest(String domain) throws APIException {
if (StringUtils.isBlank (domain)) {
throw APIException.badRequestException ("Field 'domain is required.");
}
}
}