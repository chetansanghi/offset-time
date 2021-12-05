package com.bhn.library.helper;

import java.time.Clock;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TimeProcessorHelper {

	private static final Logger logger = LoggerFactory.getLogger(TimeProcessorHelper.class);
	
	
    /*
     * Method to get current time as per Europe/Berlin Time Zone
     */
    public LocalTime getEuropeanCurrentTime() {
        final Clock clock = Clock.system(ZoneId.of("Europe/Berlin"));
        final LocalTime now = LocalTime.now(clock).truncatedTo(ChronoUnit.SECONDS);
    	//LocalTime now = LocalTime.of(11, 20,10);
        logger.info("Localtime now(Europe/Berlin): " + now);
    	return now;
    }
    
    /*
     * Method to get current time as per system time zone
     */
    public LocalTime getCurrentTime() {
        final LocalTime now = LocalTime.now().truncatedTo(ChronoUnit.SECONDS);
        logger.info("Localtime now(Current TimeZone): " + now);
    	return now;
    }
    
    /*
     * Method to get time as per input provided for seconds, minutes and hours
     */
    public LocalTime getSpecifiedTime(int hours, int minutes, int seconds) {
        final LocalTime specified = LocalTime.of(hours, minutes,seconds).truncatedTo(ChronoUnit.SECONDS);
        logger.info("Specified Localtime: " + specified);
    	return specified;
    }
    
	
}
