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
        LocalDate result = LocalDate.of(Integer.parseInt(array[0]), Integer.parseInt(array[1]),
                Integer.parseInt(array[2]));
        return result;
    }

    public static LocalTime convertStringToLocalTime(String time) {
        String[] array = time.split(":");
        LocalTime result = LocalTime.of(Integer.parseInt(array[0]), Integer.parseInt(array[1]));
        return result;
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

    public static Date appendHoursToDate(Date date, int hours) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR, 24);
        return calendar.getTime();
    }

}
