package com.hetao.grasseed.common.util;

import org.apache.commons.lang3.StringUtils;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * date：只包含年月日
 * time：只包含时分秒
 * timestamp：包含年月日时分秒
 * @author shaojp
 *
 */
public class DateUtil {
	
	public static final long MILLI_SECONDS_IN_ONE_DAY = 24 * 3600 * 1000;
	
	//2001-07-04T12:08:56.235-07或2001-07-04T12:08:56.235Z
    public static final String PATTERN_ISO8601_ONELETTER = "yyyy-MM-dd'T'HH:mm:ss.SSSX";
    //2001-07-04T12:08:56.235-0700或2001-07-04T12:08:56.235Z
	public static final String PATTERN_ISO8601_TWOLETTER = "yyyy-MM-dd'T'HH:mm:ss.SSSXX";
	//2001-07-04T12:08:56.235-07:00或2001-07-04T12:08:56.235Z
	public static final String PATTERN_ISO8601_THREELETTER = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
	
	//2001-07-04T12:08:56.235-0700或2001-07-04T12:08:56.235+0000
	public static final String PATTERN_RFC822 = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
	
	public static final String LOCAL_TIMESTAMP_PATTERN = "yyyy/MM/dd HH:mm:ss";
	public static final String LOCAL_DATE_PATTERN = "yyyy/MM/dd";

    public static final String SIMPLE_TIMESTAMP_PATTERN = "yyyyMMddHHmmss";
    public static final String SIMPLE_DATE_PATTERN = "yyyyMMdd";
	
	public static final String DEFAULT_TIMESTAMP_PATTERN = "yyyy-MM-dd HH:mm:ss";
	public static final String DEFAULT_TIMESTAMP_PATTERN_NO_SEONCDS = "yyyy-MM-dd HH:mm";
	public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";
	public static final String DEFAULT_TIME_PATTERN = "HH:mm:ss";
	public static final String DEFAULT_TIME_PATTERN_NO_SEONCDS = "HH:mm";
	
	public static final TimeZone DEFAULT_TIME_ZONE = TimeZone.getTimeZone("GMT+8");

	private static final DatatypeFactory dtf;
	
	static {
		 try {
			dtf = DatatypeFactory.newInstance();
		} catch (DatatypeConfigurationException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 将yyyy-MM-dd HH:mm格式的字符串转换为Date对象
	 * @param s
	 * @return
	 */
	public static Date parseTimestampWithoutSeconds(String s) {
		return parse(s, DEFAULT_TIMESTAMP_PATTERN_NO_SEONCDS);
	}

    /**
     * 将yyyyMMddHHmmss格式的字符串转换为Date对象
     * @param s
     * @return
     */
	public static Date parseSimpleTimestamp(String s) {
        return parse(s, SIMPLE_TIMESTAMP_PATTERN);
    }
	
	/**
	 * 将yyyy-MM-dd HH:mm:ss格式的字符串转换为Date对象
	 * @param s 格式
	 * @return
	 */
	public static Date parseTimestamp(String s) {
		return parse(s, DEFAULT_TIMESTAMP_PATTERN);
	}
	
	/**
	 * 将yyyy-MM-dd格式的字符串转换为Date对象
	 * @param s
	 * @return
	 */
	public static Date parseDate(String s) {
		return parse(s, DEFAULT_DATE_PATTERN);
	}

    /**
     * 将yyyyMMdd格式的字符串转换为Date对象
     * @param s
     * @return
     */
    public static Date parseSimpleDate(String s) {
        return parse(s, SIMPLE_DATE_PATTERN);
    }
	
	/**
	 * 将HH:mm:ss格式的字符串转换为Date对象
	 * @param s
	 * @return
	 */
	public static Date parseTime(String s) {
		return parse(s, DEFAULT_TIME_PATTERN);
	}
	
	/**
	 * 将HH:mm格式的字符串转换为Date对象
	 * @param s
	 * @return
	 */
	public static Date parseTimeWithoutSeconds(String s) {
		return parse(s, DEFAULT_TIME_PATTERN_NO_SEONCDS);
	}
	
	/**
	 * 将指定格式的字符串转换为Date对象
	 * @param s
	 * @param pattern
	 * @return
	 */
	public static Date parse(String s, String pattern) {
		if(StringUtils.isEmpty(s) || StringUtils.isEmpty(pattern)) {
			return null;
		}
		
		try {
			DateFormat df = new SimpleDateFormat(pattern);
			df.setTimeZone(DEFAULT_TIME_ZONE);
			return df.parse(s);
		} catch (ParseException e) {
			//ignore
			return null;
		}
	}
	
	/**
	 * 将Date对象转换为yyyy-MM-dd HH:mm:ss格式的字符串
	 * @param date
	 * @return
	 */
	public static String formatTimestamp(Date date) {
		return format(date, DEFAULT_TIMESTAMP_PATTERN);
	}

    /**
     * 将Date对象转换为yyyyMMddHHmmss格式的字符串
     * @param date
     * @return
     */
    public static String formatSimpleTimestamp(Date date) {
        return format(date, SIMPLE_TIMESTAMP_PATTERN);
    }

	/**
	 * 将Date对象转换为yyyy-MM-dd HH:mm格式的字符串
	 * @param date
	 * @return
	 */
	public static String formatTimestampWithoutSeconds(Date date) {
		return format(date, DEFAULT_TIMESTAMP_PATTERN_NO_SEONCDS);
	}
	
	/**
	 * 将Date对象转换为yyyy-MM-dd格式的字符串
	 * @param date
	 * @return
	 */
	public static String formatDate(Date date) {
		return format(date, DEFAULT_DATE_PATTERN);
	}

    /**
     * 将Date对象转换为yyyyMMdd格式的字符串
     * @param date
     * @return
     */
    public static String formatSimpleDate(Date date) {
        return format(date, SIMPLE_DATE_PATTERN);
    }
	
	/**
	 * 将Date对象转换为HH:mm:ss格式的字符串
	 * @param date
	 * @return
	 */
	public static String formatTime(Date date) {
		return format(date, DEFAULT_TIME_PATTERN);
	}
	
	/**
	 * 将Date对象转换为HH:mm格式的字符串
	 * @param date
	 * @return
	 */
	public static String formatTimeWithoutSeconds(Date date){
		return format(date, DEFAULT_TIME_PATTERN_NO_SEONCDS);
	}

	/**
	 * 将Date对象转换为指定格式的字符串
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String format(Date date, String pattern) {
		if(date == null || StringUtils.isEmpty(pattern)) {
			return null;
		}
		DateFormat df = new SimpleDateFormat(pattern);
        df.setTimeZone(DEFAULT_TIME_ZONE);
		return df.format(date);
	}

	public static void main(String[] args) {
	    System.out.println(DateUtil.formatSimpleTimestamp(new Date()));
    }
}
