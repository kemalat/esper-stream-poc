package de.oriontec.esper.app.event;


import com.espertech.esperio.kafka.EsperIOKafkaInputProcessor;
import com.espertech.esperio.kafka.EsperIOKafkaInputProcessorContext;
import de.oriontec.esper.app.cep.EsperInitializer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KafkaProcessor implements EsperIOKafkaInputProcessor {

  private static final Logger logger = LoggerFactory.getLogger(KafkaProcessor.class);


  public void init(EsperIOKafkaInputProcessorContext context) {

    logger.info("kafka processor initialised");
  }

  public void process(ConsumerRecords<Object, Object> records) {
    logger.debug("records size: " + records.count());
    for (ConsumerRecord record : records) {
      logger.debug("record value: " + record.value());
      if (record.value() != null) {

        List<String> commaSeparated = Arrays.asList(record.value().toString().split(","));

        HashMap<String, Object> parameters = new HashMap<>();
        for (int i = 1; i < commaSeparated.size(); i = i + 2) {
          String input = commaSeparated.get(i + 1);
          parameters.put(commaSeparated.get(i), input);
        }
        EventRow eventRow = EventRow.builder()
//            .eventType(commaSeparated.get(0))
//            .scenarioKey(commaSeparated.get(1))
//            .targetScenarios(commaSeparated.get(2))
            .eventName(commaSeparated.get(0))
            .parameters(parameters)
            .build();

        logger.debug(eventRow.toString());
        try {
          EsperInitializer.getInstance().getEngine().getEPRuntime()
              .sendEvent(eventRow.getParameters(), eventRow.getEventName());
        } catch (Exception e) {
          logger.error(e.getMessage(), e.getCause());
        }

      }
    }
  }

  public void close() {
  }
}

