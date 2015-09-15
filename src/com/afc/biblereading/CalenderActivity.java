package com.afc.biblereading;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import com.afc.biblereading.R;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


@SuppressLint("SimpleDateFormat")
public class CalenderActivity extends FragmentActivity{
	private boolean undo = false;
	private CaldroidFragment caldroidFragment;
	private CaldroidFragment dialogCaldroidFragment;
	private int index;
	public static Date targetDay;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calender);

		final SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");

		// Setup today's task title		
		Typeface face0 = Typeface.createFromAsset(getAssets(),"fonts/fonts1.TTF");
        TextView TodayMissionTitle = (TextView) findViewById(R.id.TodayMissionTitle);
        TodayMissionTitle.setTypeface(face0);

		// Setup today's task list
        int plan_id = -1;
		Date startDay = null;
		LocalDataManage DOP = ((ApplicationSingleton)getApplication()).getDataBase();
		final ArrayList<HashMap<String, Object>> plans = DOP.getPlanInfo(DOP);
		
		plan_id = (Integer) plans.get(0).get("plan_id");
		Log.v((String) plans.get(0).get("start_day"),"chapternum");
		startDay = utils.formatDateTime(this, (String) plans.get(0).get("start_day"));
		ArrayList<HashMap<String, Object>> todayTask = DOP.getTodayTask(DOP, 0, startDay);
		
		Log.d("today task return", todayTask.toString());
		int BooksNumToday = todayTask.size();
		
        ListView TodayMission = (ListView) findViewById(R.id.TodayMission);
        
        ArrayList<Task> taskList = new ArrayList<Task>();
		for (int i=0;i<BooksNumToday;i++) {
			int id = (Integer) todayTask.get(i).get("id");
			int book = (Integer) todayTask.get(i).get("book");
			int start_chapter = (Integer) todayTask.get(i).get("start_chapter");
			int end_chapter = (Integer) todayTask.get(i).get("end_chapter");
			Boolean done = (Integer) todayTask.get(i).get("status") == 1;			
			Task task = new Task(id, book, start_chapter, end_chapter, done);
			taskList.add(task);
		}
		CustomCheckboxAdapter dataAdapter = new CustomCheckboxAdapter(
				this, R.layout.single_item, taskList);
		
		Log.v(Integer.toString(TodayMission.getCount()),"view");
		TodayMission.setAdapter(dataAdapter);
//		TodayMission.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
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
		
		// Setup caldroid fragment
		caldroidFragment = new CaldroidFragment();

		// Setup arguments

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
//				Toast.makeText(getApplicationContext(), formatter.format(date),
//						Toast.LENGTH_SHORT).show();
				targetDay = date;	
		    	startActivity(intent);
//				DailyMission.dailyTitle.setText("test");
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
	
	public void ResetDays(View view){
    	Intent intent = new Intent(this, MainActivity.class);
    	SharedPreferences settings = getSharedPreferences(MainActivity.PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("Set?", "No");
        editor.commit();
        LocalDataManage DOP = new LocalDataManage(this);
		DOP.DeletePlan(DOP);
    	startActivity(intent);
    	
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
	
	private Boolean exit = false;
	@Override
    public void onBackPressed() {
        if (exit) {
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
}
