package com.afc.biblereading;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

public class DailyMission extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dailymission);
		populateListView();
	}
	
	private void populateListView() {		
		LocalDataManage DOP = ((ApplicationSingleton)getApplication()).getDataBase();
		ArrayList<HashMap<String, Object>> plans = DOP.getPlanInfo(DOP);
		Date startDay = utils.formatDateTime(this, (String) plans.get(0).get("start_day"));
		ArrayList<HashMap<String, Object>> SpecificDayTask = DOP.getDailyTask(DOP, 0, CalenderActivity.targetDay,startDay);
		
		ArrayList<Task> taskList = new ArrayList<Task>();
		for (int i=0; i<SpecificDayTask.size();i++) {
			int id = (Integer) SpecificDayTask.get(i).get("id");
			int book = (Integer) SpecificDayTask.get(i).get("book");
			int start_chapter = (Integer) SpecificDayTask.get(i).get("start_chapter");
			int end_chapter = (Integer) SpecificDayTask.get(i).get("end_chapter");
			Boolean done = (Integer) SpecificDayTask.get(i).get("status") == 1;			
			Task task = new Task(id, book, start_chapter, end_chapter, done);
			taskList.add(task);
		}

		CustomCheckboxAdapter dataAdapter = new CustomCheckboxAdapter(
				this, R.layout.single_item, taskList);
		ListView list = (ListView) findViewById(R.id.listView1);
		list.setAdapter(dataAdapter);
	}
}
