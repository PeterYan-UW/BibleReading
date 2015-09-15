package com.afc.biblereading;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.joda.time.DateTime;

import android.content.Context;
import android.net.ParseException;

public class utils {
	public static String getDateTime(DateTime date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return dateFormat.format(date.toDate());
	}
	
	public static String printDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyyƒÍMM‘¬dd»’", Locale.getDefault());
        return dateFormat.format(date);
	}
	
	public static Date formatDateTime(Context context, String timeToFormat) {
	    SimpleDateFormat iso8601Format = new SimpleDateFormat(
	            "yyyy-MM-dd HH:mm:ss");
	    Date date = null;
	    if (timeToFormat != null) {
            try {
				date = iso8601Format.parse(timeToFormat);
			} catch (java.text.ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
		return date;
	}
}
