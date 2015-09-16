package com.afc.biblereading.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.afc.biblereading.R;
import com.afc.biblereading.adapter.UserListAdapter;
import com.afc.biblereading.helper.DataHolder;
import com.afc.biblereading.helper.DialogUtils;
import com.quickblox.core.QBEntityCallbackImpl;
import com.quickblox.users.QBUsers;

import java.util.List;

import static com.afc.biblereading.user.definitions.Consts.POSITION;

public class UsersListActivity extends BaseActivity implements AdapterView.OnItemClickListener {

//    private UserListAdapter usersListAdapter;
//    private ListView usersList;
    private ScrollView userInfo;
    private TextView askLogin;
    private TextView loginTextView;
    private TextView emailTextView;
    private TextView fullNameTextView;
    private Button logOutButton;
    private Button signInButton;
    private Button selfEditButton;
    private Button singUpButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        initUI();
//        initUsersList();
    }

    private void initUI() {
    	userInfo = (ScrollView) findViewById(R.id.user_info_scrollview);
    	askLogin = (TextView) findViewById(R.id.no_user);
    	loginTextView = (TextView) findViewById(R.id.login_textview);
        emailTextView = (TextView) findViewById(R.id.email_textview);
        fullNameTextView = (TextView) findViewById(R.id.full_name_textview);
        logOutButton = (Button) findViewById(R.id.logout_button);
        signInButton = (Button) findViewById(R.id.sign_in_button);
        selfEditButton = (Button) findViewById(R.id.self_edit_button);
        singUpButton = (Button) findViewById(R.id.sign_up_button);
//        usersList = (ListView) findViewById(R.id.users_listview);
    }

    private void fillAllFields() {
        fillField(loginTextView, DataHolder.getDataHolder().getSignInUserLogin());
        fillField(emailTextView, DataHolder.getDataHolder().getSignInUserEmail());
        fillField(fullNameTextView, DataHolder.getDataHolder().getSignInUserFullName());
    }
//    private void initUsersList() {
//        usersListAdapter = new UserListAdapter(this);
//        usersList.setAdapter(usersListAdapter);
//        usersList.setOnItemClickListener(this);
//    }

    @Override
    public void onResume() {
        super.onResume();
        if (DataHolder.getDataHolder().getSignInQbUser() != null) {
            signInButton.setVisibility(View.GONE);
            singUpButton.setVisibility(View.GONE);
            askLogin.setVisibility(View.GONE);
            userInfo.setVisibility(View.VISIBLE);
            logOutButton.setVisibility(View.VISIBLE);
            selfEditButton.setVisibility(View.VISIBLE);
        }
//        usersListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // destroy session after app close
        DataHolder.getDataHolder().setSignInQbUser(null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            signInButton.setVisibility(View.GONE);
            askLogin.setVisibility(View.GONE);
            logOutButton.setVisibility(View.VISIBLE);
            fillAllFields();
        }
    }

    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.sign_in_button:
                intent = new Intent(this, SignInActivity.class);
                startActivityForResult(intent, 0);
                break;
            case R.id.sign_up_button:
                intent = new Intent(this, SignUpUserActivity.class);
                startActivity(intent);
                break;
            case R.id.logout_button:
                progressDialog.show();

                // Logout
                //
                QBUsers.signOut(new QBEntityCallbackImpl() {
                    @Override
                    public void onSuccess() {
                        progressDialog.hide();

                        DialogUtils.showLong(context, getResources().getString(R.string.user_log_out_msg));
                        updateDataAfterLogOut();
                    }

                    @Override
                    public void onError(List list) {
                        progressDialog.hide();

                        DialogUtils.showLong(context, list.get(0).toString());
                    }
                });

                break;
            case R.id.self_edit_button:
                intent = new Intent(this, UpdateUserActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void updateDataAfterLogOut() {
        DataHolder.getDataHolder().setSignInQbUser(null);
        signInButton.setVisibility(View.VISIBLE);
        askLogin.setVisibility(View.VISIBLE);
        userInfo.setVisibility(View.GONE);
        logOutButton.setVisibility(View.GONE);
        selfEditButton.setVisibility(View.GONE);
        singUpButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        startShowUserActivity(position);
    }

    private void startShowUserActivity(int position) {
        Intent intent = new Intent(this, ShowUserActivity.class);
        intent.putExtra(POSITION, position);
        startActivity(intent);
    }
}