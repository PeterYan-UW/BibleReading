package com.afc.biblereading;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import org.joda.time.DateTime;
import org.joda.time.Days;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class DailyMission extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dailymission);
		populateListView();
	}
	
	private void populateListView() {
		BibleIndex bibleindex = new BibleIndex();
		
		ArrayList<HashMap<String, Object>> plans = CalenderActivity.DOP.getPlanInfo(CalenderActivity.DOP);
		Date startDay = utils.formatDateTime(this, (String) plans.get(0).get("start_day"));
		ArrayList<HashMap<String, Object>> SpecificDayTask = CalenderActivity.DOP.getDailyTask(CalenderActivity.DOP, 0, CalenderActivity.targetDay,startDay);
		
    	int BooksNumToday=SpecificDayTask.size();
    	final String[] s = new String[BooksNumToday];
		for (int i=0;i<BooksNumToday;i++) {
			
			String start_chapter = SpecificDayTask.get(i).get("start_chapter").toString();
			String end_chapter = SpecificDayTask.get(i).get("end_chapter").toString();
			if (start_chapter.equals(end_chapter)) {
				Log.v("here1","here");
				s[i]=bibleindex.BibleBookName[Integer.parseInt(SpecificDayTask.get(i).get("book").toString())]+"  ตฺ"+start_chapter+"ีย";
			}
			else {
				Log.v("here2","here");
				s[i]=bibleindex.BibleBookName[Integer.parseInt(SpecificDayTask.get(i).get("book").toString())]+"  ตฺ"+start_chapter+"-"+end_chapter+"ีย";
			}
			
			
		}
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				this,
				R.layout.single_item,
				s);
		ListView list = (ListView) findViewById(R.id.listView1);
		list.setAdapter(adapter);
		}
}
