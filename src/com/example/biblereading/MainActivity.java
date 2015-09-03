package com.example.biblereading;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


import android.graphics.Typeface;

import android.widget.TextView;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Typeface face0 = Typeface.createFromAsset(getAssets(),"fonts/fonts1.TTF");
        TextView PickDateTitle = (TextView) findViewById(R.id.PickDateTitle);
        PickDateTitle.setTypeface(face0);
        
        Typeface face1 = Typeface.createFromAsset(getAssets(),"fonts/fonts2.TTF");
        TextView StartDate = (TextView) findViewById(R.id.StartDate);
        StartDate.setTypeface(face1);
        TextView EndDate = (TextView) findViewById(R.id.EndDate);
        EndDate.setTypeface(face1);
        
        
        
    }


   
}
