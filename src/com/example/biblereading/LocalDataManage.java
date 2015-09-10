package com.example.biblereading;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.joda.time.DateTime;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class LocalDataManage extends SQLiteOpenHelper{

	private static final int DATABASE_VERSION = 3;
	private static final String DATABASE_NAME = "BibleReading.db";
    private static final String PLAN_TABLE = "ReadingPlan";
    private static final String DAILY_TASK_TABLE = "DailyTask";
//    private static final String  = "ReadingPlan";
    
    private static final String PLAN_TABLE_CREATE = "CREATE TABLE " + PLAN_TABLE + 
    		" (plan_name TEXT, start_date INTEGER, end_date INTEGER);";
    
    private static final String DAILY_TASK_TABLE_CREATE = " CREATE TABLE " + DAILY_TASK_TABLE + 
    		" (day start_chapter, plan_id INTEGER," +
    		" book INTEGER, start_chapter INTEGER, end_chapter INTEGER," +
    		" FOREIGN KEY(plan_id) REFERENCES " + PLAN_TABLE + "(rowid));";

    LocalDataManage(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
		Log.d("Database operations", "Database created");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(PLAN_TABLE_CREATE);
		Log.d("Database operations", "Plan Table created");
        db.execSQL(DAILY_TASK_TABLE_CREATE);
		Log.d("Database operations", "Task Table created");
    }

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
	
	public void putInfo(LocalDataManage ldm, String plan_name){
		SQLiteDatabase SQ = ldm.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("plan_name", plan_name);
		long k = SQ.insert(PLAN_TABLE, null, cv);
		Log.d("Database operations", "One raw inserted");		
	}
	
	public ArrayList<HashMap<String, Object>> getPlanInfo(LocalDataManage ldm){
		ArrayList<HashMap<String, Object>> planList; 
		planList = new ArrayList<HashMap<String, Object>>(); 
		String selectQuery = "SELECT rowid, * FROM "+PLAN_TABLE; 
		SQLiteDatabase SQ = ldm.getReadableDatabase();
		Cursor cursor = SQ.rawQuery(selectQuery, null); 
		if (cursor.moveToFirst()) { 
			do { 
				HashMap<String, Object> map = new HashMap<String, Object>(); 
				map.put("uniq_id", cursor.getString(0));
				map.put("plan_name", cursor.getString(1));
				map.put("start_day", cursor.getString(2));  
				map.put("end_day", cursor.getString(3));  
				planList.add(map); 
			} while (cursor.moveToNext()); 
		} // return contact list return wordList;
		return planList;
	}
	
	public ArrayList<HashMap<String, Object>> getTaskInfo(LocalDataManage ldm){
		ArrayList<HashMap<String, Object>> planList; 
		planList = new ArrayList<HashMap<String, Object>>(); 
		String selectQuery = "SELECT rowid, day, plan_id, book, start_chapter, end_chapter FROM "+DAILY_TASK_TABLE; 
		SQLiteDatabase SQ = ldm.getReadableDatabase();
		Cursor cursor = SQ.rawQuery(selectQuery, null); 
		if (cursor.moveToFirst()) { 
			do { 
				HashMap<String, Object> map = new HashMap<String, Object>(); 
				map.put("uniq_id", cursor.getString(0));
				map.put("day", cursor.getString(1));
				map.put("plan_id", cursor.getString(2));  
				map.put("book", cursor.getString(3));  
				map.put("start_chapter", cursor.getString(4));  
				map.put("end_chapter", cursor.getString(5));  
				planList.add(map); 
			} while (cursor.moveToNext()); 
		} // return contact list return wordList;
		return planList;
	}

	public void AddPlan(LocalDataManage ldm, String planName, Date startDate, Date endDate) {
		SQLiteDatabase SQ = ldm.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("plan_name", planName);
		cv.put("start_date", utils.getDateTime(startDate));
		cv.put("end_date", utils.getDateTime(endDate));
		long k = SQ.insert(PLAN_TABLE, null, cv);
		Log.d("Database operations", "One plan inserted");			
	}

	public void AddTask(LocalDataManage ldm, int planId, int day, int book,
			int startChapter, int endChapter) {
		SQLiteDatabase SQ = ldm.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("plan_id", planId);
		cv.put("day", day);
		cv.put("book", book);
		cv.put("start_chapter", startChapter);
		cv.put("end_chapter", endChapter);
		long k = SQ.insert(DAILY_TASK_TABLE, null, cv);
		Log.d("Database operations", "One day task inserted");				
	}
	
	public void DeletePlan(LocalDataManage ldm){
		SQLiteDatabase SQ = ldm.getWritableDatabase();
		SQ.delete(DAILY_TASK_TABLE, "*", null);
		SQ.delete(PLAN_TABLE, "*", null);
	}
}
