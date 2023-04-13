package ch.so.agi.stac.jackson;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class Interval {
    private ZonedDateTime startInterval; 
    private ZonedDateTime endInterval;
    
    public ZonedDateTime getStartInterval() {
        return startInterval;
    }
    public void setStartInterval(ZonedDateTime startInterval) {
        this.startInterval = startInterval;
    }
    public ZonedDateTime getEndInterval() {
        return endInterval;
    }
    public void setEndInterval(ZonedDateTime endInterval) {
        this.endInterval = endInterval;
    } 
    
    // fluent api
    
    public Interval startInterval(String date) {
        if (date.length() <= 10) {
            startInterval = LocalDate.parse(date).atStartOfDay(ZoneId.of("UTC"));
        } else  {
            startInterval = LocalDateTime.parse(date).atZone(ZoneOffset.UTC);
        }
        return this;
    }
    
    public Interval endInterval(String date) {
        if (date.length() <= 10) {
            endInterval = LocalDate.parse(date).atStartOfDay(ZoneId.of("UTC"));
        } else  {
            endInterval = LocalDateTime.parse(date).atZone(ZoneOffset.UTC);
        }
        return this;
    }

}
