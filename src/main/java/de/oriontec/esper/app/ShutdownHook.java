package de.oriontec.esper.app;

import de.oriontec.esper.app.cep.EsperInitializer;
import de.oriontec.esper.app.event.EventListener;
import de.oriontec.esper.app.event.EventManager;
import de.oriontec.esper.app.util.RateCalculator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShutdownHook extends Thread {

  private static final Logger logger = LoggerFactory.getLogger(ShutdownHook.class);

  ShutdownHook() {
    super("Shutdown Hook");
  }

  @Override
  public void run() {
    logger.info("SHUTDOWN invoked. Gracefully shutting down the engine.");
    closeEngineEventInput();
    waitUntilQueueEmpty();
    EsperInitializer.getInstance().shutdown();
  }

  private void closeEngineEventInput() {
    EsperInitializer.getInstance().destroyKafkaInputAdapter();
    EventManager.getInstance().lockQueue();
    logger.info("SHUTDOWN: {} was locked gracefully", EventManager.class.getSimpleName());
    RateCalculator.stopCalculation();
    EventListener.getInstance().stopServerGracefully();
    logger.info("SHUTDOWN: EventListener is closed gracefully");
  }

  private void waitUntilQueueEmpty() {
    while (EventManager.getInstance().getQueueSize() > 0) {
      logger.info("SHUTDOWN: Queue size is {}", EventManager.getInstance().getQueueSize());
      try {
        Thread.sleep(1000L);
      } catch (InterruptedException e) {
        logger.error(e.getMessage(), e.getCause());
      }
    }
    logger.info("SHUTDOWN: Queue size is empty");
  }

}
