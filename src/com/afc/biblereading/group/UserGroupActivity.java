package com.afc.biblereading.group;

import static com.afc.biblereading.user.definitions.Consts.POSITION;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.afc.biblereading.MemberInfoActivity;
import com.afc.biblereading.R;
import com.afc.biblereading.adapter.UserListAdapter;
import com.afc.biblereading.helper.DataHolder;
import com.afc.biblereading.helper.DialogUtils;
import com.afc.biblereading.helper.util;
import com.afc.biblereading.user.BaseActivity;
import com.afc.biblereading.user.CreateSessionActivity;
import com.quickblox.core.QBEntityCallbackImpl;
import com.quickblox.core.request.QBPagedRequestBuilder;
import com.quickblox.customobjects.QBCustomObjects;
import com.quickblox.customobjects.model.QBCustomObject;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class UserGroupActivity extends BaseActivity {
    private LinearLayout createGroupLayout;
    
    private LinearLayout userGroupLayout; 
    private TextView userGroupNameTextView;
    private TextView userGroupNumTextView;
    private ListView groupMemberListView;
    private ProgressBar progressBar;
    
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
        userGroupNumTextView = (TextView) findViewById(R.id.user_group_number_textview);
        groupMemberListView = (ListView) findViewById(R.id.member_listview);
        progressBar = (ProgressBar) findViewById(R.id.user_group_progress);
    }

    private void initUserGroup() {
    	Group userGroup = DataHolder.getDataHolder().getSignInUserGroup();
        if (userGroup != null) {
        	createGroupLayout.setVisibility(View.GONE);
        	userGroupLayout.setVisibility(View.VISIBLE);
        	applyUserGroupInfo(userGroup.getName(), userGroup.getGroupSize());
        	groupMemberListView.setOnItemClickListener(
        			new OnItemClickListener(){
        				@Override
        				public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        					Log.v("user group activity", "click Item");
        					startShowUserActivity(position);
        				}
        			});
        }		
        else{
        	createGroupLayout.setVisibility(View.VISIBLE);
        	userGroupLayout.setVisibility(View.GONE);        	
        }
	}
    
    private void applyUserGroupInfo(String groupName, int memberNum) {
    	QBPagedRequestBuilder pagedRequestBuilder = new QBPagedRequestBuilder();
    	userGroupNameTextView.setText(groupName);
    	userGroupNumTextView.setText(String.valueOf(memberNum));
    	ArrayList<Integer> usersIds = DataHolder.getDataHolder().getSignInUserGroup().getMembersId();
    	 
    	QBUsers.getUsersByIDs(usersIds, pagedRequestBuilder, new QBEntityCallbackImpl<ArrayList<QBUser>>() {
    		@Override
            public void onSuccess(final ArrayList<QBUser> qbUsers, Bundle bundle) {
    			DataHolder.getDataHolder().getSignInUserGroup().setMembersQB(qbUsers);
    	    	UserListAdapter memberAdapter = new UserListAdapter(context, R.layout.list_item_user, qbUsers);
    	    	groupMemberListView.setAdapter(memberAdapter); 
    	    }
    	 
    	    @Override
    	    public void onError(List<String> errors) {
    	 
    	    }
    	});
    }
    
    @Override
    public void onResume(){
		super.onResume(); 
		if (DataHolder.getDataHolder().getSignInQbUser()== null){
    		Intent user = new Intent(this, CreateSessionActivity.class);
    		startActivity(user);  			
		}
		initUserGroup();  	
    }
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user_group, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
	        case R.id.leave_group:
	        	leaveGroup();
	        	return true;
			default:
			    return super.onOptionsItemSelected(item);
		}    
    }
	public void onClick(View view) {
        switch (view.getId()) {
        	case R.id.join_group_button:
        		progressBar.setVisibility(View.VISIBLE);
        		GroupHelper.getAllGroup(context, progressBar);
        		break;
            case R.id.create_group_button:
            	createGroup();
                break;
        }
    }
	
	private void leaveGroup() {
		QBCustomObject oldQBGroup = DataHolder.getDataHolder().getSignInUserQbGroup();
	    ArrayList memberList = DataHolder.getDataHolder().getSignInUserGroup().getMembersId();
	    Integer currentUserId = DataHolder.getDataHolder().getSignInUserId();
	    Log.v("current user id", String.valueOf(currentUserId));
	    Log.v("before leave group", memberList.toString());
	    memberList.remove(String.valueOf(currentUserId));
	    Log.v("leave group", memberList.toString());
	    if (memberList.size() == 0){
	    	QBCustomObjects.deleteObject("group", oldQBGroup.getCustomObjectId(), new QBEntityCallbackImpl<Object>() {
	    		@Override
		        public void onSuccess() {
		        	DataHolder.getDataHolder().setSignInUserGroup(null);	
		        	DataHolder.getDataHolder().setSignInUserQbGroup(null);
		        	initUserGroup();
		        }
		     
		        @Override
		        public void onError(List<String> errors) {
					DialogUtils.showLong(context, errors.get(0));			     
		        }
	    	});
	    }
	    else{
	    	QBCustomObject qbGroup = new QBCustomObject();
		    qbGroup.setClassName("group");
		    HashMap<String, Object> fields = new HashMap<String, Object>();
		    fields.put("members", memberList);
		    qbGroup.setFields(fields);
		    qbGroup.setCustomObjectId(oldQBGroup.getCustomObjectId());
		    QBCustomObjects.updateObject(qbGroup, new QBEntityCallbackImpl<QBCustomObject>() {
		        @Override
		        public void onSuccess(QBCustomObject newQBGroup, Bundle params) {
		        	DataHolder.getDataHolder().setSignInUserGroup(null);	
		        	DataHolder.getDataHolder().setSignInUserQbGroup(null);
		        	initUserGroup();
		        }
		     
		        @Override
		        public void onError(List<String> errors) {
					DialogUtils.showLong(context, errors.get(0));			     
		        }
		    });
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
        		progressBar.setVisibility(View.VISIBLE);
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
		    	    	DataHolder.getDataHolder().setSignInUserQbGroup(createdObject);
		    	    	Group g = util.QBGroup2Group(createdObject);
		    	    	DataHolder.getDataHolder().setSignInUserGroup(g);
		    	    	initUserGroup();
		        		progressBar.setVisibility(View.GONE);
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
	
	private Boolean exit = false;
	@Override
    public void onBackPressed() {
        if (exit) {
            DataHolder.getDataHolder().setSignInQbUser(null);
            DataHolder.getDataHolder().setSignInUserGroup(null);
            DataHolder.getDataHolder().setSignInUserQbGroup(null);
        	Intent intent = new Intent(Intent.ACTION_MAIN);
        	intent.addCategory(Intent.CATEGORY_HOME);
        	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        	startActivity(intent);
        } else {
            Toast.makeText(this, "Press Back again to Exit.",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);
        }
    }
	
	private void startShowUserActivity(int position) {
        Intent intent = new Intent(this, MemberInfoActivity.class);
        intent.putExtra(POSITION, position);
        startActivity(intent);
	}
}
