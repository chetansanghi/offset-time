package com.bhn.library;

import java.time.LocalTime;
import java.util.Optional;
import java.util.function.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bhn.library.helper.TimeProcessorHelper;
import com.bhn.library.model.TimeOffsetDetails;

/**
 * Time Offset Processor !
 *
 */
public class TimeOffsetProcessor 
{
	private static final Logger logger = LoggerFactory.getLogger(TimeOffsetProcessor.class);
	
	public static final int NUM_SEC_DAY = 86400;
	public static final int MAX_SECONDS = 60;
	public static final int MAX_MINUTES = 60;	
	public static final int MAX_HOURS = 24;	
	
	private final LocalTime inputTime;
	
	public TimeOffsetProcessor() {
		inputTime = LocalTime.now();
	}
	
	public TimeOffsetProcessor(final LocalTime inputLocalTime) {
		inputTime = inputLocalTime;
	}
	
    public LocalTime getInputTime() {
		return inputTime;
	}

	public Optional<LocalTime> addTimeOffset(final String[] args){
    	Optional<LocalTime> localTimeWithOffsetResult = Optional.empty();    	

    	//STEP 1: Validate if Input Provided
    	int timeOffset = validateTimeOffsetInputProvided(args);
        
        //STEP 2 - Validate OffSet Value
    	validateTimeOffsetIsNumeric(timeOffset);
    	
        //STEP 3 - Calculate Seconds to add for time offset input
    	Optional<TimeOffsetDetails> timeOffsetDetailsResult = calculateSecondsMinutesHoursToAdd(timeOffset);
        
    	if ( timeOffsetDetailsResult.isPresent() ) {
    		
    		final TimeOffsetDetails inputTimeOffsetDetails = timeOffsetDetailsResult.get();

        	//STEP 4: Add Time Offset to Current Time
        	localTimeWithOffsetResult = addToCurrentTime(inputTimeOffsetDetails, getInputTime());
        	
        	if ( localTimeWithOffsetResult.isPresent() ) {
        		final LocalTime localTimeWithOffset = localTimeWithOffsetResult.get();
        		
        		logger.info("Input Time: " + getInputTime());
        		logger.info("Time Offset to add: " + inputTimeOffsetDetails);
        		logger.info("Final time with offset added: " + localTimeWithOffset);
        	}
    	}
    	
    	return localTimeWithOffsetResult;
    }
    
    /*
     * Method to valid input provided for Time Offset
     */
    private int validateTimeOffsetInputProvided(final String[] args) {
    	int timeOffset = 0;
    	if( args != null && args.length > 0) {
	        try {
	        	timeOffset = Integer.parseInt(args[0]);
	        }catch(NumberFormatException nfe) {
	        	throw new IllegalArgumentException("Please specify a numeric value for time offset in seconds to add.");
	        }
    	}else {
    		throw new IllegalArgumentException("Please specify time offset in seconds to add.");
    	}
    		
        return timeOffset;
    }

    /*
     * Method to valid numeric range for input provided for Time Offset
     */
    private void validateTimeOffsetIsNumeric(final int timeOffset) {
    	final Predicate<Integer> greaterThanZero = (i) -> i > 0;
    	final Predicate<Integer> lessThanEqualToNumSecDay = (i) -> i <= 86400;
    	
        boolean isTimeOffsetValid = greaterThanZero.and(lessThanEqualToNumSecDay).test(timeOffset);
        if ( !isTimeOffsetValid ) {
        	throw new IllegalArgumentException("Please check time offset value provided. Valid range is 1 to 86400 seconds.");
        }
    	
    }
    
    /*
     * Method to calculate seconds, minutes and hours for input Time Offset provided
     */
    private Optional<TimeOffsetDetails> calculateSecondsMinutesHoursToAdd(final int timeOffset) {
    	TimeOffsetDetails timeOffSetDetail = new TimeOffsetDetails();
   	
    	int secondsToAdd = timeOffset % MAX_SECONDS;
        logger.debug("secondsToAdd = " + secondsToAdd);
        
        int minutesForTimeOffset = timeOffset / MAX_SECONDS;
        logger.debug("minutesForTimeOffset = " + minutesForTimeOffset);
        
        int minutesToAdd = 0;
        int hoursToAdd = 0;
        if ( minutesForTimeOffset >= MAX_MINUTES ) {
        	minutesToAdd = minutesForTimeOffset % MAX_MINUTES;
        	hoursToAdd = minutesForTimeOffset / MAX_MINUTES;
        }else {
        	minutesToAdd = minutesForTimeOffset % MAX_MINUTES;
        }
        
        logger.debug("minutesToAdd = " + minutesToAdd);
        logger.debug("hoursToAdd = " + hoursToAdd);  
        
        timeOffSetDetail = new TimeOffsetDetails(secondsToAdd, minutesToAdd, hoursToAdd);
        
        return Optional.ofNullable(timeOffSetDetail);
    } 
    
    /*
     * Method to add input time offset provided to current time
     */
    private Optional<LocalTime> addToCurrentTime(final TimeOffsetDetails inputTimeOffset, final LocalTime now ) {
		LocalTime localTimeWithOffset = null;
		
	    int nowSeconds = now.getSecond();
	    nowSeconds = nowSeconds + inputTimeOffset.getSecondsToAdd();
	    int minutesToAdd = inputTimeOffset.getMinutesToAdd();
	    if ( nowSeconds >= MAX_SECONDS ) {
	    	logger.debug("Now Seconds after adding seconds offset greater than 60");
	    	nowSeconds -= MAX_SECONDS;
	    	minutesToAdd += 1;
	    	logger.debug("minutes to add after adding offset seconds: " + minutesToAdd);
	    }
		logger.info("seconds after adding offset seconds: " + nowSeconds);
		
		
        int nowMinutes = now.getMinute();
        nowMinutes = nowMinutes + minutesToAdd;
        int hoursToAdd = inputTimeOffset.getHoursToAdd();
        if ( nowMinutes >= MAX_MINUTES ) {
        	logger.debug("Now Minutes after adding minutes offset greater than 60");
        	nowMinutes -= MAX_MINUTES;
        	hoursToAdd += 1;
        	logger.debug("hours to add after adding offset minutes: " + hoursToAdd);
        }
    	logger.info("minutes after adding offset minutes: " + nowMinutes);
    	
    	int nowHours = now.getHour();
    	nowHours = nowHours + hoursToAdd;
    	if ( nowHours >= MAX_HOURS ) {
        	logger.info("Now Hours after adding hours offset greater than 24 and this means date needs to change");    		
    		nowHours -= MAX_HOURS;
    	}
    	logger.info("hours after adding offset hours: " + nowHours);    		

    	localTimeWithOffset = LocalTime.of(nowHours, nowMinutes,nowSeconds);
    	logger.debug("Localtime localTimeWithOffset: " + localTimeWithOffset);		

		return Optional.ofNullable(localTimeWithOffset);
	}  
    
	public static void main( String[] args )
    {
		TimeProcessorHelper timeProcessorHelper = new TimeProcessorHelper();
    	TimeOffsetProcessor timeOffsetProcessor = new TimeOffsetProcessor(timeProcessorHelper.getEuropeanCurrentTime());
    	Optional<LocalTime> localTimeWithOffsetResult = timeOffsetProcessor.addTimeOffset(args);
    	if( localTimeWithOffsetResult.isPresent() ) {
    		logger.info("Final Time With offset added: " + localTimeWithOffsetResult.get());
    	}
    	
    }  
}
