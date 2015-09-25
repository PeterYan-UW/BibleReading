package com.afc.biblereading.group;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.afc.biblereading.R;
import com.afc.biblereading.helper.DataHolder;
import com.afc.biblereading.helper.DialogUtils;
import com.afc.biblereading.helper.util;
import com.afc.biblereading.user.BaseActivity;
import com.quickblox.core.QBEntityCallbackImpl;
import com.quickblox.customobjects.QBCustomObjects;
import com.quickblox.customobjects.model.QBCustomObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import static com.afc.biblereading.user.definitions.Consts.POSITION;

public class ShowGroupActivity extends BaseActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_group);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.show_group, menu);
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
	
	public void onClick(View view) {
        switch (view.getId()) {
        	case R.id.join_this_group:
        		joinGroup();
        		break;
        }
    }

	private void joinGroup() {
		int position;
	    Bundle extras = getIntent().getExtras();
	    if(extras == null) {
	    	position= -1;
	    } else {
	    	position= extras.getInt(POSITION);
	    }
	    QBCustomObject oldQBGroup = DataHolder.getDataHolder().getQBGroup(position);
	    ArrayList<Integer> members = DataHolder.getDataHolder().getGroup(position).getMembersId();
	    members.add(DataHolder.getDataHolder().getSignInUserId());
	    
	    QBCustomObject qbGroup = new QBCustomObject();
	    qbGroup.setClassName("group");
	    HashMap<String, Object> fields = new HashMap<String, Object>();
	    fields.put("members", members);
	    qbGroup.setFields(fields);
	    qbGroup.setCustomObjectId(oldQBGroup.getCustomObjectId());
	    QBCustomObjects.updateObject(qbGroup, new QBEntityCallbackImpl<QBCustomObject>() {
	        @Override
	        public void onSuccess(QBCustomObject newQBGroup, Bundle params) {
	        	Group group = util.QBGroup2Group(newQBGroup);
	        	DataHolder.getDataHolder().setSignInUserGroup(group);	
	        	DataHolder.getDataHolder().setSignInUserQbGroup(newQBGroup);	 
	        	backToUserGroupsActivity();
	        }
	     
	        @Override
	        public void onError(List<String> errors) {
				DialogUtils.showLong(context, errors.get(0));			     
	        }
	    });		
	}
	private void backToUserGroupsActivity() {
		finish();
	}	
}
