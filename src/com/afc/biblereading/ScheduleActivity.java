package com.afc.biblereading;

import static com.afc.biblereading.user.definitions.Consts.PREFS_NAME;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.joda.time.DateTime;

import com.afc.biblereading.R;
import com.afc.biblereading.helper.DataHolder;
import com.afc.biblereading.helper.util;
import com.afc.biblereading.user.BaseActivity;
import com.afc.biblereading.user.CreateSessionActivity;
import com.afc.biblereading.user.UserActivity;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;
import android.graphics.Typeface;
import android.widget.TextView;


public class ScheduleActivity extends BaseActivity {
	public static final String PREFS_NAME = "MyPrefsFile";
	public static PendingIntent pendingIntent;
	public static AlarmManager alarmManager;
	public static String dontnotify = "No";

	private RelativeLayout PickBookLayout;
	private TextView PickBookTitle;
	private Spinner startSpinner;
	private Spinner endSpinner;
	private String beginBook;
	private String endBook;
	private int beginBookIndex;
	private int endBookIndex;

	private RelativeLayout PickDateLayout;
	private TextView PickDateTitle;
	private TextView StartDateText;
	private DatePicker StartDateValue;
	private TextView EndDateText;
	private DatePicker EndDateValue;
	private DateTime startDate;
	private DateTime endDate;
	
	private LocalDataManage DOP;
	private Boolean exit = false;
	private BibleIndex bibleIndex = new BibleIndex();
	private List<String> bookList = Arrays.asList(bibleIndex.BibleBookName);
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        DOP = ((ApplicationSingleton)getApplication()).getDataBase();
        
		ArrayList<HashMap<String, Object>> result = DOP.getPlanInfo(DOP);
		Log.d("database plan return", String.valueOf(result.size()));
		Log.d("database plan return", result.toString());

    	Log.v("main activity", String.valueOf(result.size()));
    	
    	SharedPreferences alarm = getSharedPreferences(PREFS_NAME, 0);
        String checkout = alarm.getString("checkout", "No");
        dontnotify = checkout;
    	
        if (result.size()>0) {
        	Intent intent = new Intent(this, Tabs.class);
        	startActivity(intent);
        }
        else {
        	Log.v("main activity", "go set date");
        	setContentView(R.layout.activity_schedule);
            initUI();    
            setUpSpinner();
        }
    }

    private void setUpSpinner() {
    	startSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				beginBook = (String) parent.getItemAtPosition(pos);
				beginBookIndex = bookList.indexOf(beginBook);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
    	endSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				endBook = (String) parent.getItemAtPosition(pos);
				endBookIndex = bookList.indexOf(endBook);				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}

	private void initUI() {
        Typeface face0 = Typeface.createFromAsset(getAssets(),"fonts/fonts1.TTF");
        Typeface face1 = Typeface.createFromAsset(getAssets(),"fonts/fonts2.TTF");
		
        PickBookLayout = (RelativeLayout) findViewById(R.id.pick_book_layout);
        PickDateLayout = (RelativeLayout) findViewById(R.id.pick_date_layout);
        
        PickDateTitle = (TextView) findViewById(R.id.PickDateTitle);
        PickBookTitle = (TextView) findViewById(R.id.PickBookTitle);
        PickDateTitle.setTypeface(face0);
        PickBookTitle.setTypeface(face0);
        
        StartDateText = (TextView) findViewById(R.id.StartDateText);
        EndDateText = (TextView) findViewById(R.id.EndDateText);
        StartDateText.setTypeface(face1);
        EndDateText.setTypeface(face1);

		startSpinner = (Spinner) findViewById(R.id.start_spinner);
		endSpinner = (Spinner) findViewById(R.id.end_spinner);	
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
		        R.array.book_array_chinese, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		startSpinner.setAdapter(adapter);
		endSpinner.setAdapter(adapter);
		
    	StartDateValue = (DatePicker) findViewById(R.id.StartDateValue);
        EndDateValue = (DatePicker) findViewById(R.id.EndDateValue);   
	}
    

    @Override
	protected void onResume(){
		super.onResume();
		if (DataHolder.getDataHolder().getSignInQbUser()== null && util.isNetworkAvailable(this)){
    		Intent user = new Intent(this, CreateSessionActivity.class);
    		startActivity(user);  			
		}
    }
    
	public void nextStept(View view){		
		if (endBookIndex < beginBookIndex){
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("请选择\""+beginBook+"\"之后的书作为结束");
			builder.setPositiveButton("了解", null);
			builder.create().show();
		}
		else{
			PickBookLayout.setVisibility(View.GONE);
			PickDateLayout.setVisibility(View.VISIBLE);
		}
	}
	
	public void stepBack(View view){
		PickBookLayout.setVisibility(View.VISIBLE);
		PickDateLayout.setVisibility(View.GONE);
	}
	
    public void startReading(View view){
    	progressDialog.show();
		// TODO: add space for plan name, default 'reading plan'+id, cant be empty"
		String planName = "reading plan 1";     

        startDate = new DateTime(StartDateValue.getYear(), StartDateValue.getMonth()+1, StartDateValue.getDayOfMonth(),0,0,0,0);
        endDate = new DateTime(EndDateValue.getYear(), EndDateValue.getMonth()+1, EndDateValue.getDayOfMonth(),0,0,0,0);
        CreateReadingPlan.CreatePlan(DOP, planName, beginBookIndex, endBookIndex, startDate, endDate, this);

        Calendar calendar = Calendar.getInstance();
    
		calendar.set(Calendar.HOUR_OF_DAY, 7);
		calendar.set(Calendar.MINUTE,59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 0);
		
		
		SharedPreferences alarm = getSharedPreferences(PREFS_NAME, 0);
        String setalarm = alarm.getString("alarm", "No");
        SharedPreferences.Editor editor = alarm.edit();
		if (setalarm.equals("No")) {
        
        
			alarmManager = (AlarmManager) getApplicationContext().getSystemService(getBaseContext().ALARM_SERVICE);
		
			int id = (int) System.currentTimeMillis();
	
			Intent intent0 = new Intent(this, TimeAlarm.class);	 
	
			pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), id, intent0, PendingIntent.FLAG_UPDATE_CURRENT);

			Log.v("first","notify2");

        	alarmManager.setInexactRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), 24*60*60*1000, pendingIntent);
			//alarmManager.setInexactRepeating(AlarmManager.RTC, 0, 10000, pendingIntent);

			
			editor.putString("alarm", "Yes ");
			
		}
		dontnotify = "No";
		editor.putString("checkout", "No");
		editor.commit();
        
        Intent intent = new Intent(this, Tabs.class);
    	startActivity(intent);
    }
    
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
}
