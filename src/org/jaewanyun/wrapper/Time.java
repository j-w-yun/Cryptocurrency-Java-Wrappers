package org.jaewanyun.wrapper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * Time manager
 */
public class Time {

	private static DateFormat dateFormat;

	static {dateFormat = new SimpleDateFormat("yyyyMMddHHmm");}


	/**
	 * Returns a UNIX timestamp of time specified
	 * @param year Year
	 * @param month Month
	 * @param day Day
	 * @param hour Hour
	 * @param minute Minute
	 * @param timeZone Timezone; e.g. "UTC-5:00"
	 * @return Time in UNIX timestamp
	 */
	public static long unixTimestamp(int year, int month, int day, int hour, int minute, String timeZone) {

		long timeNumber = (year * 100000000L) + (month * 1000000) + (day * 10000) + (hour * 100) + (minute);
		String timeString = Long.toString(timeNumber);
		long unixTime = 0L;

		dateFormat.setTimeZone(TimeZone.getTimeZone(timeZone));

		try {
			unixTime = dateFormat.parse(timeString).getTime();
			unixTime /= 1000L;
		} catch(ParseException pe) {
			pe.printStackTrace();
		}

		return unixTime;

	}

}
