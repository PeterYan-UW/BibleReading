package com.afc.biblereading;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;

public class Tabs extends FragmentActivity{
    private FragmentTabHost mTabHost;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

    	super.onCreate(savedInstanceState);
    	
        setContentView(R.layout.activity_tabs);

        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);

        mTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);

        mTabHost.addTab(
                mTabHost.newTabSpec("User Info").setIndicator("User Info", null),
                First.class, null);

        mTabHost.addTab(
                mTabHost.newTabSpec("Fami Info").setIndicator("Fami Info", null),
                Second.class, null);

    }
}