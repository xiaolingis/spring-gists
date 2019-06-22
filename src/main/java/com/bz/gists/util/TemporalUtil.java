package com.bz.gists.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.util.Date;

/**
 * Created on 2019/6/22
 *
 * @author zhongyongbin
 */
public final class TemporalUtil {

    private TemporalUtil() {
    }

    public static String timeToString(Temporal temporal, Formatter formatter) {
        return formatter.dateTimeFormatter.format(temporal);
    }

    public static LocalDate stringToDate(String strTime, Formatter formatter) {
        return LocalDate.parse(strTime, formatter.dateTimeFormatter);
    }

    public static LocalDateTime stringToDateTime(String strTime, Formatter formatter) {
        return LocalDateTime.parse(strTime, formatter.dateTimeFormatter);
    }

    public static Date temporalToDate(LocalDateTime dateTime) {
        return Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
    }


    public static LocalDateTime dateToTemporal(Date date) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.systemDefault());
    }

    public static LocalDateTime getMaxDateTime(LocalDate date) {
        return LocalDateTime.of(date, LocalTime.MAX);
    }

    public static LocalDateTime getMaxDateTime(LocalDateTime dateTime) {
        return getMaxDateTime(dateTime.toLocalDate());
    }

    public static LocalDateTime getMinDateTime(LocalDate date) {
        return LocalDateTime.of(date, LocalTime.MIN);
    }

    public static LocalDateTime getMinDateTime(LocalDateTime dateTime) {
        return getMinDateTime(dateTime.toLocalDate());
    }

    public static LocalDateTime getNoonDateTime(LocalDate date) {
        return LocalDateTime.of(date, LocalTime.NOON);
    }

    public static LocalDateTime getNoonDateTime(LocalDateTime dateTime) {
        return getNoonDateTime(dateTime.toLocalDate());
    }

    public enum Formatter {
        ISO(DateTimeFormatter.ISO_DATE_TIME),
        yyMMdd(DateTimeFormatter.ofPattern("yyMMdd")),
        yyyyMMdd(DateTimeFormatter.ofPattern("yyyyMMdd")),
        yyyyMMddHHmmss(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")),
        yy_MM_dd(DateTimeFormatter.ofPattern("yy-MM-dd")),
        yyyy_MM_dd(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
        yyyy_MM_dd_HH_mm_ss(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        final DateTimeFormatter dateTimeFormatter;

        Formatter(DateTimeFormatter dateTimeFormatter) {
            this.dateTimeFormatter = dateTimeFormatter;
        }
    }
}
