package com.afc.biblereading;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.joda.time.DateTime;
import org.joda.time.Days;

import android.content.Context;
import android.util.Log;

public class CreateReadingPlan {

	private final static int TOTAL_BOOKS = 66;
	private final static int TOTAL_CHAPTERS = 1189;

	private final static BibleIndex Bibleindex = new BibleIndex();
	public static void CreatePlan(String planName, DateTime startDate,
			DateTime endDate, MainActivity context) {
		//TODO: need to gen uniqu plan id
		int planId = 0;
		LocalDataManage DOP = new LocalDataManage(context);
		DOP.AddPlan(DOP, 0, planName, startDate, endDate);
        int totalReadingDays = 
        		Days.daysBetween(startDate, endDate).getDays()+1;
        
        Log.v(Integer.toString(totalReadingDays),"timetime");
        Log.v(Integer.toString(TOTAL_CHAPTERS/totalReadingDays),"Chapter");
        Log.v(Integer.toString(TOTAL_CHAPTERS%totalReadingDays),"remainder");
        
		dailyTask(totalReadingDays, planId, DOP);
		
	}
	
	private static void dailyTask(int totalReadingDays, int planId, LocalDataManage DOP){
		
        int dailyChapters = TOTAL_CHAPTERS/totalReadingDays;
        int backUpdailyChapters = dailyChapters;
        int remainderChapters = TOTAL_CHAPTERS%totalReadingDays;
        
        int StartBook = 0;
        int StartChapter = 1;
        int EndBook = 0;
        int EndChapter = 1;
        
        for(int CurrentDay = 1; CurrentDay <= totalReadingDays; CurrentDay++) {
        	
        	if (CurrentDay <= remainderChapters) {
        		dailyChapters = backUpdailyChapters;
        	}
        	else {
        		dailyChapters = backUpdailyChapters - 1; 
        	}
    		for (int CurrentBook=StartBook; CurrentBook < TOTAL_BOOKS; CurrentBook++) {
    			int CurrentBookChaptersNum = Bibleindex.BibleChapterNum[StartBook];
    			if (CurrentBookChaptersNum-StartChapter >= dailyChapters) {
    				EndBook = StartBook;
	        		EndChapter = StartChapter+dailyChapters;
    				DOP.AddTask(DOP, planId, CurrentDay, CurrentBook, StartChapter, EndChapter);
    				StartChapter = EndChapter+1;
    				break; 
    			}
    			else if (CurrentBookChaptersNum < StartChapter) {
    				StartBook = CurrentBook+1;
    				StartChapter = 1;
    			}
    			else {
    				EndBook = StartBook;
    				EndChapter = Bibleindex.BibleChapterNum[CurrentBook];
    				DOP.AddTask(DOP, planId, CurrentDay, CurrentBook, StartChapter, EndChapter);
    				dailyChapters = dailyChapters - (EndChapter-StartChapter) - 1;
    				StartBook++;
    				StartChapter = 1;
    				
    			}
    		}
        }
	}
}
