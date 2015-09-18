package com.afc.biblereading.group;

import java.util.ArrayList;
import java.util.List;

import com.afc.biblereading.R;
import com.afc.biblereading.ShowGroupActivity;
import com.afc.biblereading.adapter.GroupListAdapter;
import com.afc.biblereading.helper.DataHolder;
import com.afc.biblereading.helper.util;
import com.quickblox.core.QBEntityCallbackImpl;
import com.quickblox.customobjects.QBCustomObjects;
import com.quickblox.customobjects.model.QBCustomObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import static com.afc.biblereading.user.definitions.Consts.POSITION;

public class GroupListActivity extends Activity  implements AdapterView.OnItemClickListener {
	
    private GroupListAdapter groupsListAdapter;
    private ListView groupList;
    private Button joinGroupButton;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group_list);

        initUI();
        initGroupList();
	}
    private void initUI() {
        groupList = (ListView) findViewById(R.id.groups_listview);
        joinGroupButton = (Button) findViewById(R.id.join_group_button);
    }
    
    private void initGroupList() {
    	groupsListAdapter = new GroupListAdapter(this);
    	groupList.setAdapter(groupsListAdapter);
    	groupList.setOnItemClickListener(this);
    }
    
    @Override
    public void onResume() {
        super.onResume();
        if (DataHolder.getDataHolder().getSignInQbUserGroup() != null) {
            joinGroupButton.setVisibility(View.GONE);
        }
        groupsListAdapter.notifyDataSetChanged();
    }
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.group_list, menu);
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
	@Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        startShowGroupActivity(position);
    }

    private void startShowGroupActivity(int position) {
        Intent intent = new Intent(this, ShowGroupActivity.class);
        intent.putExtra(POSITION, position);
        startActivity(intent);
    }
    
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.join_group_button:
            	break;
            case R.id.create_group_button:
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
        				group.putInteger("owner", currentUserID);
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
        		    	 
        		    	    }
        		    	});
        			}
        		});

        		builder.setNegativeButton("Cancel",null);

        		builder.create().show();
                break;
        }
    }
}
