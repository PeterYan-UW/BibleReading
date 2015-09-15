package com.afc.biblereading;

import java.awt.font.TextAttribute;
import java.text.AttributedString;
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
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


@SuppressLint("SimpleDateFormat")
public class CalenderActivity extends FragmentActivity{
	private boolean undo = false;
	private CaldroidFragment caldroidFragment;
	private CaldroidFragment dialogCaldroidFragment;
	private int index;
	public static LocalDataManage DOP;
	public static Date targetDay;
	private void setCustomResourceForDates() {
		Calendar cal = Calendar.getInstance();

		// Min date is last 7 days
		cal.add(Calendar.DATE, -18);
		Date blueDate = cal.getTime();

		// Max date is next 7 days
		cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 16);
		Date greenDate = cal.getTime();

		if (caldroidFragment != null) {
			caldroidFragment.setBackgroundResourceForDate(R.color.blue,
					blueDate);
			caldroidFragment.setBackgroundResourceForDate(R.color.green,
					greenDate);
			caldroidFragment.setTextColorForDate(R.color.white, blueDate);
			caldroidFragment.setTextColorForDate(R.color.white, greenDate);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calender);

		final SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");

		// Setup caldroid fragment
		// **** If you want normal CaldroidFragment, use below line ****
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

			// Uncomment this to customize startDayOfWeek
			// args.putInt(CaldroidFragment.START_DAY_OF_WEEK,
			// CaldroidFragment.TUESDAY); // Tuesday
			caldroidFragment.setArguments(args);
		}

		setCustomResourceForDates();

		// Attach to the activity
		FragmentTransaction t = getSupportFragmentManager().beginTransaction();
		t.replace(R.id.calendar, caldroidFragment);
		t.commit();
		
		final Intent intent = new Intent(this, DailyMission.class);
		
		int plan_id = -1;
		int BooksNumToday=0;
		Date startDay = null;
		DOP = new LocalDataManage(this);
		final ArrayList<HashMap<String, Object>> plans = DOP.getPlanInfo(DOP);
		if (plans.size()==1){
			plan_id = Integer.parseInt((String) plans.get(0).get("plan_id"));
			Log.v((String) plans.get(0).get("start_day"),"chapternum");
			startDay = utils.formatDateTime(this, (String) plans.get(0).get("start_day"));
			ArrayList<HashMap<String, Object>> todayTask = DOP.getTodayTask(DOP, 0, startDay);
//			Log.d("database task return", String.valueOf(taskResult.size()));
			Log.d("today task return", todayTask.toString());
			Log.v(Integer.toString(todayTask.size()),"task");
			BooksNumToday=todayTask.size();
		}
		
		// Setup listener
		final CaldroidListener listener = new CaldroidListener() {
			
			@Override
			public void onSelectDate(Date date, View view) {
				Toast.makeText(getApplicationContext(), formatter.format(date),
						Toast.LENGTH_SHORT).show();
				targetDay = date;
				
		    	startActivity(intent);
			}

			@Override
			public void onChangeMonth(int month, int year) {
				String text = "month: " + month + " year: " + year;
				Toast.makeText(getApplicationContext(), text,
						Toast.LENGTH_SHORT).show();
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

		createScheduledNotification();
		
		Typeface face0 = Typeface.createFromAsset(getAssets(),"fonts/fonts1.TTF");
        TextView TodayMissionTitle = (TextView) findViewById(R.id.TodayMissionTitle);
        TodayMissionTitle.setTypeface(face0);
		
        ListView TodayMission = (ListView) findViewById(R.id.TodayMission);
        TodayMission.setBackgroundColor(Color.WHITE);
        
		final String[] s = new String[BooksNumToday];
		for (int i=0;i<BooksNumToday;i++) {
			ArrayList<HashMap<String, Object>> todayTask = DOP.getTodayTask(DOP, 0, startDay);
			BibleIndex bibleindex = new BibleIndex();
			String start_chapter = todayTask.get(i).get("start_chapter").toString();
			String end_chapter = todayTask.get(i).get("end_chapter").toString();
			if (start_chapter.equals(end_chapter)) {
				Log.v("here1","here");
				s[i]=bibleindex.BibleBookName[Integer.parseInt(todayTask.get(i).get("book").toString())]+"  µÚ"+start_chapter+"ÕÂ";
			}
			else {
				Log.v("here2","here");
				s[i]=bibleindex.BibleBookName[Integer.parseInt(todayTask.get(i).get("book").toString())]+"  µÚ"+start_chapter+"-"+end_chapter+"ÕÂ";
			}
			
			
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				this,
				R.layout.single_item,
				s);
		
		Log.v(Integer.toString(TodayMission.getCount()),"view");
		TodayMission.setAdapter(adapter);
		TodayMission.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		TodayMission.setOnItemClickListener(new OnItemClickListener() {
			  
			  @Override
			  public void onItemClick(AdapterView<?> parent, View view,
			    int position, long id) {
				  	index = position;
				  	String name = s[position];
				  	
				  	AttributedString as = new AttributedString(name);
				  	as.addAttribute(TextAttribute.STRIKETHROUGH,
				  	        TextAttribute.STRIKETHROUGH_ON);
				  	((TextView) view).setText(name+"¡Ì");
				  	s[position] = name+"¡Ì";
//				  	if (view.getContext().toString().equals(name)) {
//				  		Log.v("yes","test");
//				  	}
//				  	else {
//				  		Log.v("no","test");
//				  	}
//				  	ListView LV = (ListView) findViewById(R.id.TodayMission);
//				  	View v = (View) LV.getChildAt(position);
//				  	
//					v.setBackgroundColor(Color.GREEN);
			  }
		});
		
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
	
}
