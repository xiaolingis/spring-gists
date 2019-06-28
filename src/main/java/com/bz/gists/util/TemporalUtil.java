package com.bz.gists.util;

import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.Date;

/**
 * Created on 2019/6/22
 *
 * @author zhongyongbin
 */
public final class TemporalUtil {

    public static final DateTimeFormatter yyMMdd = DateTimeFormatter.ofPattern("yyMMdd");

    public static final DateTimeFormatter yyyyMMdd = DateTimeFormatter.ofPattern("yyyyMMdd");

    public static final DateTimeFormatter yyyyMMddHHmmss = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    public static final DateTimeFormatter yy_MM_dd = DateTimeFormatter.ofPattern("yy-MM-dd");

    public static final DateTimeFormatter yyyy_MM_dd = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static final DateTimeFormatter yyyy_MM_dd_HH_mm_ss = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static final DateTimeFormatter yyyy_MM_dd_HH_mm_ss_SSS = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    private static final PeriodFormatter periodFormatter = new PeriodFormatterBuilder()
            .appendSeconds().appendSuffix("s")
            .appendMinutes().appendSuffix("m")
            .appendHours().appendSuffix("h")
            .appendDays().appendSuffix("d")
            .appendMonths().appendSuffix("M")
            .appendYears().appendSuffix("y")
            .toFormatter();

    private TemporalUtil() {
    }

    /**
     * 时间格式化为字符串
     */
    public static String timeToString(Temporal temporal, DateTimeFormatter formatter) {
        return formatter.format(temporal);
    }

    /**
     * 时间戳格式化为字符串
     */
    public static String timestampToString(long timestamp, DateTimeFormatter formatter) {
        return formatter.format(timestampToLocalDateTime(timestamp));
    }

    /**
     * 时间戳转化为日期时间
     */
    public static LocalDateTime timestampToLocalDateTime(long timestamp) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault());
    }

    /**
     * 字符串转化为日期
     */
    public static LocalDate stringToLocalDate(String strTime, DateTimeFormatter formatter) {
        return LocalDate.parse(strTime, formatter);
    }

    /**
     * 字符串转化为日期时间
     */
    public static LocalDateTime stringToLocalDateTime(String strTime, DateTimeFormatter formatter) {
        return LocalDateTime.parse(strTime, formatter);
    }

    /**
     * 字符串转化为时间戳
     */
    public static long stringToTimestamp(String strTime, DateTimeFormatter formatter) {
        return LocalDateTime.parse(strTime, formatter).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    /**
     * Java 8 时间转化为 Date
     */
    public static Date temporalToDate(LocalDateTime dateTime) {
        return Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Date 转化为 Java 8 时间
     */
    public static LocalDateTime dateToTemporal(Date date) {
        return timestampToLocalDateTime(date.getTime());
    }

    /**
     * 获取指定日期的最大时间，如 2019-06-24 23:59:59.999999
     */
    public static LocalDateTime getMaxDateTime(LocalDateTime dateTime) {
        return getMaxDateTime(dateTime.toLocalDate());
    }

    /**
     * 获取指定日期的最大时间，如 2019-06-24 23:59:59.999999
     */
    public static LocalDateTime getMaxDateTime(LocalDate date) {
        return LocalDateTime.of(date, LocalTime.MAX);
    }

    /**
     * 获取指定日期的最小时间，如 2019-06-24 00:00:00
     */
    public static LocalDateTime getMinDateTime(LocalDateTime dateTime) {
        return getMinDateTime(dateTime.toLocalDate());
    }

    /**
     * 获取指定日期的最小时间，如 2019-06-24 00:00:00
     */
    public static LocalDateTime getMinDateTime(LocalDate date) {
        return LocalDateTime.of(date, LocalTime.MIN);
    }

    /**
     * 获取指定日期中午时间，如 2019-06-24 12:00:00
     */
    public static LocalDateTime getNoonDateTime(LocalDateTime dateTime) {
        return getNoonDateTime(dateTime.toLocalDate());
    }

    /**
     * 获取指定日期中午时间，如 2019-06-24 12:00:00
     */
    public static LocalDateTime getNoonDateTime(LocalDate date) {
        return LocalDateTime.of(date, LocalTime.NOON);
    }

    /**
     * 计算两个时间相差的毫秒数
     */
    public static long betweenMillis(Temporal startInclusive, Temporal endExclusive) {
        return Duration.between(startInclusive, endExclusive).toMillis();
    }

    /**
     * 计算两个时间相差的秒数
     */
    public static long betweenSeconds(Temporal startInclusive, Temporal endExclusive) {
        return Duration.between(startInclusive, endExclusive).getSeconds();
    }

    /**
     * 计算两个时间相差的分钟数
     */
    public static long betweenMinutes(Temporal startInclusive, Temporal endExclusive) {
        return Duration.between(startInclusive, endExclusive).toMillis();
    }

    /**
     * 计算两个时间相差的小时数
     */
    public static long betweenHours(Temporal startInclusive, Temporal endExclusive) {
        return Duration.between(startInclusive, endExclusive).toHours();
    }

    /**
     * 计算两个时间相差的天数
     */
    public static long betweenDays(Temporal startInclusive, Temporal endExclusive) {
        return Duration.between(startInclusive, endExclusive).toDays();
    }

    /**
     * 计算两个日期相差的周数
     */
    public static long betweenWeeks(LocalDate startInclusive, LocalDate endExclusive) {
        return startInclusive.until(endExclusive, ChronoUnit.WEEKS);
    }

    /**
     * 计算两个日期相差的月数
     */
    public static long betweenMonths(LocalDate startInclusive, LocalDate endExclusive) {
        return Period.between(startInclusive, endExclusive).getMonths();
    }

    /**
     * 计算两个日期相差的年数
     */
    public static long betweenYears(LocalDate startInclusive, LocalDate endExclusive) {
        return Period.between(startInclusive, endExclusive).getYears();
    }

    /**
     * 解析时间周期，如 1s 表示 1秒，1d 表示一天。
     *
     * @param period 时间周期，单位有 s m h d w M y ，分别对应秒，分，时，天，周，月，年
     */
    public static Duration parsePeriod(String period) {
        return Duration.ofMillis(periodFormatter.parsePeriod(period).toStandardDuration().getMillis());
    }
}
