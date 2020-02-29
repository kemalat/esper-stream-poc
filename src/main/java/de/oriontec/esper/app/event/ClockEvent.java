package de.oriontec.esper.app.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class ClockEvent {

  private int minute;
  private int hour;
  private int day;
  private int month;
  private int year;
  private int dayOfWeek;
}
