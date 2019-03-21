package com.x.utils;

import org.apache.commons.lang.time.DateFormatUtils;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;

public class DateTimeUtil {
	public static final String DATE_PARTTEN = "yyyy-MM-dd";
	public static final String TIME_PARTTEN = "HH:mm:ss";
	public static final String DAYTIME_PARTTEN = "yyyy-MM-dd HH:mm:ss";
	public static final String MILLIONSECONDS_PARTTEN = "yyyy-MM-dd HH:mm:ss.SSS";
	public static final String DAYTIME_PARTTEN2 = "yyyyMMddHHmmss";

	public static final String MONTH_FIRST_DATE = "MONTH_FIRST_DATE";
	public static final String MONTH_LAST_DATE = "MONTH_LAST_DATE";

	/**
	 * 获取系统当前日期 yyyy-MM-dd
	 * 
	 * @return
	 */
	public static String getToday() {
		return DateFormatUtils.format(System.currentTimeMillis(), DATE_PARTTEN);
	}

	/**
	 * 获取系统当前时间 HH:mm:ss
	 * 
	 * @return
	 */
	public static String getCurTime() {
		return DateFormatUtils.format(System.currentTimeMillis(), TIME_PARTTEN);
	}

	/**
	 * 获取系统当前日期和时间 yyyy-MM-dd HH:mm:ss
	 * 
	 * @return
	 */
	public static String getCurDayTime() {
		return DateFormatUtils.format(System.currentTimeMillis(), DAYTIME_PARTTEN);
	}

	/**
	 * 获取系统当前日期和时间具体到毫秒 yyyy-MM-dd HH:mm:ss:SSS
	 *
	 * @return
	 */
	public static String getCurMillionSecondsTime() {
		return DateFormatUtils.format(System.currentTimeMillis(), MILLIONSECONDS_PARTTEN);
	}

	/**
	 * 按指定格式转化成字符串
	 * 
	 * @param date
	 * @param partten
	 * @return
	 */
	public static String date2Str(Date date, String pattern) {
		return DateFormatUtils.format(date, pattern);
	}

	/**
	 * 时间字符串转化成时间对象
	 * 
	 * @param strDate
	 * @param pattern
	 * @return
	 */
	public static Date str2Date(String strDate, String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		try {
			return sdf.parse(strDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 传入字符串，yyyy/mm/dd转换为yyyy-mm-dd格式出来
	 * 
	 * @param dates
	 * @return
	 */
	public static Date strSlashDate(String dates) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		Date date;
		try {
			date = format.parse(dates);
			format = new SimpleDateFormat("yyyy-MM-dd");
			String newD = format.format(date);
			ParsePosition pos = new ParsePosition(0);
			Date strtodate = format.parse(newD, pos);
			return strtodate;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // 有异常要捕获

		return null;

	}

	// 把字符串yyyymmdd转成时间格式yyyy-MM-dd格式
	public static Date formatDate(String str) {
		SimpleDateFormat sf1 = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat sf2 = new SimpleDateFormat("yyyy-MM-dd");
		try {
			String sfstr = sf2.format(sf1.parse(str));
			ParsePosition pos = new ParsePosition(0);
			return sf2.parse(sfstr, pos);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 计算两个日期差（相差几天）
	 * 
	 * @param begin
	 * @param end
	 * @return
	 */
	public static int dateDif(Date begin, Date end) {
		return (int) (msDiff(begin, end) / 1000l / 3600l / 24l);
	}

	/**
	 * 计算两个日期相差的分钟
	 * @param begin
	 * @param end
	 * @return
	 */
	public static int minutesDiff(Date begin, Date end) {
		return (int) (msDiff(begin, end) / 1000l / 60l);
	}

	/**
	 * 计算两个日期相差的秒数
	 * @param begin
	 * @param end
	 * @return
	 */
	public static int secondsDiff(Date begin, Date end) {
		return (int) (msDiff(begin, end) / 1000l);
	}

	/**
	 * 计算两个日期相差的毫秒数
	 * @param begin
	 * @param end
	 * @return
	 */
	public static long msDiff(Date begin, Date end) {
		long beginTime = begin.getTime();
		long endTime = end.getTime();
		long difTime = endTime - beginTime;
		return difTime;
	}



	/**
	 * 求得从某天开始，过了几年几月几日几时几分几秒后，日期是多少 几年几月几日几时几分几秒可以为负数
	 *
	 * @param date
	 *            Date
	 * @param year
	 *            int
	 * @param month
	 *            int
	 * @param day
	 *            int
	 * @param hour
	 *            int
	 * @param min
	 *            int
	 * @param sec
	 *            int
	 * @return Date
	 */
	public static Date modifyDate(Date date, int year, int month, int day, int hour, int min, int sec) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.YEAR, year);
		cal.add(Calendar.MONTH, month);
		cal.add(Calendar.DATE, day);
		cal.add(Calendar.HOUR, hour);
		cal.add(Calendar.MINUTE, min);
		cal.add(Calendar.SECOND, sec);

		return cal.getTime();
	}

	/**
	 * 获取日期所在月份的第一天日期／最后一天日期 MONTH_FIRST_DATE MONTH_LAST_DATE
	 *
	 * @param defined
	 * @return
	 */
	public static Date getDateByDefine(Date date, String define) {
		// defined : LAST_MONTHN_FIRST_DATE
		// defined : LAST_MONTHN_LAST_DATE
		Calendar c = new GregorianCalendar(TimeZone.getDefault(), Locale.getDefault());
		c.setTime(date);

		if (define.equalsIgnoreCase(MONTH_FIRST_DATE)) {
			// 设置这个月的第一天
			c.set(Calendar.DATE, 1);
			date = c.getTime();
		} else if (define.equalsIgnoreCase(MONTH_LAST_DATE)) {
			// 获取日期最后一天
			date = getDateByDefine(c.getTime(), MONTH_FIRST_DATE);
			date = modifyDate(date, 0, 1, 0, 0, 0, 0);
			date = modifyDate(date, 0, 0, -1, 0, 0, 0);
		}

		if (date == null) {
			return null;
		}
		return date;
	};
}
