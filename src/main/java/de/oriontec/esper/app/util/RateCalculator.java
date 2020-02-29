package de.oriontec.esper.app.util;

import com.google.common.util.concurrent.RateLimiter;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("UnstableApiUsage")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RateCalculator {

  private static RateLimiter limiter = RateLimiter.create(1.0);
  private static Logger logger = LoggerFactory.getLogger(RateCalculator.class);
  private static AtomicInteger rate = new AtomicInteger(0);
  private static boolean flag = true;

  static {
    Thread informer = new Thread(() -> {
      while (flag) {
        limiter.acquire();
        logger.trace("Incoming rate on " + LocalDateTime.now() + " is " + rate);
        resetCounter();
      }
    });

    informer.setName("Rate Informer");
    informer.setDaemon(true);
    informer.start();
  }


  public static synchronized void increaseCounter() {
    rate.incrementAndGet();
  }

  private static synchronized void resetCounter() {
    rate.set(0);
  }

  public static void stopCalculation() {
    flag = false;
  }
}
