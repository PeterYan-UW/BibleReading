package com.afc.biblereading.group;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.afc.biblereading.helper.DataHolder;
import com.afc.biblereading.helper.DialogUtils;
import com.afc.biblereading.helper.util;
import com.quickblox.core.QBEntityCallbackImpl;
import com.quickblox.core.request.QBRequestGetBuilder;
import com.quickblox.customobjects.QBCustomObjects;
import com.quickblox.customobjects.model.QBCustomObject;

public class GroupHelper {
	
	public static void leaveGroup(final Context context) {
		QBCustomObject oldQBGroup = DataHolder.getDataHolder().getSignInUserQbGroup();
	    ArrayList memberList = DataHolder.getDataHolder().getSignInUserGroup().getMembersId();
	    Integer currentUserId = DataHolder.getDataHolder().getSignInUserId();
	    Log.v("current user id", String.valueOf(currentUserId));
	    Log.v("before leave group", memberList.toString());
	    memberList.remove(String.valueOf(currentUserId));
	    Log.v("leave group", memberList.toString());
	    
	    QBCustomObject qbGroup = new QBCustomObject();
	    qbGroup.setClassName("group");
	    HashMap<String, Object> fields = new HashMap<String, Object>();
	    fields.put("members", memberList);
	    qbGroup.setFields(fields);
	    qbGroup.setCustomObjectId(oldQBGroup.getCustomObjectId());
	    QBCustomObjects.updateObject(qbGroup, new QBEntityCallbackImpl<QBCustomObject>() {
	        @Override
	        public void onSuccess(QBCustomObject newQBGroup, Bundle params) {
	        	Group group = util.QBGroup2Group(newQBGroup);
	        	DataHolder.getDataHolder().setSignInUserGroup(null);	
	        	DataHolder.getDataHolder().setSignInUserQbGroup(null);
	        }
	     
	        @Override
	        public void onError(List<String> errors) {
				DialogUtils.showLong(context, errors.get(0));			     
	        }
	    });
		
	}
	
    public static void getAllGroup(final Context context, final ProgressBar progressBar) {
    	QBRequestGetBuilder groupRequestBuilder = new QBRequestGetBuilder();
    	QBCustomObjects.getObjects("group", groupRequestBuilder, new QBEntityCallbackImpl<ArrayList<QBCustomObject>>() {
			
			@Override
			public void onSuccess(ArrayList<QBCustomObject> groups, Bundle bundle) {
				List<Group> groupList = new ArrayList<Group>();
				for (int i=0; i<groups.size(); i++){
					Group g = util.QBGroup2Group(groups.get(i));
					groupList.add(g);
				}
    			DataHolder.getDataHolder().setGroupList(groupList);
    			DataHolder.getDataHolder().setQBGroupList(groups);
        		Intent intent = new Intent(context, GroupListActivity.class);
        		context.startActivity(intent);
        		progressBar.setVisibility(View.GONE);
			}
			
			@Override
			public void onError(List<String> errors) {
				DialogUtils.showLong(context, errors.get(0));				
			}
		});
    }
}
