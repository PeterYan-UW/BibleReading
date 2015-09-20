package com.afc.biblereading.user;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.afc.biblereading.R;
import com.afc.biblereading.helper.DialogUtils;
import com.quickblox.core.QBEntityCallbackImpl;
import com.quickblox.core.QBSettings;
import com.quickblox.auth.QBAuth;
import com.quickblox.auth.model.QBSession;
import java.util.List;









import static com.afc.biblereading.user.definitions.Consts.APP_ID;
import static com.afc.biblereading.user.definitions.Consts.AUTH_KEY;
import static com.afc.biblereading.user.definitions.Consts.AUTH_SECRET;

public class CreateSessionActivity extends Activity{

    private Context context;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.progress_bar);
        context = this;

        initUI();

        // Initialize QuickBlox application with credentials.
        //
        QBSettings.getInstance().fastConfigInit(APP_ID, AUTH_KEY, AUTH_SECRET);

        // Create QuickBlox session
        //
        QBAuth.createSession(new QBEntityCallbackImpl<QBSession>() {
            @Override
            public void onSuccess(QBSession qbSession, Bundle bundle) {
            	startLogin();
            }

            @Override
            public void onError(List<String> errors) {
                // print errors that came from server
                DialogUtils.showLong(context, errors.get(0));
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void initUI() {
    	progressBar = (ProgressBar) findViewById(R.id.login_progress);
        progressBar.setVisibility(View.VISIBLE);
    }
    
    private void startLogin(){
    	Intent intent = new Intent(this, UserActivity.class);
    	startActivity(intent);
    	finish();    	
    }
}