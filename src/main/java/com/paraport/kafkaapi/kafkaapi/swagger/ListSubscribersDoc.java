package com.paraport.kafkaapi.kafkaapi.swagger;


import java.lang. annotation. ElementType;
import java.lang. annotation. Retention;
import java.lang. annotation. RetentionPolicy;
import java.lang. annotation. Target;
import io. swagger.v3.oas. annotations. Operation;
import io. swagger.v3.oas. annotations. tags.Tag;

@Target(value = { ElementType.ANNOTATION_TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Tag(name="subscriber")
@Operation (summary ="List all the Subscribers", description = "Lists all the topics which are subscribed along with their target domains and topics", operationId= "List subscribers", method="GET")
public @interface ListSubscribersDoc {

}
