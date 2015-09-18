package com.afc.biblereading.group;

import com.afc.biblereading.R;
import com.afc.biblereading.ShowGroupActivity;
import com.afc.biblereading.adapter.GroupListAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import static com.afc.biblereading.user.definitions.Consts.POSITION;

public class GroupListActivity extends Activity implements AdapterView.OnItemClickListener {
	
    private GroupListAdapter groupsListAdapter;
    private ListView groupList;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group_list);

        initUI();
        initGroupList();
	}
	
	private void initUI() {
        groupList = (ListView) findViewById(R.id.groups_listview);
    }
    
    private void initGroupList() {
    	groupsListAdapter = new GroupListAdapter(this);
    	groupList.setAdapter(groupsListAdapter);
    	groupList.setOnItemClickListener(this);
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
}
