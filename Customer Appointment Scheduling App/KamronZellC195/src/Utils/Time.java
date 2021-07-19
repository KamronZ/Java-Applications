package Utils;

import java.sql.Timestamp;
import java.time.*;
import java.util.Calendar;

/**
 * class handles dates and time
 */
public class Time {

    /**
     * used to get the LocalDate only
     * @return current LocalDate
     */
    public static LocalDate currentLocalDate(){
        return LocalDate.now();
    }

    /**
     * Used to get current LocalDateTime
     * @return current LocalDateTime
     */
    public static LocalDateTime getCurrentTime(){
        return LocalDateTime.now();
    }

    /**
     * Used to display to user what timeZone they are in
     * @return Timezone's display name
     */
    public static String getTimeZone() {
        //get Calendar instance
        Calendar now = Calendar.getInstance();

        //get current TimeZone using getTimeZone method of Calendar class
        return now.getTimeZone().getDisplayName();
    }

    /**
     * Used to get TimeZone ID as String
     * @return TimeZone ID
     */
    public static String getTimeZoneID( ){


        Calendar now = Calendar.getInstance();
        return now.getTimeZone().getID();

    }

    /**
     * Used to get the offset that is applied to EST business hours to the user's local timeZone
     * @param hour
     * @return int hour value offset between EST and current
     */
    public static int getTimeZoneOffsetEastToLocal(int hour) {
        Calendar now = Calendar.getInstance();
        String timeZone1 = "America/New_York";
        String timeZone2 = now.getTimeZone().getID();

        LocalDateTime dt = LocalDateTime.now();

        ZonedDateTime fromZonedDateTime = dt.atZone(ZoneId.of(timeZone1));
        ZonedDateTime toZonedDateTime = dt.atZone(ZoneId.of(timeZone2));
        long diff = Duration.between(fromZonedDateTime, toZonedDateTime).toMillis();

        System.out.println("difference between timezones is " + diff + " milliseconds");

        return hour - (int)((diff / (1000*60*60)) % 24);
    }

    /**
     * Used to get offset to switch from UTC time to Local time
     * @param hour
     * @return int hour value between UTC and local time
     */
    public static int getTimeZoneOffsetUTCToLocal(int hour) {
        Calendar now = Calendar.getInstance();
        String timeZone1 = "UTC";
        String timeZone2 = now.getTimeZone().getID();

        LocalDateTime dt = LocalDateTime.now();
        ZonedDateTime fromZonedDateTime = dt.atZone(ZoneId.of(timeZone1));
        ZonedDateTime toZonedDateTime = dt.atZone(ZoneId.of(timeZone2));
        long diff = Duration.between(fromZonedDateTime, toZonedDateTime).toMillis();

        System.out.println("difference between timezones is " + diff + " milliseconds");

        return hour + (int)((diff / (1000*60*60)) % 24);
    }

    /**
     * onvers LDT into a timestamp
     * @param selectedTimeToConvert
     * @return Timestamp of LDT
     */
    public static Timestamp convertLDTToTimestamp(LocalDateTime selectedTimeToConvert){
        System.out.println("Converted time is:  " + Timestamp.valueOf(selectedTimeToConvert ));
        return Timestamp.valueOf(selectedTimeToConvert);
    }

    /**
     * Timestamp to a LocalDate
     * @param timestampToConvert
     * @return LD from a timestamp
     */
    public static LocalDate convertToLocalDate(Timestamp timestampToConvert){
        ZoneId zoneID =ZoneId.systemDefault();
       return timestampToConvert.toInstant().atZone(zoneID).toLocalDate();
    }

    /**
     * takes a LDT and converts to a Timestamp in UTC time
     * @param timeToConvert
     * @return convert ldt to timestamp in UTC time
     */
    public static Timestamp convertToUTC(LocalDateTime timeToConvert){

        LocalDateTime ldt = timeToConvert;

        ZoneId zoneId = ZoneId.systemDefault();

        ZonedDateTime zdtStart = ldt.atZone(zoneId);
        System.out.println("Local Time: " + zdtStart);
        ZonedDateTime utcStart =  zdtStart.withZoneSameInstant(ZoneId.of("UTC"));
        System.out.println("Zoned time: " + utcStart);
        ldt = utcStart.toLocalDateTime();
        System.out.println("Zoned time with zone stripped: " +ldt);

        return convertLDTToTimestamp(ldt);
    }


}




