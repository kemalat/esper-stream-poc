package de.oriontec.esper.app.config;


import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "engine")
public class AppConfig {

  @Value("${esper.name}")
  private String name;
  @Value("${esper.sdk.name}")
  private String sdkName;
  @Value("${esper.sdk.version}")
  private String sdkVersion;
  @Value("${event.listener.port}")
  private int eventListenerPort;
  @Value("${event.listener.ip}")
  private String eventListenerIp;
  @Value("${esper.jdbc.enabled}")
  private boolean esperJdbcEnabled;
  @Value("${esper.jdbc.port}")
  private int esperJdbcPort;
  @Value("${esper.jdbc.processorCount}")
  private int esperJdbcProcessorCount;
  @Value("${esper.jdbc.sessionIdleTimeout}")
  private int esperJdbcSessionIdleTimeout;
  @Value("${esper.kafka.enabled}")
  private boolean kafkaEnabled;
  @Value("${esper.kafka.bootstrap.servers}")
  private String kafkaBootstrapServers;
  @Value("${esper.kafka.consumer.group.id}")
  private String kafkaConsumerGroupId;
  @Value("${esper.kafka.consumer.client.id}")
  private String kafkaConsumerClientId;
  @Value("${esper.kafka.consumer.offset}")
  private String kafkaConsumerOffset;
  @Value("${esper.kafka.topics}")
  private String kafkaTopics;

  @Value("${esper.worker.inboundThreadSize}")
  private int workerInboundThreadSize;
  @Value("${esper.worker.outboundThreadSize}")
  private int workerOutboundThreadSize;

  @Value("${statement.audit.enabled}")
  private boolean auditEnabled;

}
