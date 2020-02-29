package de.oriontec.esper.app.event;

import de.oriontec.esper.app.exception.EventManagerException;
import java.util.concurrent.LinkedBlockingQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class EventManager {

  private static LinkedBlockingQueue<EventRow> eventRows = new LinkedBlockingQueue<>();
  private static volatile boolean isLocked = false;
  public static final Logger logger = LoggerFactory.getLogger(EventManager.class);
  private static EventManager instance;

  public static synchronized EventManager getInstance() {
    if (instance == null) {
      instance = new EventManager();
    }
    return instance;
  }

  public int postEvent(EventRow eventRow) throws EventManagerException {
    try {
      if (!isLocked) {
        eventRows.put(eventRow);
        return 1;
      }
      return 0;
    } catch (InterruptedException e) {
      throw new EventManagerException(e.getMessage(), e);
    }
  }

  public EventRow takeEvent() throws EventManagerException {
    try {
      return eventRows.take();
    } catch (InterruptedException e) {
      throw new EventManagerException(e.getMessage(), e);
    }
  }

  public int getQueueSize() {
    return eventRows.size();
  }

  static synchronized boolean unlockQueue() {
    isLocked = false;
    return isLocked;
  }

  public synchronized boolean lockQueue() {
    isLocked = true;
    return isLocked;
  }
}
