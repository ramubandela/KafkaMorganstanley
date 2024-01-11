FROM gitlab.paraport.com/parametric/dependency_proxy/containers/openjdk:14-slim
COPY . /tmp/app/

RUN mv /tmp/app/target  /usr/app/
RUN update-ca-certificates
RUN sh -c 'keytool  -importcert -keypass changeit -file /etc/ssl/certs/ca-certificates.crt -keystore /usr/local/openjdk-14/lib/security/cacerts -noprompt -storepass changeit'

RUN sh -ccp usr/local/openjdk-14/lib/security/cacerts
RUN rm -rf /tmp/app
/tmp/kafka.client.truststore.jks
EXPOSE 8080
WORKDIR /usr/app
RUN sh -c 'touch kafka-api-service-1.0.0. SNAPSHOT. jar
ENTRYPOINT ["java", "-Xms4196m", "-Xms4196m", "-jar", "kafka-api-service-1.0.0-SNAPSHOT.jar"]