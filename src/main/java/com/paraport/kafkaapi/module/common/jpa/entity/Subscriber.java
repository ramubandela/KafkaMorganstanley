package com.paraport.kafkaapi.module.common.jpa.entity;

import java.time.LocalDate;

import javax. persistence. Entity;
import javax. persistence. Id;
import javax. persistence. Table;
import lombok. AllArgsConstructor;
import lombok.Builder;
import lombok. Data;
import lombok. NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name="subscriber")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Subscriber {
@Id
private Long id;
private String producerTopicName;
private String producerDomainName;
private String subscriberDomainName;
private String subscriberTopicName;
private LocalDate subscriptionDate;
private Boolean subscriptionStatus;



}
