package org.noname.wrapper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class Time {

	private static DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm");

	public static long unixTimestamp(int year, int month, int day, int hour, int minute) {

		long timeNumber = year * 100000000 + month * 1000000 + day * 10000 + hour * 100 + minute;
		String timeString = Long.toString(timeNumber);
		long unixTime = 0L;

		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC-5:00"));

		try {
			unixTime = dateFormat.parse(timeString).getTime();
			unixTime /= 1000L;
		} catch(ParseException pe) {
			pe.printStackTrace();
		}

		return unixTime;

	}

}
