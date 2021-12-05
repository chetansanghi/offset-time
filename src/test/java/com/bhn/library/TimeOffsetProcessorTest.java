package com.bhn.library;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bhn.library.helper.TimeProcessorHelper;

/**
 * Unit test for simple App.
 */
@RunWith(JUnitPlatform.class)
public class TimeOffsetProcessorTest 
{
	private static final Logger logger = LoggerFactory.getLogger(TimeOffsetProcessorTest.class);

	private TimeOffsetProcessor timeOffsetProcessor;
	
	private static TimeProcessorHelper timeProcessorHelper;
	//one example - 11:20:10
	//other example - 12:00:00
	//another example - 00:00:00
	//another example - 23:00:00
	//another example - 23:59:00
	//another example - 23:59:59
	//another example - 00:59:00
	//another example - 00:59:59
	private static int specifiedHours;
	private static int specifiedMinutes;
	private static int specifiedSeconds;
	
	
	@BeforeAll
	static void setup() {
		timeProcessorHelper = new TimeProcessorHelper();
		specifiedHours = 00;
		specifiedMinutes = 59;
		specifiedSeconds = 59;
	}	
	
	@BeforeEach
	void init() {
		timeOffsetProcessor = new TimeOffsetProcessor();	
	}
	
    @Test
    public void whenNullInput_ThenIllegalArgumentExceptionThrown() {
    	logger.info("****** START whenNullInput_ThenIllegalArgumentExceptionThrown **********");    	
        assertThrows(IllegalArgumentException.class, () -> {
        	timeOffsetProcessor.addTimeOffset(null);
        });
    	logger.info("****** END whenNullInput_ThenIllegalArgumentExceptionThrown **********");        
    }

    @Test
    public void whenNoInput_ThenIllegalArgumentExceptionThrown() {
    	logger.info("****** START whenNoInput_ThenIllegalArgumentExceptionThrown **********");    	
        assertThrows(IllegalArgumentException.class, () -> {
        	timeOffsetProcessor.addTimeOffset(new String[] {});
        });
    	logger.info("****** END whenNoInput_ThenIllegalArgumentExceptionThrown **********");        
    }

    @Test
    public void whenInputIsNonNumeric_ThenNumberFormatExceptionThrown() {
    	logger.info("****** START whenInputIsNonNumeric_ThenNumberFormatExceptionThrown **********");    	
        assertThrows(IllegalArgumentException.class, () -> {
        	timeOffsetProcessor.addTimeOffset(new String[] {"abc"});
        });
    	logger.info("****** END whenInputIsNonNumeric_ThenNumberFormatExceptionThrown **********");        
    }

    
    @Test
    public void whenInputAs0Seconds_ThenIllegalArgumentExceptionThrown() {
    	logger.info("****** START whenInputAs0Seconds_ThenIllegalArgumentExceptionThrown **********");    	
        assertThrows(IllegalArgumentException.class, () -> {
        	timeOffsetProcessor.addTimeOffset(new String[] {"0"});
        });
    	logger.info("****** END whenInputAs0Seconds_ThenIllegalArgumentExceptionThrown **********");        
    }
    
    @Test
    public void whenInputIsMoreThan1YearInSeconds_ThenIllegalArgumentExceptionThrown() {
    	logger.info("****** START whenInputIsMoreThan1YearInSeconds_ThenIllegalArgumentExceptionThrown **********");    	
        assertThrows(IllegalArgumentException.class, () -> {
        	timeOffsetProcessor.addTimeOffset(new String[] {"1314001"});
        });
    	logger.info("****** END whenInputIsMoreThan1YearInSeconds_ThenIllegalArgumentExceptionThrown **********");        
    }
    
    @Test
    public void whenInputSecondsLessThan60_ThenAddedToCurrentTime() {
    	logger.info("****** START whenInputSecondsLessThan60_ThenAddedToCurrentTime **********");
    	final LocalTime specifiedTime = timeProcessorHelper.getSpecifiedTime(specifiedHours, specifiedMinutes, specifiedSeconds);
    	timeOffsetProcessor = new TimeOffsetProcessor(specifiedTime);    	
    	Optional<LocalTime> timeWithOffset = timeOffsetProcessor.addTimeOffset(new String[] {"30"});
    	LocalTime expectedTime = specifiedTime.plusSeconds(30).truncatedTo(ChronoUnit.SECONDS);
    	logger.info("timeWithOffset = " + timeWithOffset.get());
    	logger.info("expectedTime = " + expectedTime);    	
    	assertNotNull(timeWithOffset);
    	assertNotNull(timeWithOffset.get());
    	assertEquals(timeWithOffset.get(), expectedTime);
    	logger.info("****** END whenInputSecondsLessThan60_ThenAddedToCurrentTime **********");    	
    }
    
    @Test
    public void whenInputSeconds_ResultsInto60ForSeconds_ThenIncrementMinute() {
    	logger.info("****** START whenInputSeconds_ResultsInto60ForSeconds_ThenIncrementMinute **********");    	
    	final LocalTime specifiedTime = timeProcessorHelper.getSpecifiedTime(specifiedHours, specifiedMinutes, specifiedSeconds);
    	timeOffsetProcessor = new TimeOffsetProcessor(specifiedTime);    	
    	Optional<LocalTime> timeWithOffset = timeOffsetProcessor.addTimeOffset(new String[] {"50"});
    	LocalTime expectedTime = specifiedTime.plusSeconds(50).truncatedTo(ChronoUnit.SECONDS);
    	logger.info("timeWithOffset = " + timeWithOffset.get());
    	logger.info("expectedTime = " + expectedTime);    	
    	assertNotNull(timeWithOffset);
    	assertNotNull(timeWithOffset.get());
    	assertEquals(timeWithOffset.get(), expectedTime);
    	logger.info("****** END whenInputSeconds_ResultsInto60ForSeconds_ThenIncrementMinute **********");    	
    }
    
    @Test
    public void whenInputSecondsAre500_ThenIncrementMinuteAndSeconds() {
    	logger.info("****** START whenInputSecondsAre500_ThenIncrementMinuteAndSeconds **********");    	
    	final LocalTime specifiedTime = timeProcessorHelper.getSpecifiedTime(specifiedHours, specifiedMinutes, specifiedSeconds);
    	timeOffsetProcessor = new TimeOffsetProcessor(specifiedTime);
    	Optional<LocalTime> timeWithOffset = timeOffsetProcessor.addTimeOffset(new String[] {"500"});
    	LocalTime expectedTime = specifiedTime.plusSeconds(500).truncatedTo(ChronoUnit.SECONDS);
    	logger.info("timeWithOffset = " + timeWithOffset.get());
    	logger.info("expectedTime = " + expectedTime);    	
    	assertNotNull(timeWithOffset);
    	assertNotNull(timeWithOffset.get());
    	assertEquals(timeWithOffset.get(), expectedTime);
    	logger.info("****** END whenInputSecondsAre500_ThenIncrementMinuteAndSeconds **********");    	
    }

    @Test
    public void whenInputSecondsAre530_ThenIncrementMinuteAndSeconds() {
    	logger.info("****** START whenInputSecondsAre530_ThenIncrementMinuteAndSeconds **********");    	
    	final LocalTime specifiedTime = timeProcessorHelper.getSpecifiedTime(specifiedHours, specifiedMinutes, specifiedSeconds);
    	timeOffsetProcessor = new TimeOffsetProcessor(specifiedTime);
    	Optional<LocalTime> timeWithOffset = timeOffsetProcessor.addTimeOffset(new String[] {"530"});
    	LocalTime expectedTime = specifiedTime.plusSeconds(530).truncatedTo(ChronoUnit.SECONDS);
    	logger.info("timeWithOffset = " + timeWithOffset.get());
    	logger.info("expectedTime = " + expectedTime);    	
    	assertNotNull(timeWithOffset);
    	assertNotNull(timeWithOffset.get());
    	assertEquals(timeWithOffset.get(), expectedTime);
    	logger.info("****** END whenInputSecondsAre530_ThenIncrementMinuteAndSeconds **********");    	
    }


    @Test
    public void whenInputSecondsAre540_ThenOnlyIncrementMinute() {
    	logger.info("****** START whenInputSecondsAre540_ThenOnlyIncrementMinute **********");    	
    	final LocalTime specifiedTime = timeProcessorHelper.getSpecifiedTime(specifiedHours, specifiedMinutes, specifiedSeconds);
    	timeOffsetProcessor = new TimeOffsetProcessor(specifiedTime);
    	Optional<LocalTime> timeWithOffset = timeOffsetProcessor.addTimeOffset(new String[] {"540"});
    	LocalTime expectedTime = specifiedTime.plusSeconds(540).truncatedTo(ChronoUnit.SECONDS);
    	logger.info("timeWithOffset = " + timeWithOffset.get());
    	logger.info("expectedTime = " + expectedTime);    	
    	assertNotNull(timeWithOffset);
    	assertNotNull(timeWithOffset.get());
    	assertEquals(timeWithOffset.get(), expectedTime);
    	logger.info("****** END whenInputSecondsAre540_ThenOnlyIncrementMinute **********");    	
    }

    @Test
    public void whenInputSecondsAre3110_ThenRolloverSecondAndRolloverMinute() {
    	logger.info("****** START whenInputSecondsAre3110_ThenRolloverSecondAndRolloverMinute **********");    	
    	final LocalTime specifiedTime = timeProcessorHelper.getSpecifiedTime(specifiedHours, specifiedMinutes, specifiedSeconds);
    	timeOffsetProcessor = new TimeOffsetProcessor(specifiedTime);
    	Optional<LocalTime> timeWithOffset = timeOffsetProcessor.addTimeOffset(new String[] {"3110"});
    	LocalTime expectedTime = specifiedTime.plusSeconds(3110).truncatedTo(ChronoUnit.SECONDS);
    	logger.info("timeWithOffset = " + timeWithOffset.get());
    	logger.info("expectedTime = " + expectedTime);    	
    	assertNotNull(timeWithOffset);
    	assertNotNull(timeWithOffset.get());
    	assertEquals(timeWithOffset.get(), expectedTime);
    	logger.info("****** END whenInputSecondsAre3110_ThenRolloverSecondAndRolloverMinute **********");    	
    }

    @Test
    public void whenInputSecondsAre46310_ThenRolloverSecondAndRolloverMinuteAndRolloverHour() {
    	logger.info("****** START whenInputSecondsAre46310_ThenRolloverSecondAndRolloverMinuteAndRolloverHour **********");    	
    	final LocalTime specifiedTime = timeProcessorHelper.getSpecifiedTime(specifiedHours, specifiedMinutes, specifiedSeconds);
    	timeOffsetProcessor = new TimeOffsetProcessor(specifiedTime);
    	Optional<LocalTime> timeWithOffset = timeOffsetProcessor.addTimeOffset(new String[] {"46310"});
    	LocalTime expectedTime = specifiedTime.plusSeconds(46310).truncatedTo(ChronoUnit.SECONDS);
    	logger.info("timeWithOffset = " + timeWithOffset.get());
    	logger.info("expectedTime = " + expectedTime);    	
    	assertNotNull(timeWithOffset);
    	assertNotNull(timeWithOffset.get());
    	assertEquals(timeWithOffset.get(), expectedTime);
    	logger.info("****** END whenInputSecondsAre46310_ThenRolloverSecondAndRolloverMinuteAndRolloverHour **********");    	
    }

    @Test
    public void whenInputSecondsAre49910_ThenRolloverSecondAndRolloverMinuteAndRolloverHour() {
    	logger.info("****** START whenInputSecondsAre49910_ThenRolloverSecondAndRolloverMinuteAndRolloverHour **********");    	
    	final LocalTime specifiedTime = timeProcessorHelper.getSpecifiedTime(specifiedHours, specifiedMinutes, specifiedSeconds);
    	timeOffsetProcessor = new TimeOffsetProcessor(specifiedTime);
    	Optional<LocalTime> timeWithOffset = timeOffsetProcessor.addTimeOffset(new String[] {"49910"});
    	LocalTime expectedTime = specifiedTime.plusSeconds(49910).truncatedTo(ChronoUnit.SECONDS);
    	logger.info("timeWithOffset = " + timeWithOffset.get());
    	logger.info("expectedTime = " + expectedTime);    	
    	assertNotNull(timeWithOffset);
    	assertNotNull(timeWithOffset.get());
    	assertEquals(timeWithOffset.get(), expectedTime);
    	logger.info("****** END whenInputSecondsAre49910_ThenRolloverSecondAndRolloverMinuteAndRolloverHour **********");    	
    }

    @Test
    public void whenInputSecondsAre1Minute_ThenRolloverSecondAndIncrementMinute() {
    	logger.info("****** START whenInputSecondsAre1Minute_ThenRolloverSecondAndIncrementMinute **********");    	
    	final LocalTime specifiedTime = timeProcessorHelper.getSpecifiedTime(specifiedHours, specifiedMinutes, specifiedSeconds);
    	timeOffsetProcessor = new TimeOffsetProcessor(specifiedTime);
    	Optional<LocalTime> timeWithOffset = timeOffsetProcessor.addTimeOffset(new String[] {"60"});
    	LocalTime expectedTime = specifiedTime.plusSeconds(60).truncatedTo(ChronoUnit.SECONDS);
    	logger.info("timeWithOffset = " + timeWithOffset.get());
    	logger.info("expectedTime = " + expectedTime);    	
    	assertNotNull(timeWithOffset);
    	assertNotNull(timeWithOffset.get());
    	assertEquals(timeWithOffset.get(), expectedTime);
    	logger.info("****** END whenInputSecondsAre1Minute_ThenRolloverSecondAndIncrementMinute **********");    	
    }
    
    @Test
    public void whenInputSecondsAre1Hour_ThenRolloverSecondAndRolloverMinuteAndIncrementHour() {
    	logger.info("****** START whenInputSecondsAre1Hour_ThenRolloverSecondAndRolloverMinuteAndIncrementHour **********");    	
    	final LocalTime specifiedTime = timeProcessorHelper.getSpecifiedTime(specifiedHours, specifiedMinutes, specifiedSeconds);
    	timeOffsetProcessor = new TimeOffsetProcessor(specifiedTime);
    	Optional<LocalTime> timeWithOffset = timeOffsetProcessor.addTimeOffset(new String[] {"3600"});
    	LocalTime expectedTime = specifiedTime.plusSeconds(3600).truncatedTo(ChronoUnit.SECONDS);
    	logger.info("timeWithOffset = " + timeWithOffset.get());
    	logger.info("expectedTime = " + expectedTime);    	
    	assertNotNull(timeWithOffset);
    	assertNotNull(timeWithOffset.get());
    	assertEquals(timeWithOffset.get(), expectedTime);
    	logger.info("****** END whenInputSecondsAre1Hour_ThenRolloverSecondAndRolloverMinuteAndIncrementHour **********");    	
    }
    
    
    @Test
    public void when230000_Add59Seconds_SecondsAdded() {
    	logger.info("****** START when230000_Add59Seconds_SecondsAdded **********");    	
    	final LocalTime specifiedTime = timeProcessorHelper.getSpecifiedTime(23, 00, 00);
    	timeOffsetProcessor = new TimeOffsetProcessor(specifiedTime);
    	Optional<LocalTime> timeWithOffset = timeOffsetProcessor.addTimeOffset(new String[] {"59"});
    	LocalTime expectedTime = specifiedTime.plusSeconds(59).truncatedTo(ChronoUnit.SECONDS);
    	logger.info("timeWithOffset = " + timeWithOffset.get());
    	logger.info("expectedTime = " + expectedTime);    	
    	assertNotNull(timeWithOffset);
    	assertNotNull(timeWithOffset.get());
    	assertEquals(timeWithOffset.get(), expectedTime);
    	logger.info("****** END when230000_Add59Seconds_SecondsAdded **********");    	
    }

    @Test
    public void when230000_Add60Seconds_MinuteIncrementedAndSecondsAre0() {
    	logger.info("****** START when230000_Add60Seconds_MinuteIncrementedAndSecondsAre0 **********");    	
    	final LocalTime specifiedTime = timeProcessorHelper.getSpecifiedTime(23, 00, 00);
    	timeOffsetProcessor = new TimeOffsetProcessor(specifiedTime);
    	Optional<LocalTime> timeWithOffset = timeOffsetProcessor.addTimeOffset(new String[] {"60"});
    	LocalTime expectedTime = specifiedTime.plusSeconds(60).truncatedTo(ChronoUnit.SECONDS);
    	logger.info("timeWithOffset = " + timeWithOffset.get());
    	logger.info("expectedTime = " + expectedTime);    	
    	assertNotNull(timeWithOffset);
    	assertNotNull(timeWithOffset.get());
    	assertEquals(timeWithOffset.get(), expectedTime);
    	logger.info("****** END when230000_Add60Seconds_MinuteIncrementedAndSecondsAre0 **********");    	
    }

    @Test
    public void when230100_Add59Minutes_HoursIncrementedAndMinutesAre0AndSecondsAre0() {
    	logger.info("****** START when230100_Add59Minutes_HoursIncrementedAndMinutesAre0AndSecondsAre0 **********");    	
    	final LocalTime specifiedTime = timeProcessorHelper.getSpecifiedTime(23, 01, 00);
    	timeOffsetProcessor = new TimeOffsetProcessor(specifiedTime);
    	Optional<LocalTime> timeWithOffset = timeOffsetProcessor.addTimeOffset(new String[] {"3540"});
    	LocalTime expectedTime = specifiedTime.plusSeconds(3540).truncatedTo(ChronoUnit.SECONDS);
    	logger.info("timeWithOffset = " + timeWithOffset.get());
    	logger.info("expectedTime = " + expectedTime);    	
    	assertNotNull(timeWithOffset);
    	assertNotNull(timeWithOffset.get());
    	assertEquals(timeWithOffset.get(), expectedTime);
    	logger.info("****** END when230100_Add59Minutes_HoursIncrementedAndMinutesAre0AndSecondsAre0 **********");    	
    }
    
    @Test
    public void when230101_Add58Minutes59Seconds_HoursIncrementedAndMinutesAre0AndSecondsAre0() {
    	logger.info("****** START when230101_Add58Minutes59Seconds_HoursIncrementedAndMinutesAre0AndSecondsAre0 **********");    	
    	final LocalTime specifiedTime = timeProcessorHelper.getSpecifiedTime(23, 01, 01);
    	timeOffsetProcessor = new TimeOffsetProcessor(specifiedTime);
    	Optional<LocalTime> timeWithOffset = timeOffsetProcessor.addTimeOffset(new String[] {"3539"});
    	LocalTime expectedTime = specifiedTime.plusSeconds(3539).truncatedTo(ChronoUnit.SECONDS);
    	logger.info("timeWithOffset = " + timeWithOffset.get());
    	logger.info("expectedTime = " + expectedTime);    	
    	assertNotNull(timeWithOffset);
    	assertNotNull(timeWithOffset.get());
    	assertEquals(timeWithOffset.get(), expectedTime);
    	logger.info("****** END when230101_Add58Minutes59Seconds_HoursIncrementedAndMinutesAre0AndSecondsAre0 **********");    	
    }

    
    @Test
    public void when235900_Add60Seconds_HoursIncrementedAndMinutesAre0AndSecondsAre0() {
    	logger.info("****** START when235900_Add60Seconds_HoursIncrementedAndMinutesAre0AndSecondsAre0 **********");    	
    	final LocalTime specifiedTime = timeProcessorHelper.getSpecifiedTime(23, 59, 00);
    	timeOffsetProcessor = new TimeOffsetProcessor(specifiedTime);
    	Optional<LocalTime> timeWithOffset = timeOffsetProcessor.addTimeOffset(new String[] {"60"});
    	LocalTime expectedTime = specifiedTime.plusSeconds(60).truncatedTo(ChronoUnit.SECONDS);
    	logger.info("timeWithOffset = " + timeWithOffset.get());
    	logger.info("expectedTime = " + expectedTime);    	
    	assertNotNull(timeWithOffset);
    	assertNotNull(timeWithOffset.get());
    	assertEquals(timeWithOffset.get(), expectedTime);
    	logger.info("****** END when235900_Add60Seconds_HoursIncrementedAndMinutesAre0AndSecondsAre0 **********");    	
    }

    @Test
    public void when235900_Add120Seconds_HoursIncrementedAndMinuteIsIncrementedAndSecondsAre0() {
    	logger.info("****** START when235900_Add120Seconds_HoursIncrementedAndMinuteIsIncrementedAndSecondsAre0 **********");    	
    	final LocalTime specifiedTime = timeProcessorHelper.getSpecifiedTime(23, 59, 00);
    	timeOffsetProcessor = new TimeOffsetProcessor(specifiedTime);
    	Optional<LocalTime> timeWithOffset = timeOffsetProcessor.addTimeOffset(new String[] {"120"});
    	LocalTime expectedTime = specifiedTime.plusSeconds(120).truncatedTo(ChronoUnit.SECONDS);
    	logger.info("timeWithOffset = " + timeWithOffset.get());
    	logger.info("expectedTime = " + expectedTime);    	
    	assertNotNull(timeWithOffset);
    	assertNotNull(timeWithOffset.get());
    	assertEquals(timeWithOffset.get(), expectedTime);
    	logger.info("****** END when235900_Add120Seconds_HoursIncrementedAndMinuteIsIncrementedAndSecondsAre0 **********");    	
    }
    
    @Test
    public void when000000_Add59Seconds_SecondsAdded() {
    	logger.info("****** START when000000_Add59Seconds_SecondsAdded **********");    	
    	final LocalTime specifiedTime = timeProcessorHelper.getSpecifiedTime(00, 00, 00);
    	timeOffsetProcessor = new TimeOffsetProcessor(specifiedTime);
    	Optional<LocalTime> timeWithOffset = timeOffsetProcessor.addTimeOffset(new String[] {"59"});
    	LocalTime expectedTime = specifiedTime.plusSeconds(59).truncatedTo(ChronoUnit.SECONDS);
    	logger.info("timeWithOffset = " + timeWithOffset.get());
    	logger.info("expectedTime = " + expectedTime);    	
    	assertNotNull(timeWithOffset);
    	assertNotNull(timeWithOffset.get());
    	assertEquals(timeWithOffset.get(), expectedTime);
    	logger.info("****** END when000000_Add59Seconds_SecondsAdded **********");    	
    }
    

    @Test
    public void when000000_Add60Seconds_MinuteIncrementedAndSecondsAre0() {
    	logger.info("****** START when000000_Add60Seconds_MinuteIncrementedAndSecondsAre0 **********");    	
    	final LocalTime specifiedTime = timeProcessorHelper.getSpecifiedTime(00, 00, 00);
    	timeOffsetProcessor = new TimeOffsetProcessor(specifiedTime);
    	Optional<LocalTime> timeWithOffset = timeOffsetProcessor.addTimeOffset(new String[] {"60"});
    	LocalTime expectedTime = specifiedTime.plusSeconds(60).truncatedTo(ChronoUnit.SECONDS);
    	logger.info("timeWithOffset = " + timeWithOffset.get());
    	logger.info("expectedTime = " + expectedTime);    	
    	assertNotNull(timeWithOffset);
    	assertNotNull(timeWithOffset.get());
    	assertEquals(timeWithOffset.get(), expectedTime);
    	logger.info("****** END when000000_Add60Seconds_MinuteIncrementedAndSecondsAre0 **********");    	
    }

    @Test
    public void when000100_Add59Minutes_HoursIncrementedAndMinutesAre0AndSecondsAre0() {
    	logger.info("****** START when000100_Add59Minutes_HoursIncrementedAndMinutesAre0AndSecondsAre0 **********");    	
    	final LocalTime specifiedTime = timeProcessorHelper.getSpecifiedTime(00, 01, 00);
    	timeOffsetProcessor = new TimeOffsetProcessor(specifiedTime);
    	Optional<LocalTime> timeWithOffset = timeOffsetProcessor.addTimeOffset(new String[] {"3540"});
    	LocalTime expectedTime = specifiedTime.plusSeconds(3540).truncatedTo(ChronoUnit.SECONDS);
    	logger.info("timeWithOffset = " + timeWithOffset.get());
    	logger.info("expectedTime = " + expectedTime);    	
    	assertNotNull(timeWithOffset);
    	assertNotNull(timeWithOffset.get());
    	assertEquals(timeWithOffset.get(), expectedTime);
    	logger.info("****** END when000100_Add59Minutes_HoursIncrementedAndMinutesAre0AndSecondsAre0 **********");    	
    }
    
    @Test
    public void when000101_Add58Minutes59Seconds_HoursIncrementedAndMinutesAre0AndSecondsAre0() {
    	logger.info("****** START when000101_Add58Minutes59Seconds_HoursIncrementedAndMinutesAre0AndSecondsAre0 **********");    	
    	final LocalTime specifiedTime = timeProcessorHelper.getSpecifiedTime(00, 01, 01);
    	timeOffsetProcessor = new TimeOffsetProcessor(specifiedTime);
    	Optional<LocalTime> timeWithOffset = timeOffsetProcessor.addTimeOffset(new String[] {"3539"});
    	LocalTime expectedTime = specifiedTime.plusSeconds(3539).truncatedTo(ChronoUnit.SECONDS);
    	logger.info("timeWithOffset = " + timeWithOffset.get());
    	logger.info("expectedTime = " + expectedTime);    	
    	assertNotNull(timeWithOffset);
    	assertNotNull(timeWithOffset.get());
    	assertEquals(timeWithOffset.get(), expectedTime);
    	logger.info("****** END when000101_Add58Minutes59Seconds_HoursIncrementedAndMinutesAre0AndSecondsAre0 **********");    	
    }

    
    @Test
    public void when005900_Add60Seconds_HoursIncrementedAndMinutesAre0AndSecondsAre0() {
    	logger.info("****** START when005900_Add60Seconds_HoursIncrementedAndMinutesAre0AndSecondsAre0 **********");    	
    	final LocalTime specifiedTime = timeProcessorHelper.getSpecifiedTime(00, 59, 00);
    	timeOffsetProcessor = new TimeOffsetProcessor(specifiedTime);
    	Optional<LocalTime> timeWithOffset = timeOffsetProcessor.addTimeOffset(new String[] {"60"});
    	LocalTime expectedTime = specifiedTime.plusSeconds(60).truncatedTo(ChronoUnit.SECONDS);
    	logger.info("timeWithOffset = " + timeWithOffset.get());
    	logger.info("expectedTime = " + expectedTime);    	
    	assertNotNull(timeWithOffset);
    	assertNotNull(timeWithOffset.get());
    	assertEquals(timeWithOffset.get(), expectedTime);
    	logger.info("****** END when005900_Add60Seconds_HoursIncrementedAndMinutesAre0AndSecondsAre0 **********");    	
    }

    @Test
    public void when005900_Add120Seconds_HoursIncrementedAndMinuteIsIncrementedAndSecondsAre0() {
    	logger.info("****** START when005900_Add120Seconds_HoursIncrementedAndMinuteIsIncrementedAndSecondsAre0 **********");    	
    	final LocalTime specifiedTime = timeProcessorHelper.getSpecifiedTime(00, 59, 00);
    	timeOffsetProcessor = new TimeOffsetProcessor(specifiedTime);
    	Optional<LocalTime> timeWithOffset = timeOffsetProcessor.addTimeOffset(new String[] {"120"});
    	LocalTime expectedTime = specifiedTime.plusSeconds(120).truncatedTo(ChronoUnit.SECONDS);
    	logger.info("timeWithOffset = " + timeWithOffset.get());
    	logger.info("expectedTime = " + expectedTime);    	
    	assertNotNull(timeWithOffset);
    	assertNotNull(timeWithOffset.get());
    	assertEquals(timeWithOffset.get(), expectedTime);
    	logger.info("****** END when005900_Add120Seconds_HoursIncrementedAndMinuteIsIncrementedAndSecondsAre0 **********");    	
    }

   
    @Test
    public void when005900_Add59Seconds_SecondsAdded() {
    	logger.info("****** START when005900_Add59Seconds_SecondsAdded **********");    	
    	final LocalTime specifiedTime = timeProcessorHelper.getSpecifiedTime(00, 59, 00);
    	timeOffsetProcessor = new TimeOffsetProcessor(specifiedTime);
    	Optional<LocalTime> timeWithOffset = timeOffsetProcessor.addTimeOffset(new String[] {"59"});
    	LocalTime expectedTime = specifiedTime.plusSeconds(59).truncatedTo(ChronoUnit.SECONDS);
    	logger.info("timeWithOffset = " + timeWithOffset.get());
    	logger.info("expectedTime = " + expectedTime);    	
    	assertNotNull(timeWithOffset);
    	assertNotNull(timeWithOffset.get());
    	assertEquals(timeWithOffset.get(), expectedTime);
    	logger.info("****** END when005900_Add59Seconds_SecondsAdded **********");    	
    }
    

    @Test
    public void when005900_Add60Seconds_HourIncrementedAndMinuteAre0AndSecondsAre0() {
    	logger.info("****** START when005900_Add60Seconds_HourIncrementedAndMinuteAre0AndSecondsAre0 **********");    	
    	final LocalTime specifiedTime = timeProcessorHelper.getSpecifiedTime(00, 59, 00);
    	timeOffsetProcessor = new TimeOffsetProcessor(specifiedTime);
    	Optional<LocalTime> timeWithOffset = timeOffsetProcessor.addTimeOffset(new String[] {"60"});
    	LocalTime expectedTime = specifiedTime.plusSeconds(60).truncatedTo(ChronoUnit.SECONDS);
    	logger.info("timeWithOffset = " + timeWithOffset.get());
    	logger.info("expectedTime = " + expectedTime);    	
    	assertNotNull(timeWithOffset);
    	assertNotNull(timeWithOffset.get());
    	assertEquals(timeWithOffset.get(), expectedTime);
    	logger.info("****** END when005900_Add60Seconds_HourIncrementedAndMinuteAre0AndSecondsAre0 **********");    	
    }

    @Test
    public void when005900_Add59Minutes_HoursIncrementedAndMinutesAre0AndSecondsAre0() {
    	logger.info("****** START when005900_Add59Minutes_HoursIncrementedAndMinutesAre0AndSecondsAre0 **********");    	
    	final LocalTime specifiedTime = timeProcessorHelper.getSpecifiedTime(00, 59, 00);
    	timeOffsetProcessor = new TimeOffsetProcessor(specifiedTime);
    	Optional<LocalTime> timeWithOffset = timeOffsetProcessor.addTimeOffset(new String[] {"3540"});
    	LocalTime expectedTime = specifiedTime.plusSeconds(3540).truncatedTo(ChronoUnit.SECONDS);
    	logger.info("timeWithOffset = " + timeWithOffset.get());
    	logger.info("expectedTime = " + expectedTime);    	
    	assertNotNull(timeWithOffset);
    	assertNotNull(timeWithOffset.get());
    	assertEquals(timeWithOffset.get(), expectedTime);
    	logger.info("****** END when005900_Add59Minutes_HoursIncrementedAndMinutesAre0AndSecondsAre0 **********");    	
    }
    
    @Test
    public void when005901_Add58Minutes59Seconds_HoursIncrementedAndMinutesAre0AndSecondsAre0() {
    	logger.info("****** START when005901_Add58Minutes59Seconds_HoursIncrementedAndMinutesAre0AndSecondsAre0 **********");    	
    	final LocalTime specifiedTime = timeProcessorHelper.getSpecifiedTime(00, 59, 01);
    	timeOffsetProcessor = new TimeOffsetProcessor(specifiedTime);
    	Optional<LocalTime> timeWithOffset = timeOffsetProcessor.addTimeOffset(new String[] {"3539"});
    	LocalTime expectedTime = specifiedTime.plusSeconds(3539).truncatedTo(ChronoUnit.SECONDS);
    	logger.info("timeWithOffset = " + timeWithOffset.get());
    	logger.info("expectedTime = " + expectedTime);    	
    	assertNotNull(timeWithOffset);
    	assertNotNull(timeWithOffset.get());
    	assertEquals(timeWithOffset.get(), expectedTime);
    	logger.info("****** END when005901_Add58Minutes59Seconds_HoursIncrementedAndMinutesAre0AndSecondsAre0 **********");    	
    }

    @Test
    public void when005959_Add60Seconds_HoursIncrementedAndMinutesAre0AndSecondsAreIncremented() {
    	logger.info("****** START when005959_Add60Seconds_HoursIncrementedAndMinutesAre0AndSecondsAreIncremented **********");    	
    	final LocalTime specifiedTime = timeProcessorHelper.getSpecifiedTime(00, 59, 59);
    	timeOffsetProcessor = new TimeOffsetProcessor(specifiedTime);
    	Optional<LocalTime> timeWithOffset = timeOffsetProcessor.addTimeOffset(new String[] {"60"});
    	LocalTime expectedTime = specifiedTime.plusSeconds(60).truncatedTo(ChronoUnit.SECONDS);
    	logger.info("timeWithOffset = " + timeWithOffset.get());
    	logger.info("expectedTime = " + expectedTime);    	
    	assertNotNull(timeWithOffset);
    	assertNotNull(timeWithOffset.get());
    	assertEquals(timeWithOffset.get(), expectedTime);
    	logger.info("****** END when005959_Add60Seconds_HoursIncrementedAndMinutesAre0AndSecondsAreIncremented **********");    	
    }

    @Test
    public void when005959_Add120Seconds_HoursIncrementedAndMinuteIsIncrementedAndSecondsAreIncremented() {
    	logger.info("****** START when005959_Add120Seconds_HoursIncrementedAndMinuteIsIncrementedAndSecondsAre0 **********");    	
    	final LocalTime specifiedTime = timeProcessorHelper.getSpecifiedTime(00, 59, 59);
    	timeOffsetProcessor = new TimeOffsetProcessor(specifiedTime);
    	Optional<LocalTime> timeWithOffset = timeOffsetProcessor.addTimeOffset(new String[] {"120"});
    	LocalTime expectedTime = specifiedTime.plusSeconds(120).truncatedTo(ChronoUnit.SECONDS);
    	logger.info("timeWithOffset = " + timeWithOffset.get());
    	logger.info("expectedTime = " + expectedTime);    	
    	assertNotNull(timeWithOffset);
    	assertNotNull(timeWithOffset.get());
    	assertEquals(timeWithOffset.get(), expectedTime);
    	logger.info("****** END when005959_Add120Seconds_HoursIncrementedAndMinuteIsIncrementedAndSecondsAre0 **********");    	
    }

   
    @Test
    public void when005959_Add59Seconds_HoursAreIncrementedAndMinutesAre0AndSecondsIncremented() {
    	logger.info("****** START when005959_Add59Seconds_HoursAreIncrementedAndMinutesAre0AndSecondsIncremented **********");    	
    	final LocalTime specifiedTime = timeProcessorHelper.getSpecifiedTime(00, 59, 59);
    	timeOffsetProcessor = new TimeOffsetProcessor(specifiedTime);
    	Optional<LocalTime> timeWithOffset = timeOffsetProcessor.addTimeOffset(new String[] {"59"});
    	LocalTime expectedTime = specifiedTime.plusSeconds(59).truncatedTo(ChronoUnit.SECONDS);
    	logger.info("timeWithOffset = " + timeWithOffset.get());
    	logger.info("expectedTime = " + expectedTime);    	
    	assertNotNull(timeWithOffset);
    	assertNotNull(timeWithOffset.get());
    	assertEquals(timeWithOffset.get(), expectedTime);
    	logger.info("****** END when005959_Add59Seconds_HoursAreIncrementedAndMinutesAre0AndSecondsIncremented **********");    	
    }
    

    @Test
    public void when005959_Add60Seconds_HourIncrementedAndMinuteAre0AndSecondsAreIncremented() {
    	logger.info("****** START when005959_Add60Seconds_HourIncrementedAndMinuteAre0AndSecondsAreIncremented **********");    	
    	final LocalTime specifiedTime = timeProcessorHelper.getSpecifiedTime(00, 59, 59);
    	timeOffsetProcessor = new TimeOffsetProcessor(specifiedTime);
    	Optional<LocalTime> timeWithOffset = timeOffsetProcessor.addTimeOffset(new String[] {"60"});
    	LocalTime expectedTime = specifiedTime.plusSeconds(60).truncatedTo(ChronoUnit.SECONDS);
    	logger.info("timeWithOffset = " + timeWithOffset.get());
    	logger.info("expectedTime = " + expectedTime);    	
    	assertNotNull(timeWithOffset);
    	assertNotNull(timeWithOffset.get());
    	assertEquals(timeWithOffset.get(), expectedTime);
    	logger.info("****** END when005959_Add60Seconds_HourIncrementedAndMinuteAre0AndSecondsAreIncremented **********");    	
    }

    @Test
    public void when005959_Add59Minutes_HoursIncrementedAndMinutesAreIncrementedAndSecondsAreIncremented() {
    	logger.info("****** START when005959_Add59Minutes_HoursIncrementedAndMinutesAreIncrementedAndSecondsAreIncremented **********");    	
    	final LocalTime specifiedTime = timeProcessorHelper.getSpecifiedTime(00, 59, 59);
    	timeOffsetProcessor = new TimeOffsetProcessor(specifiedTime);
    	Optional<LocalTime> timeWithOffset = timeOffsetProcessor.addTimeOffset(new String[] {"3540"});
    	LocalTime expectedTime = specifiedTime.plusSeconds(3540).truncatedTo(ChronoUnit.SECONDS);
    	logger.info("timeWithOffset = " + timeWithOffset.get());
    	logger.info("expectedTime = " + expectedTime);    	
    	assertNotNull(timeWithOffset);
    	assertNotNull(timeWithOffset.get());
    	assertEquals(timeWithOffset.get(), expectedTime);
    	logger.info("****** END when005959_Add59Minutes_HoursIncrementedAndMinutesAreIncrementedAndSecondsAreIncremented **********");    	
    }
    
    @Test
    public void when005959_Add59MinutesAnd1Seconds_HoursIncrementedAndMinutesAreIncrementedAndSecondsAreRolledOver() {
    	logger.info("****** START when005959_Add59MinutesAnd1Seconds_HoursIncrementedAndMinutesAreIncrementedAndSecondsAreRolledOver **********");    	
    	final LocalTime specifiedTime = timeProcessorHelper.getSpecifiedTime(00, 59, 59);
    	timeOffsetProcessor = new TimeOffsetProcessor(specifiedTime);
    	Optional<LocalTime> timeWithOffset = timeOffsetProcessor.addTimeOffset(new String[] {"3541"});
    	LocalTime expectedTime = specifiedTime.plusSeconds(3541).truncatedTo(ChronoUnit.SECONDS);
    	logger.info("timeWithOffset = " + timeWithOffset.get());
    	logger.info("expectedTime = " + expectedTime);    	
    	assertNotNull(timeWithOffset);
    	assertNotNull(timeWithOffset.get());
    	assertEquals(timeWithOffset.get(), expectedTime);
    	logger.info("****** END when005959_Add59MinutesAnd1Seconds_HoursIncrementedAndMinutesAreIncrementedAndSecondsAreRolledOver **********");    	
    }

    @Test
    public void when005959_Add58Minutes59Seconds_HoursIncrementedAndMinutesAreIncrementedAndSecondsAreIncremented() {
    	logger.info("****** START when005959_Add58Minutes59Seconds_HoursIncrementedAndMinutesAreIncrementedAndSecondsAreIncremented **********");    	
    	final LocalTime specifiedTime = timeProcessorHelper.getSpecifiedTime(00, 59, 59);
    	timeOffsetProcessor = new TimeOffsetProcessor(specifiedTime);
    	Optional<LocalTime> timeWithOffset = timeOffsetProcessor.addTimeOffset(new String[] {"3539"});
    	LocalTime expectedTime = specifiedTime.plusSeconds(3539).truncatedTo(ChronoUnit.SECONDS);
    	logger.info("timeWithOffset = " + timeWithOffset.get());
    	logger.info("expectedTime = " + expectedTime);    	
    	assertNotNull(timeWithOffset);
    	assertNotNull(timeWithOffset.get());
    	assertEquals(timeWithOffset.get(), expectedTime);
    	logger.info("****** END when005959_Add58Minutes59Seconds_HoursIncrementedAndMinutesAreIncrementedAndSecondsAreIncremented **********");    	
    }

    @ParameterizedTest
    @ValueSource(strings = { "86400", "129600", "172800","216000","259200","302400","1314000" })
    public void whenInputSecondsAre1YearOrMore_ThenRolloverSecondAndRolloverMinuteAndRolloverHourAndNoIncrement(String timeOffsetInSeconds) {
    	logger.info("****** START whenInputSecondsAre1YearOrMore_ThenRolloverSecondAndRolloverMinuteAndRolloverHourAndNoIncrement **********");    	
    	final LocalTime specifiedTime = timeProcessorHelper.getSpecifiedTime(specifiedHours, specifiedMinutes, specifiedSeconds);
    	timeOffsetProcessor = new TimeOffsetProcessor(specifiedTime);
    	Optional<LocalTime> timeWithOffset = timeOffsetProcessor.addTimeOffset(new String[] {timeOffsetInSeconds});
    	LocalTime expectedTime = specifiedTime.plusSeconds(Integer.parseInt(timeOffsetInSeconds)).truncatedTo(ChronoUnit.SECONDS);
    	logger.info("timeWithOffset = " + timeWithOffset.get());
    	logger.info("expectedTime = " + expectedTime);    	
    	assertNotNull(timeWithOffset);
    	assertNotNull(timeWithOffset.get());
    	assertEquals(timeWithOffset.get(), expectedTime);
    	logger.info("****** END whenInputSecondsAre1YearOrMore_ThenRolloverSecondAndRolloverMinuteAndRolloverHourAndNoIncrement **********");    	
    }

}
