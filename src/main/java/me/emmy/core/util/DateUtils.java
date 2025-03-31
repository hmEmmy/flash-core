package me.emmy.core.util;

import lombok.experimental.UtilityClass;

import java.text.SimpleDateFormat;
import java.util.Date;

@UtilityClass
public class DateUtils {
    /**
     * Parses a time string to milliseconds.
     *
     * @param time the time string
     * @return the time in milliseconds
     */
    public long parseTime(String time) {
        long result = 0L;
        StringBuilder number = new StringBuilder();
        for (char c : time.toCharArray()) {
            if (Character.isDigit(c)) {
                number.append(c);
            } else {
                long num = Long.parseLong(number.toString());
                switch (c) {
                    case 's':
                        result += num * 1000L;
                        break;
                    case 'm':
                        result += num * 60000L;
                        break;
                    case 'h':
                        result += num * 3600000L;
                        break;
                    case 'd':
                        result += num * 86400000L;
                        break;
                    case 'w':
                        result += num * 604800000L;
                        break;
                    case 'y':
                        result += num * 31556952000L;
                        break;
                }
                number = new StringBuilder();
            }
        }
        return result;
    }

    /**
     * Formats a date in milliseconds to a readable format.
     *
     * @param millis the date in milliseconds
     * @return the formatted date
     */
    public static String formatDate(long millis) {
        return new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new Date(millis));
    }
}