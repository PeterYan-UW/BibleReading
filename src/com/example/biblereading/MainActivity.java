package com.example.biblereading;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.joda.time.DateTime;
import org.joda.time.Days;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.graphics.Typeface;
import android.widget.TextView;


public class MainActivity extends Activity {
	public static final String PREFS_NAME = "MyPrefsFile";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        String value = settings.getString("Set?", "No");
        Log.v("here i am".split("\\s+")[0],"split");
        Log.v("here i am".split("\\s+")[1],"split");
        Log.v("here i am".split("\\s+")[2],"split");
        
        LocalDataManage DOP = new LocalDataManage(this);
		ArrayList<HashMap<String, Object>> result = DOP.getPlanInfo(DOP);
		Log.d("database plan return", String.valueOf(result.size()));
		Log.d("database plan return", result.toString());
		
		ArrayList<HashMap<String, Object>> taskResult = DOP.getTaskInfo(DOP);
		Log.d("database task return", String.valueOf(taskResult.size()));
		Log.d("database task return", taskResult.toString());
		
//        if (value.equals("Yes")) {
//        	setContentView(R.layout.activity_calender);
//        }
//        else {
        	setContentView(R.layout.activity_main);
            
            Typeface face0 = Typeface.createFromAsset(getAssets(),"fonts/fonts1.TTF");
            TextView PickDateTitle = (TextView) findViewById(R.id.PickDateTitle);
            PickDateTitle.setTypeface(face0);
            
            Typeface face1 = Typeface.createFromAsset(getAssets(),"fonts/fonts2.TTF");
            TextView StartDateText = (TextView) findViewById(R.id.StartDateText);
            StartDateText.setTypeface(face1);
            TextView EndDateText = (TextView) findViewById(R.id.EndDateText);
            EndDateText.setTypeface(face1);
//        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    public void startReading(View view){
    	Intent intent = new Intent(this, CalenderActivity.class);

		// TODO: add space for plan name, default 'reading plan'+id, cant be empty"
		String planName = "reading plan 1";
    	DatePicker StartDateValue = (DatePicker) findViewById(R.id.StartDateValue);
        DatePicker EndDateValue = (DatePicker) findViewById(R.id.EndDateValue);        

        Date startDate = new Date(StartDateValue.getYear(), StartDateValue.getMonth()+1, StartDateValue.getDayOfMonth());
        Date endDate = new Date(EndDateValue.getYear(), EndDateValue.getMonth()+1, EndDateValue.getDayOfMonth());
        
        CreateReadingPlan.CreatePlan(planName, startDate, endDate, this);
        
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("Set?", "Yes");

        // Commit the edits!
        editor.commit();
        
    	startActivity(intent);
    }
}
