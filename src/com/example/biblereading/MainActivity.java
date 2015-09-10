package com.example.biblereading;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

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
import android.widget.Button;


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
        if (value.equals("Yes")) {
        	Intent intent = new Intent(this, CalenderActivity.class);
        	
        	startActivity(intent);
        }
        else {
        	setContentView(R.layout.activity_main);
            
            Typeface face0 = Typeface.createFromAsset(getAssets(),"fonts/fonts1.TTF");
            TextView PickDateTitle = (TextView) findViewById(R.id.PickDateTitle);
            PickDateTitle.setTypeface(face0);
            
            Typeface face1 = Typeface.createFromAsset(getAssets(),"fonts/fonts2.TTF");
            TextView StartDateText = (TextView) findViewById(R.id.StartDateText);
            StartDateText.setTypeface(face1);
            TextView EndDateText = (TextView) findViewById(R.id.EndDateText);
            EndDateText.setTypeface(face1);
        }
        
        
        
        
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
    	DatePicker StartDateValue = (DatePicker) findViewById(R.id.StartDateValue);
        DatePicker EndDateValue = (DatePicker) findViewById(R.id.EndDateValue);
        

        DateTime startDate = new DateTime(StartDateValue.getYear(), StartDateValue.getMonth()+1, StartDateValue.getDayOfMonth(), 0, 0, 0, 0);
        DateTime endDate = new DateTime(EndDateValue.getYear(), EndDateValue.getMonth()+1, EndDateValue.getDayOfMonth(), 0, 0, 0, 0);

        int TotalReadingDays = Days.daysBetween(startDate, endDate).getDays()+1;
         
        Log.v(Integer.toString(TotalReadingDays),"timetime");
        Log.v(Integer.toString(1189/TotalReadingDays),"PAGES");
        Log.v(Integer.toString(1189%TotalReadingDays),"remainder");
        
        Map<Integer, Vector<String>> Days_StartToEnd = new HashMap<Integer, Vector<String>>();
        
        int ChaptersDaily = 1189/TotalReadingDays;
        int BackUp_ChaptersDaily = ChaptersDaily;
        int RemainderPages = 1189%TotalReadingDays;
        int StartBook = 0;
        int StartChapter = 1;
        int EndBook = 0;
        int EndChapter = 1;
        BibleIndex Bibleindex = new BibleIndex();
        for(int CurrentDay=1; CurrentDay<=TotalReadingDays; CurrentDay++) {
        	Vector<String> Book_Chapter = new Vector<String>();
        	ChaptersDaily = BackUp_ChaptersDaily;
        	if (CurrentDay<=RemainderPages) {
        		for (int CurrentBook=StartBook; CurrentBook<66; CurrentBook++) {
        			int CurrentBookChaptersNum = Bibleindex.BibleChapterNum[StartBook];
        			if (CurrentBookChaptersNum-StartChapter>=ChaptersDaily) {
        				EndBook = StartBook;
        				EndChapter = StartChapter+ChaptersDaily;
        				Book_Chapter.addElement(Bibleindex.BibleBookName[CurrentBook]+" 第"+Integer.toString(StartChapter)+"章-第"+Integer.toString(EndChapter)+"章");
        				StartChapter = EndChapter+1;
        				break; 
        			}
        			else if (CurrentBookChaptersNum<StartChapter) {
        				StartBook = CurrentBook+1;
        				StartChapter = 1;
        			}
        			else {
        				EndBook = StartBook;
        				EndChapter = Bibleindex.BibleChapterNum[CurrentBook];
        				Book_Chapter.addElement(Bibleindex.BibleBookName[CurrentBook]+" 第"+Integer.toString(StartChapter)+"章-第"+Integer.toString(EndChapter)+"章");
        				ChaptersDaily = ChaptersDaily - (EndChapter-StartChapter) - 1;
        				StartBook++;
        				StartChapter = 1;
        				
        			}
        		}
        		Days_StartToEnd.put(CurrentDay,Book_Chapter);
        	}
        	else {
        		for (int CurrentBook=StartBook; CurrentBook<66; CurrentBook++) {
        			int CurrentBookChaptersNum = Bibleindex.BibleChapterNum[StartBook];
        			if (CurrentBookChaptersNum-StartChapter>=ChaptersDaily-1) {
        				EndBook = StartBook;
        				EndChapter = StartChapter+ChaptersDaily-1;
        				Book_Chapter.addElement(Bibleindex.BibleBookName[CurrentBook]+" 第"+Integer.toString(StartChapter)+"章-第"+Integer.toString(EndChapter)+"章");
        				StartChapter = EndChapter+1;
        				break; 
        			}
        			else if (CurrentBookChaptersNum<StartChapter) {
        				StartBook = CurrentBook+1;
        				StartChapter = 1;
        			}
        			else {
        				EndBook = StartBook;
        				EndChapter = Bibleindex.BibleChapterNum[CurrentBook];
        				Book_Chapter.addElement(Bibleindex.BibleBookName[CurrentBook]+" 第"+Integer.toString(StartChapter)+"章-第"+Integer.toString(EndChapter)+"章");
        				ChaptersDaily = ChaptersDaily - (EndChapter-StartChapter) - 1;
        				StartBook++;
        				StartChapter = 1;
        			}
        		}
        		Days_StartToEnd.put(CurrentDay,Book_Chapter);
        	}
        }
        for(int i=1; i<=TotalReadingDays; i++) {
        	Vector<String> STE = Days_StartToEnd.get(i);
        	Log.v(Integer.toString(i),"ChapterDay");
        	for (int j = 0; j < STE.size(); j++) {
        		
        		Log.v(STE.get(j),"Chapter");
        	}
        }
        
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("Set?", "Yes");

        // Commit the edits!
        editor.commit();
        
    	startActivity(intent);
    }
    
}
