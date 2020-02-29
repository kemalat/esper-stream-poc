package de.oriontec.esper.app.event;

import com.espertech.esper.client.EPRuntime;
import de.oriontec.esper.app.cep.EsperInitializer;
import de.oriontec.esper.app.util.RateCalculator;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This class handles the incoming requests. It creates an instance of EventRow class and pushes it to the CEP engine.
 */
public class EventHandler extends SimpleChannelUpstreamHandler {

  private static final Logger logger = LoggerFactory.getLogger(EventHandler.class);
  private EPRuntime engine = EsperInitializer.getInstance().getEngine().getEPRuntime();

  /**
   * The method is called when a new message is received.
   */
  @Override
  public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
    handleIncomingEvent(e);
  }

  /**
   * The method is called in case of exception.
   */
  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
    logger.error(e.getCause().getMessage(), e.getCause());
  }

  private void handleIncomingEvent(MessageEvent e) {
    String unparsedEventStr = (String) e.getMessage();
    logger.trace("Incoming raw event: " + unparsedEventStr);

    String[] eventStrings = unparsedEventStr.split("~");
    for (String eventString : eventStrings) {
      try {

        // parse eventRow
        List<String> commaSeparated = Arrays.asList(eventString.split(","));
        String eventType = commaSeparated.get(0);

        Map<String, Object> parameters = new HashMap<>();
        for (int i = 4; i < commaSeparated.size(); i = i + 2) {
          if (eventType.equals("c")) {
            parameters.put(commaSeparated.get(i), Integer.parseInt(commaSeparated.get(i + 1)));
          } else {
            String input = commaSeparated.get(i + 1);
            parameters.put(commaSeparated.get(i), input);
          }
        }

        EventRow eventRow = EventRow.builder()
            .eventType(eventType)
            .scenarioKey(commaSeparated.get(1))
            .targetScenarios(commaSeparated.get(2))
            .eventName(commaSeparated.get(3))
            .parameters(parameters).build();

        logger.debug("Parsed EventRow: " + eventRow.toString());
        // if this is an eventRow? (not as clock eventRow or other types)
        switch (eventRow.getEventType()) {
          case "a":
            engine.sendEvent(eventRow.getParameters(), eventRow.getEventFullName());
            RateCalculator.increaseCounter();
            break;
          case "c":
            ClockEvent clockEvent = createClockEvent(eventRow);
            EsperInitializer.getInstance().getEngine().getEPRuntime().sendEvent(clockEvent);
            break;
          case "is":
            logger.info("Monitor eventRow type.");
            break;
          default:
            logger.info("Inappropriate EventRow type type.");
            break;
        }
      } catch (Exception ex) {
        logger.error(ex.getMessage(), ex);
      }
    }
  }

  private ClockEvent createClockEvent(EventRow eventRow) {
    int minute = Integer.parseInt(String.valueOf(eventRow.getParameters().get("MINUTE")));
    int hour = Integer.parseInt(String.valueOf(eventRow.getParameters().get("HOUR")));
    int day = Integer.parseInt(String.valueOf(eventRow.getParameters().get("DAY")));
    int month = Integer.parseInt(String.valueOf(eventRow.getParameters().get("MONTH")));
    int year = Integer.parseInt(String.valueOf(eventRow.getParameters().get("YEAR")));
    int dayOfWeek = Integer.parseInt(String.valueOf(eventRow.getParameters().get("DAY_OF_WEEK")));
    return new ClockEvent(minute, hour, day, month, year, dayOfWeek);
  }
}
