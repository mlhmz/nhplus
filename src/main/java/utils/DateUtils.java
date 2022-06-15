package utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;

/**
 * Utility Class to manage Dates in the Application
 */
public class DateUtils {
    private static final String TIME_FORMAT = "HH-mm-ss";
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String DATE_AND_TIME_FORMAT = DATE_FORMAT + "_" + TIME_FORMAT;

    public static LocalDate convertStringToLocalDate(String date) {
        String[] array = date.split("-");
        return LocalDate.of(Integer.parseInt(array[0]), Integer.parseInt(array[1]),
                Integer.parseInt(array[2]));
    }

    public static LocalTime convertStringToLocalTime(String time) {
        String[] array = time.split(":");
        return LocalTime.of(Integer.parseInt(array[0]), Integer.parseInt(array[1]));
    }

    /**
     * Converts complete {@link Date}, including Time, to String in Format yyyy-MM-dd_HH-mm-ss
     *
     * @param date The {@link Date} to format
     * @return a string with the date
     */
    public static String convertCompleteDateToString(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_AND_TIME_FORMAT);
        return formatter.format(date);
    }

    /**
     * Converts complete Date String, including Time, from Format yyyy-MM-dd_HH-mm-ss into {@link Date}
     *
     * @param dateString String to convert
     * @return {@link Date} with the Date of the String
     */
    public static Date convertCompleteDateStringToString(String dateString) {
        if (dateString == null) {
            return null;
        }

        SimpleDateFormat formatter = new SimpleDateFormat(DATE_AND_TIME_FORMAT);
        try {
            return formatter.parse(dateString);
        } catch (ParseException ex) {
            // if the dateString doesn't match the Format
            return null;
        }
    }

    /**
     * Appends Hours to a Date
     *
     * @param date The Date object
     * @param hours hours to add
     * @return Date with added Hours
     */
    public static Date appendHoursToDate(Date date, int hours) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR, hours);
        return calendar.getTime();
    }

    /**
     * Appends Days to Date
     *
     * @param date The Date object
     * @param days days to add
     * @return Date with added Days
     */
    public static Date appendDaysToDate(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        // The Day Constant DATE is the Day of the Month
        calendar.add(Calendar.DATE, days);
        return calendar.getTime();
    }

    /**
     * Appends Months to Date
     *
     * @param date The Date object
     * @param months days to add
     * @return Date with added Months
     */
    public static Date appendMonthsToDate(Date date, int months) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, months);
        return calendar.getTime();
    }

    /**
     * Appends Years to Date
     *
     * @param date The Date object
     * @param years days to add
     * @return Date with added Years
     */
    public static Date appendYearsToDate(Date date, int years) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, years);
        return calendar.getTime();
    }

}
