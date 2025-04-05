package me.emmy.core.util;

import lombok.experimental.UtilityClass;

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
     * Formats a time in milliseconds to a readable format.
     *
     * @param millis the time in milliseconds
     * @return the formatted time
     */
    public String formatTimeMillis(long millis) {
        long seconds = millis / 1000L;

        if (seconds <= 0L) {
            return "0 seconds";
        }

        long minutes = seconds / 60L;
        seconds %= 60L;
        long hours = minutes / 60L;
        minutes %= 60L;
        long days = hours / 24L;
        hours %= 24L;
        long years = days / 365L;
        days %= 365L;

        StringBuilder time = new StringBuilder();

        if (years != 0L) {
            time.append(years).append((years == 1L) ? " year " : " years ");
        }

        if (days != 0L) {
            time.append(days).append((days == 1L) ? " day " : " days ");
        }

        if (hours != 0L) {
            time.append(hours).append((hours == 1L) ? " hour " : " hours ");
        }

        if (minutes != 0L) {
            time.append(minutes).append((minutes == 1L) ? " minute " : " minutes ");
        }

        if (seconds != 0L) {
            time.append(seconds).append((seconds == 1L) ? " second " : " seconds ");
        }

        return time.toString().trim();
    }

}