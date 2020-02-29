package de.oriontec.esper.app.event;


import java.io.Serializable;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * The class is the representation of an incoming event. An instance of this class is created on EventHandler and passed to the CEP engine.
 */
@Getter
@Setter
@ToString
@Builder
public class EventRow implements Serializable {

  private String scenarioKey;
  private String eventType;
  private String targetScenarios;
  private String eventName;
  private Map parameters;


  public String getEventFullName() {
    return this.targetScenarios + "_" + this.eventName;
  }

}

