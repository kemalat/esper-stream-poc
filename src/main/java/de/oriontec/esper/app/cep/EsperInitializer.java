package de.oriontec.esper.app.cep;

import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esperio.kafka.EsperIOKafkaConfig;
import com.espertech.esperio.kafka.EsperIOKafkaInputAdapter;
import com.espertech.esperio.kafka.EsperIOKafkaInputSubscriberByTopicList;
import de.oriontec.esper.app.Main;
import de.oriontec.esper.app.config.AppConfig;
import de.oriontec.esper.app.event.ClockEvent;
import de.oriontec.esper.app.event.KafkaProcessor;
import java.util.Properties;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class EsperInitializer {

  private static final Logger logger = LoggerFactory.getLogger(EsperInitializer.class);
  private static EsperInitializer instance;
  private final AppConfig appConfig;

  private EsperIOKafkaInputAdapter kafkaInputAdapter = null;
  private EPServiceProvider engine;


  /**
   * Constructor for CEP Engine. Initializes engine configuration and loads statements.
   */
  public EsperInitializer() {
    appConfig = Main.getApplicationContext().getBean(AppConfig.class);
    initializeEngine();
  }

  /**
   * The method creates an instance of this class and returns it. If exists returns itself.
   *
   * @return an instance of a CEP engine.
   */
  public static synchronized EsperInitializer getInstance() {
    if (instance == null) {
      instance = new EsperInitializer();
    }
    return instance;
  }

  /**
   * The method initializes the Esper engine.
   */
  private void initializeEngine() {
    this.engine = EPServiceProviderManager.getProvider(appConfig.getName(), configureEsper());
  }

  public void initModule() {
    if (appConfig.isKafkaEnabled()) {
      initializeKafkaInputAdapter();
    }
  }

  private void initializeKafkaInputAdapter() {
    kafkaInputAdapter = new EsperIOKafkaInputAdapter(configureKafka(), engine.getURI());
    kafkaInputAdapter.start();
  }

  private Properties configureKafka() {
    //Kafka server and Kafka Input Adapter Properties
    Properties kafkaProps = new Properties();
    kafkaProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, appConfig.getKafkaBootstrapServers());
    kafkaProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
    kafkaProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
    kafkaProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, appConfig.getKafkaConsumerOffset());
    kafkaProps.put(ConsumerConfig.GROUP_ID_CONFIG, appConfig.getKafkaConsumerGroupId());
    kafkaProps.put(ConsumerConfig.CLIENT_ID_CONFIG, appConfig.getKafkaConsumerClientId());
    kafkaProps.put(EsperIOKafkaConfig.INPUT_SUBSCRIBER_CONFIG, EsperIOKafkaInputSubscriberByTopicList.class.getName());
    kafkaProps.put(EsperIOKafkaConfig.TOPICS_CONFIG, appConfig.getKafkaTopics());
    kafkaProps.put(EsperIOKafkaConfig.INPUT_PROCESSOR_CONFIG, KafkaProcessor.class.getName());
    return kafkaProps;
  }

  private Configuration configureEsper() {
    Configuration config = new Configuration();
    config.addImport(Helper.class);
    config.getEngineDefaults().getThreading().setEngineFairlock(true);
    config.getEngineDefaults().getThreading().setThreadPoolInbound(true);
    config.getEngineDefaults().getThreading()
        .setThreadPoolInboundNumThreads(appConfig.getWorkerInboundThreadSize());
    config.getEngineDefaults().getThreading().setThreadPoolOutbound(true);
    config.getEngineDefaults().getThreading()
        .setThreadPoolOutboundNumThreads(appConfig.getWorkerOutboundThreadSize());
    config.getEngineDefaults().getThreading().setListenerDispatchPreserveOrder(false);
    config.getEngineDefaults().getThreading().setInsertIntoDispatchPreserveOrder(false);
    config.addEventType(ClockEvent.class);
    return config;
  }

  /**
   * The method returns the EPServiceProvider instance of Esper engine. It can later be used to get the actual engine instance, and do some
   * stuff (such as send events, modify configuration).
   *
   * @return the EPServiceProvider instance of Esper engine.
   */
  public EPServiceProvider getEngine() {
    return engine;
  }

  /**
   * The method shutdowns the Esper engine.
   */
  public void shutdown() {
    engine.destroy();

  }

  public void destroyKafkaInputAdapter() {
    if (kafkaInputAdapter != null) {
      logger.info("Kafka message broker destroyed");
      kafkaInputAdapter.destroy();
    }
  }
}
