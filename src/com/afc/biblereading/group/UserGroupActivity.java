package com.afc.biblereading.group;

import static com.afc.biblereading.user.definitions.Consts.POSITION;

import java.util.ArrayList;
import java.util.List;

import com.afc.biblereading.R;
import com.afc.biblereading.R.id;
import com.afc.biblereading.R.layout;
import com.afc.biblereading.adapter.GroupListAdapter;
import com.afc.biblereading.helper.DataHolder;
import com.afc.biblereading.helper.DialogUtils;
import com.afc.biblereading.helper.util;
import com.afc.biblereading.user.BaseActivity;
import com.quickblox.core.QBEntityCallbackImpl;
import com.quickblox.core.request.QBRequestGetBuilder;
import com.quickblox.customobjects.QBCustomObjects;
import com.quickblox.customobjects.model.QBCustomObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class UserGroupActivity extends BaseActivity {
    private LinearLayout createGroupLayout;
    
    private LinearLayout userGroupLayout; 
    private TextView userGroupNameTextView;
    private TextView userGroupRateTextView;
    private ProgressBar userGroupProgressBar;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_group);
		initUI();
        initUserGroup();
	}
	private void initUI() {        
        createGroupLayout = (LinearLayout) findViewById(R.id.create_group_layout);
        
        userGroupLayout = (LinearLayout) findViewById(R.id.user_group_layout);
        userGroupNameTextView = (TextView) findViewById(R.id.user_group_name_textview);
        userGroupRateTextView = (TextView) findViewById(R.id.user_group_rate_textview);
        userGroupProgressBar = (ProgressBar) findViewById(R.id.user_group_finished_rate);            
    }

    private void initUserGroup() {
    	Group userGroup = DataHolder.getDataHolder().getSignInQbUserGroup();
        if (userGroup != null) {
        	createGroupLayout.setVisibility(View.GONE);
        	userGroupLayout.setVisibility(View.VISIBLE);
        	applyUserGroupInfo(userGroup.getName(), userGroup.getFinishRate());
        }		
	}
    
    private void applyUserGroupInfo(String groupName, int rate) {
    	userGroupNameTextView.setText(groupName);
    	userGroupRateTextView.setText(rate+" %");
    	userGroupProgressBar.setProgress(rate);
    }
    
    @Override
    public void onResume(){
		super.onResume(); 
		initUserGroup();  	
    }
	
	public void onClick(View view) {
        switch (view.getId()) {
        	case R.id.join_group_button:
        		getAllGroup();
        		break;
            case R.id.create_group_button:
            	createGroup();
                break;
        }
    }
    
	private void createGroup(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Create New Bible Reading Group");
		builder.setMessage("Group Name");
		final EditText inputField = new EditText(this);
		builder.setView(inputField);
		builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialogInterface, int i) {
				String group_name = inputField.getText().toString();
				Integer currentUserID = DataHolder.getDataHolder().getSignInUserId();
				ArrayList<Integer> members = new ArrayList<Integer>();
				members.add(currentUserID);
				
				QBCustomObject group = new QBCustomObject();
				group.putString("group_name", group_name);
				group.putInteger("num_member", 1);
				group.putArray("members", members);
				group.setClassName("group");
				QBCustomObjects.createObject(group, new QBEntityCallbackImpl<QBCustomObject>() {
		    	    @Override
		    	    public void onSuccess(QBCustomObject createdObject, Bundle bundle) {
		    	    	Group g = util.QBGroup2Group(createdObject);
		    	    	DataHolder.getDataHolder().setSignInQbUserGroup(g);
		    	    }
		    	 
		    	    @Override
		    	    public void onError(List<String> errors) {
						DialogUtils.showLong(context, errors.get(0));		    	 
		    	    }
		    	});
			}
		});
		builder.setNegativeButton("Cancel",null);
		builder.create().show();
	}
    private void getAllGroup() {
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
    			startGetAllGroupsActivity();				
			}
			
			@Override
			public void onError(List<String> errors) {
				DialogUtils.showLong(context, errors.get(0));				
			}
		});
    }
    
    private void startGetAllGroupsActivity() {
    	Intent intent = new Intent(this, GroupListActivity.class);
    	startActivity(intent);
    }				
    
}
