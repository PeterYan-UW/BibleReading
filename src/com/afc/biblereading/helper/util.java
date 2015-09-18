package com.afc.biblereading.helper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

import org.joda.time.DateTime;
import org.joda.time.Days;

import com.afc.biblereading.group.Group;
import com.quickblox.customobjects.model.QBCustomObject;

import android.content.Context;
import android.net.ParseException;

public class util {
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
	
	public static HashMap<Date, Integer> initCalenderDates(DateTime enday, DateTime startday, 
			ArrayList<Integer> failedDays, int failColor, int passColor, int futureColor){
		HashMap<Date, Integer> eventDates = new HashMap<Date, Integer>();
		int beforeToday = Days.daysBetween(startday, new DateTime()).getDays();
		int afterToday = Days.daysBetween(new DateTime(), enday).getDays()+1;
		int days = 0;
		for(; days<beforeToday; days++){
			DateTime dt = startday.plusDays(days);
			Date d = dt.toDate();
			if (failedDays.contains(days+1)){
				eventDates.put(d, failColor);				
			}
			else {
				eventDates.put(d, passColor);				
			}
		}
		days++;
		for(; days<=afterToday+beforeToday; days++){
			DateTime dt = startday.plusDays(days);
			Date d = dt.toDate();
			if (failedDays.contains(days+1)){
				eventDates.put(d, futureColor);				
			}
			else {
				eventDates.put(d, passColor);				
			}
		}
		return eventDates;
	}
	
	public static Group QBGroup2Group(QBCustomObject QBGroup){
		HashMap<String, Object> fields = QBGroup.getFields();
		String ID = (String) fields.get("id");
		String name = (String) fields.get("group_name");
		ArrayList<Integer> members = (ArrayList<Integer>) fields.get("members");
		Group group = new Group(ID, name, members);
		return group;
	}
}
