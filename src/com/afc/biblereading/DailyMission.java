package com.afc.biblereading;

import android.app.Activity;
import android.os.Bundle;
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
		String[] s = {"b","a","c","d","a","c","d","a","c","d","a","c","d","a","c","d","a","c","d","a","c","d"};
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				this,
				R.layout.single_item,
				s);
		ListView list = (ListView) findViewById(R.id.listView1);
		list.setAdapter(adapter);
		}
}
