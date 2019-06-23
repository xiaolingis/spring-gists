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

    public static DateTimeFormatter ISO = DateTimeFormatter.ISO_DATE_TIME;

    public static DateTimeFormatter yyMMdd = DateTimeFormatter.ofPattern("yyMMdd");

    public static DateTimeFormatter yyyyMMdd = DateTimeFormatter.ofPattern("yyyyMMdd");

    public static DateTimeFormatter yyyyMMddHHmmss = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    public static DateTimeFormatter yy_MM_dd = DateTimeFormatter.ofPattern("yy-MM-dd");

    public static DateTimeFormatter yyyy_MM_dd = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static DateTimeFormatter yyyy_MM_dd_HH_mm_ss = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static DateTimeFormatter yyyy_MM_dd_HH_mm_ss_SSS = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    private TemporalUtil() {
    }

    public static String timeToString(Temporal temporal, DateTimeFormatter formatter) {
        return formatter.format(temporal);
    }

    public static String timestampToString(long timestamp, DateTimeFormatter formatter) {
        return formatter.format(timestampToTemporal(timestamp));
    }

    public static LocalDate stringToLocalDate(String strTime, DateTimeFormatter formatter) {
        return LocalDate.parse(strTime, formatter);
    }

    public static LocalDateTime stringToLocalDateTime(String strTime, DateTimeFormatter formatter) {
        return LocalDateTime.parse(strTime, formatter);
    }

    public static long stringToTimestamp(String strTime, DateTimeFormatter formatter) {
        return LocalDateTime.parse(strTime, formatter).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    public static Date temporalToDate(LocalDateTime dateTime) {
        return Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static LocalDateTime dateToTemporal(Date date) {
        return timestampToTemporal(date.getTime());
    }

    public static LocalDateTime timestampToTemporal(long timestamp) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault());
    }

    public static LocalDateTime getMaxDateTime(LocalDateTime dateTime) {
        return getMaxDateTime(dateTime.toLocalDate());
    }

    public static LocalDateTime getMaxDateTime(LocalDate date) {
        return LocalDateTime.of(date, LocalTime.MAX);
    }

    public static LocalDateTime getMinDateTime(LocalDateTime dateTime) {
        return getMinDateTime(dateTime.toLocalDate());
    }

    public static LocalDateTime getMinDateTime(LocalDate date) {
        return LocalDateTime.of(date, LocalTime.MIN);
    }

    public static LocalDateTime getNoonDateTime(LocalDateTime dateTime) {
        return getNoonDateTime(dateTime.toLocalDate());
    }

    public static LocalDateTime getNoonDateTime(LocalDate date) {
        return LocalDateTime.of(date, LocalTime.NOON);
    }
}
