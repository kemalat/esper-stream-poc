package de.oriontec.esper.app.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class GlobalExceptionHandler implements Thread.UncaughtExceptionHandler {

  private static Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  @Override
  public void uncaughtException(Thread t, Throwable e) {
    logger.error(e.getMessage(), e);
  }
}
