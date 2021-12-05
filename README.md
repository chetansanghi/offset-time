# Time Offset Processor

This library provides method to add a time offset given in seconds to input Local Time as European or any other time.


## Input 

Objects of the Time class hold a time value for a European‐style 24 hour clock. The value consists of hours, minutes and seconds. The range of the value is 00:00:00 (midnight) to 23:59:59 (one second before midnight).

## What it does?
 
 * Add an offset to this Time.
 * Rolls over the hours, minutes and seconds fields when needed.
 * Add one second to the current time.
 * When the seconds value reaches 60, it rolls over to zero.
 * When the seconds roll over to zero, the minutes advance.
 * So 00:00:59 rolls over to 00:01:00.
 * When the minutes reach 60, they roll over and the hours advance.
 * So 00:59:59 rolls over to 01:00:00.
 * When the hours reach 24, they roll over to zero.
 * So 23:59:59 rolls over to 00:00:00.
