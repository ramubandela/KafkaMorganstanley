package com.paraport.functionLambda;

public class FunctionExample {

	public class CreateTopicFunction implements RequestHandler<APIGatewayProxy
	  RequestEvent, APIGatewayProxyResponseEvent> {

private static LambdaLogger  Log; 
	 
private static final String DOMAIN_NAME = "domainName"; 

private static	  final Integer DEFAULT PARTIONS = 3; 

private static final Short DEFAULTREPLICAS = 1; 
private static final Gson gson = new GsonBuilder().create();
	  private final AWSKafka awskafkaClient = AWSKafkaClient
	  Builder.standard().withRegion (Regions. US_EAST 1).build(); 
	  private final AWSSecretsManager awsSecretManager Client = AWSSecretsManager Client
	  Builder.standard() withRegion (Regions.US_EAST 1).build();
	  
	  private SecretsManager ausSecretsMgr; 
	  private MSKBrokersManager mskBrokersMgr; 
	public  CreateTopicFunction (){
	   super(); 
	this.awssecretsMgr = new SecretsManager (aus Secret Manager Client, awskafkaClient);
	  this.mskBrokersMgr = new  MSKBrokersManager (awskafkaClient);
	 
	  
	  @Override 
	public APIGatewayProxyResponseEvent handleRequest (APIGateway
	  ProxyRequest Event httpRequest, Context context) { Log = context.getLogger();
	  create topic kafka request");

}
	/*
	 * public class CreateTopicFunction implements RequestHandler<APIGateway Proxy
	 * Request Event, APIGateway Proxy ResponseEvent> private static LambdaLogger
	 * Log; private static final String DOMAIN_NAME = "domainName"; private static
	 * final Integer DEFAULT PARTIONS = 3; private static final Short DEFAULT
	 * REPLICAS = 1; private static final Gson gson = new GsonBuilder ().create();
	 * private final AWSKafka awskafkaClient = AWSKafkaClient
	 * Builder.standard().withRegion (Regions. US_EAST 1).build(); private final
	 * AWSSecretsManager awsSecretManager Client = AWSSecretsManager Client
	 * Builder.standard() withRegion (Regions.US_EAST 1).build(); private
	 * SecretsManager ausSecretsMgr; private MSKBrokersManager mskBrokersMgr; public
	 * CreateTopicFunction () { super(); this.awssecretsMgr = new SecretsManager
	 * (aus Secret Manager Client, awskafkaClient); this.mskBrokersMgr = new
	 * MSKBrokersManager (awskafkaClient);
	 * 
	 * @Override public APIGateway Proxy ResponseEvent handleRequest (APIGateway
	 * ProxyRequest Event httpRequest, Context context) { Log = context.getLogger();
	 * create topic kafka request");
	 */
}
