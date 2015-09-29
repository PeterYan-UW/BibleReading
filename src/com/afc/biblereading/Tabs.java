package com.afc.biblereading;



import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.TabHost;
import android.app.Activity;



public class Tabs extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {

//    	super.onCreate(savedInstanceState);
//    	
//        setContentView(R.layout.activity_tabs);
//
//        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
//
//        mTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);
//
//        mTabHost.addTab(
//                mTabHost.newTabSpec("User Info").setIndicator("User Info", null),
//                First.class, null);
//
//        mTabHost.addTab(
//                mTabHost.newTabSpec("Fami Info").setIndicator("Fami Info", null),
//                Second.class, null);
    	super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_tabs);
        setTitle("珠海新闻网Android客户端");
        
        TabHost tabs = (TabHost) findViewById(R.id.tabhost);
        tabs.setup();
        
        
        
        LayoutInflater.from(this).inflate(R.layout.acitvity_first, tabs.getTabContentView(),true);   //将布局文件与TabHost关联在一起
     	LayoutInflater.from(this).inflate(R.layout.acitvity_second, tabs.getTabContentView(),true);
      
      
     	tabs.addTab(tabs.newTabSpec("TAB1").setIndicator("线性布局").setContent(R.id.layout01));  //setContent()指定每个Tab包含的View
     	tabs.addTab(tabs.newTabSpec("TAB2").setIndicator("绝对布局").setContent(R.id.layout02));
        
        
        tabs.setCurrentTab(0);

    }
}