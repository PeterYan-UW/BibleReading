package com.afc.biblereading;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import junit.framework.Protectable;

import org.joda.time.DateTime;

import com.afc.biblereading.R;
import com.afc.biblereading.adapter.CustomCheckboxAdapter;
import com.afc.biblereading.group.Group;
import com.afc.biblereading.helper.DataHolder;
import com.afc.biblereading.helper.DialogUtils;
import com.afc.biblereading.helper.util;
import com.afc.biblereading.user.CreateSessionActivity;
import com.afc.biblereading.user.UserActivity;
import com.quickblox.core.QBCallback;
import com.quickblox.core.QBEntityCallbackImpl;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.result.Result;
import com.quickblox.customobjects.QBCustomObjects;
import com.quickblox.customobjects.model.QBCustomObject;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


@SuppressLint("SimpleDateFormat")
public class CalenderActivity extends FragmentActivity{
	private boolean undo = false;
	LocalDataManage DOP;
	private Date startDay = null;
	private Date endDay = null;
	private CaldroidFragment caldroidFragment;
	private CaldroidFragment dialogCaldroidFragment;
	private int index;
	public static Date targetDay;
	ArrayList<Task> todayTaskList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calender);
		setTodayTask();
		
		// Setup caldroid fragment
		final SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
		caldroidFragment = new CaldroidFragment();
		refreshCalender();		
		// If Activity is created after rotation
		if (savedInstanceState != null) {
			caldroidFragment.restoreStatesFromKey(savedInstanceState,
					"CALDROID_SAVED_STATE");
		}
		// If activity is created from fresh
		else {
			Bundle args = new Bundle();
			Calendar cal = Calendar.getInstance();
			args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
			args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
			args.putBoolean(CaldroidFragment.ENABLE_SWIPE, true);
			args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, true);
			caldroidFragment.setArguments(args);
		}

		// Attach to the activity
		FragmentTransaction t = getSupportFragmentManager().beginTransaction();
		t.replace(R.id.calendar, caldroidFragment);
		t.commit();
		
		final Intent intent = new Intent(this, DailyMission.class);
		
		// Setup listener
		final CaldroidListener listener = new CaldroidListener() {
			
			@Override
			public void onSelectDate(Date date, View view) {
				targetDay = date;	
		    	startActivity(intent);
			}

			@Override
			public void onLongClickDate(Date date, View view) {
				Toast.makeText(getApplicationContext(),
						"Long click " + formatter.format(date),
						Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onCaldroidViewCreated() {
				if (caldroidFragment.getLeftArrowButton() != null) {
					Toast.makeText(getApplicationContext(),
							"Caldroid view is created", Toast.LENGTH_SHORT)
							.show();
				}
			}

		};

		// Setup Caldroid
		caldroidFragment.setCaldroidListener(listener);

		// Get Today's Task
		createScheduledNotification();
		
	}
	
	private void setTodayTask() {
		// Setup today's task title		
		Typeface face0 = Typeface.createFromAsset(getAssets(),"fonts/fonts1.TTF");
        TextView TodayMissionTitle = (TextView) findViewById(R.id.TodayMissionTitle);
        TodayMissionTitle.setTypeface(face0);

		// Setup today's task list
        int plan_id = -1;
		DOP = ((ApplicationSingleton)getApplication()).getDataBase();
		final ArrayList<HashMap<String, Object>> plans = DOP.getPlanInfo(DOP);
		
		plan_id = (Integer) plans.get(0).get("plan_id");
		Log.v((String) plans.get(0).get("start_day"),"chapternum");
		startDay = util.formatDateTime(this, (String) plans.get(0).get("start_day"));
		endDay = util.formatDateTime(this, (String) plans.get(0).get("end_day"));
		ArrayList<HashMap<String, Object>> todayTask = DOP.getTodayTask(DOP, 0, startDay);
		
		Log.d("today task return", todayTask.toString());
		
        ListView TodayMission = (ListView) findViewById(R.id.TodayMission);
        
        todayTaskList = util.DBTasks2Tasks(todayTask);
		CustomCheckboxAdapter dataAdapter = new CustomCheckboxAdapter(
				this, R.layout.single_item, todayTaskList);
		
		Log.v(Integer.toString(TodayMission.getCount()),"view");
		TodayMission.setAdapter(dataAdapter);
		TodayMission.setOnItemClickListener(new OnItemClickListener() {
			  
			  @Override
			  public void onItemClick(AdapterView<?> parent, View view,
			    int position, long id) {
				  	index = position;
				  	Task task = (Task) parent.getItemAtPosition(position);
				  	
				  	Toast.makeText(getApplicationContext(),
				  			"Clicked on Row: " + task.asString(),
				  			Toast.LENGTH_LONG).show();
			  }
		});
		
	}

	@Override
	protected void onResume(){
		super.onResume();
		refreshCalender();
	}
	
	private void refreshCalender(){
		ArrayList<Integer> Faildays = DOP.getUnfinishedDay(DOP, 0, startDay);
		HashMap<Date, Integer> backgroundForDateMap = util.initCalenderDates(
				new DateTime(endDay), new DateTime(startDay), Faildays, 
				R.color.failed, R.color.passed, R.color.future);
		caldroidFragment.setBackgroundResourceForDates(backgroundForDateMap);
		caldroidFragment.refreshView();
	}

	/**
	 * Save current states of the Caldroid here
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);

		if (caldroidFragment != null) {
			caldroidFragment.saveStatesToKey(outState, "CALDROID_SAVED_STATE");
		}

		if (dialogCaldroidFragment != null) {
			dialogCaldroidFragment.saveStatesToKey(outState,
					"DIALOG_CALDROID_SAVED_STATE");
		}
	}
	
	private void createScheduledNotification() {
	
		// Get new calendar object and set the date to now
	
		Calendar calendar = Calendar.getInstance();
	
		calendar.setTimeInMillis(System.currentTimeMillis());
	
		// Add defined amount of days to the date
	
		calendar.add(Calendar.SECOND, 10);
		
		// Retrieve alarm manager from the system
	
		AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(getBaseContext().ALARM_SERVICE);
	
	 	// Every scheduled intent needs a different ID, else it is just executed once
	
		int id = (int) System.currentTimeMillis();
	
	 	// Prepare the intent which should be launched at the date
	
	 	Intent intent = new Intent(this, TimeAlarm.class);	 
	
	 // Prepare the pending intent
	
	 	PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		
	 	// Register the alert in the system. You have the option to define if the device has to wake up on the alert or not
	 	Log.v("first","notify");
		//alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000*60, pendingIntent);
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.calender, menu);
        return true;
    }
    

	public void ResetDays(){
		Intent backMain = new Intent(this, MainActivity.class);
		LocalDataManage DOP = ((ApplicationSingleton)getApplication()).getDataBase();
		DOP.DeletePlan(DOP);
    	startActivity(backMain);    	
    }
	
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
        case R.id.reset:
        	ResetDays();
            return true;
        case R.id.user:
        	if (DataHolder.getDataHolder().getSignInQbUser() == null){
        		Intent user = new Intent(this, CreateSessionActivity.class);
        		startActivity(user);  
        	}
        	else{
        		Intent user = new Intent(this, UserActivity.class);
        		startActivity(user);          		
        	}
        default:
            return super.onOptionsItemSelected(item);
        }
    }
    	
	private Boolean exit = false;
	@Override
    public void onBackPressed() {
        if (exit) {
            DataHolder.getDataHolder().setSignInQbUser(null);
            DataHolder.getDataHolder().setSignInUserGroup(null);
            DataHolder.getDataHolder().setSignInUserQbGroup(null);
        	Intent intent = new Intent(Intent.ACTION_MAIN);
        	intent.addCategory(Intent.CATEGORY_HOME);
        	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        	startActivity(intent);
        } else {
            Toast.makeText(this, "Press Back again to Exit.",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);
        }

    }
	public void onClick(View view){
        switch (view.getId()) {
	        case R.id.CheckInTodayTask:
	        	if (DataHolder.getDataHolder().getSignInUserGroup() != null){
		        	ArrayList<HashMap<String, Object>> todayTask = DOP.getTodayTask(DOP, 0, startDay);
		        	ArrayList<Task> converTodayTask = util.DBTasks2Tasks(todayTask);
		        	String finished = "读完";
		        	int total = converTodayTask.size();
		        	int miss = 0;
		        	for (Task t : converTodayTask){
		        		if (t.getDone()){
		        			finished += t.asString()+" ";
		        		}
		        		else{
		        			miss++;
		        		}
		        	}
		        	final String checkInString = util.GenCheckInMessage(total, miss, finished);
	
		    		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		    		builder.setTitle("发表一下信息到小组");
		    		builder.setMessage(checkInString);
		    		builder.setPositiveButton("发表", new DialogInterface.OnClickListener() {
		    			@Override
		    			public void onClick(DialogInterface dialogInterface, int i) {
		    				checkInTodayTask(checkInString);
		    			}
		    		});
		    		builder.setNegativeButton("取消",null);
		    		builder.create().show();
	        	}
	    		break;
        }
	}
	
	private void checkInTodayTask(String message){
		QBCustomObject task = new QBCustomObject();
		task.putString("task", message);
		task.setClassName("DailyRecords");
		QBCustomObjects.createObject(task, new QBEntityCallbackImpl<QBCustomObject>() {
    	    @Override
    	    public void onSuccess(QBCustomObject createdObject, Bundle bundle) {
    	    }
    	 
    	    @Override
    	    public void onError(List<String> errors) {    	 
    	    }
    	});
	}
}
