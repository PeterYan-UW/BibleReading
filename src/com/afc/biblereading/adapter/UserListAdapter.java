package com.afc.biblereading.adapter;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.afc.biblereading.R;
import com.afc.biblereading.group.Member;
import com.afc.biblereading.helper.DataHolder;
import com.quickblox.core.QBEntityCallbackImpl;
import com.quickblox.core.request.QBRequestGetBuilder;
import com.quickblox.customobjects.QBCustomObjects;
import com.quickblox.users.model.QBUser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.xmlpull.v1.XmlPullParser;

public class UserListAdapter extends ArrayAdapter<QBUser> {
	private ArrayList<QBUser> memberList;

    private int resource;
    public UserListAdapter(Context context, int resource, 
			ArrayList<QBUser> memberList) {
    	super(context, resource, memberList);
    	this.memberList = new ArrayList<QBUser>();
    	this.memberList = memberList;
    	this.resource = resource;
    }
	
	private class ViewHolder {
		TextView userName;
		ImageView statusIcon;
		TextView statusTextview;
	}

    private Boolean wait = true;
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
			LayoutInflater vi = (LayoutInflater)getContext().getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
			convertView = vi.inflate(resource, null);
            viewHolder = new ViewHolder();
            viewHolder.userName = (TextView) convertView.findViewById(
            		R.id.user_name_textview);
            viewHolder.statusIcon = (ImageView) convertView.findViewById(
            		R.id.today_task_imageview);
            viewHolder.statusTextview = (TextView) convertView.findViewById(
            		R.id.status_textview);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        QBUser member = this.memberList.get(position);
        String name = member.getFullName();
        final Integer id = member.getId();
        viewHolder.userName.setText(name);

        checkStatus(viewHolder, id);
        return convertView;
    }

    private void checkStatus(final ViewHolder viewHolder, Integer id) {
    	Date today = new Date();    	
    	today.setHours(0);    	
    	today.setMinutes(0);    	
    	today.setSeconds(0);
    	QBRequestGetBuilder logRequestBuilder = new QBRequestGetBuilder();
    	logRequestBuilder.eq("user_id", id);
    	logRequestBuilder.eq("done", true);
    	logRequestBuilder.gt("created_at", today.getTime()/1000);
		QBCustomObjects.countObjects("DailyRecords", logRequestBuilder, new QBEntityCallbackImpl<Integer>(){
			 @Override
             public void onSuccess(Integer count, Bundle bundle) {
				 if (count >= 1){
					 viewHolder.statusIcon.setImageDrawable(getContext().getResources().getDrawable(R.drawable.done));
					 viewHolder.statusTextview.setText("已完成今日任务");
				 }
				 else{
					 viewHolder.statusIcon.setImageDrawable(getContext().getResources().getDrawable(R.drawable.fail));
					 viewHolder.statusTextview.setText("还未完成今日任务");
				 }
             }

             @Override
             public void onError(List<String> errors) {
             }
		});		
	}
}