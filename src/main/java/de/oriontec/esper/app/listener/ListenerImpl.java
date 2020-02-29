package de.oriontec.esper.app.listener;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ListenerImpl implements UpdateListener {

  private Logger logger = LoggerFactory.getLogger(ListenerImpl.class);


  @Override
  public void update(EventBean[] eventBeans, EventBean[] eventBeans1) {

    logger.info("Listener executed.");

  }
}
