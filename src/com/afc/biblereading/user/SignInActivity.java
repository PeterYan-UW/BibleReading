package com.afc.biblereading.user;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.afc.biblereading.R;
import com.afc.biblereading.group.Group;
import com.afc.biblereading.helper.DataHolder;
import com.afc.biblereading.helper.DialogUtils;
import com.afc.biblereading.helper.util;
import com.quickblox.core.QBEntityCallbackImpl;
import com.quickblox.core.request.QBRequestGetBuilder;
import com.quickblox.customobjects.QBCustomObjects;
import com.quickblox.customobjects.model.QBCustomObject;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import java.util.ArrayList;
import java.util.List;

public class SignInActivity extends BaseActivity {

    private EditText loginEditText;
    private EditText passwordEditText;

    @Override
    public void onCreate(Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);
        setContentView(R.layout.activity_sign_in);
        initUI();
    }

    private void initUI() {
        loginEditText = (EditText) findViewById(R.id.login_edittext);
        passwordEditText = (EditText) findViewById(R.id.password_edittext);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sign_in_button:
                progressDialog.show();

                // Sign in application with user
                //
                QBUser qbUser = new QBUser(loginEditText.getText().toString(), passwordEditText.getText().toString());
                QBUsers.signIn(qbUser, new QBEntityCallbackImpl<QBUser>() {
                    @Override
                    public void onSuccess(QBUser qbUser, Bundle bundle) {
                        progressDialog.hide();

                        setResult(RESULT_OK);

                        DataHolder.getDataHolder().setSignInQbUser(qbUser);
                        checkUserGroup(qbUser);
                        // password does not come, so if you want use it somewhere else, try something like this:
                        DataHolder.getDataHolder().setSignInUserPassword(passwordEditText.getText().toString());
                        DialogUtils.showLong(context, getResources().getString(R.string.user_successfully_sign_in));

                        finish();
                    }

					@Override
                    public void onError(List<String> errors) {
                        progressDialog.hide();
                        DialogUtils.showLong(context, errors.get(0));
                    }
                });
                progressDialog.hide();
                break;
        }
    }


    private void checkUserGroup(QBUser qbUser) {
    	QBRequestGetBuilder userGroupRequestBuilder = new QBRequestGetBuilder();
    	userGroupRequestBuilder.ctn("members", qbUser.getId());
    	QBCustomObjects.getObjects("group", userGroupRequestBuilder, new QBEntityCallbackImpl<ArrayList<QBCustomObject>>(){
    		@Override
			public void onSuccess(ArrayList<QBCustomObject> groups, Bundle bundle) {
    			// Logically each user can only join one group
    			// So the condition should be (groups.size() == 1)
    			if (groups.size() >= 1){
    				Group userGroup = util.QBGroup2Group(groups.get(0));
    				DataHolder.getDataHolder().setSignInQbUserGroup(userGroup);
    			}	
			}
			
			@Override
			public void onError(List<String> errors) {
				DialogUtils.showLong(context, errors.get(0));				
			}
    	});
		
		
	}
}