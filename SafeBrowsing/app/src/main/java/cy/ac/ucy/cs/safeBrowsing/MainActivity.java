package cy.ac.ucy.cs.safeBrowsing;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();
    LoginButton login;
    CallbackManager callbackManager;
    LoginResult LR;
    AccessToken AT;

    // ---------------
    LinearLayout list;
    // ---------------

    //Creating and initializing the feature(Code from Facebook install guide)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext()); //Calls for the facebook feature
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_main);
        login = (LoginButton) findViewById(R.id.login_button);
        //login.setReadPermissions("user_friends", "email");

        // Callback registration
        login.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                LR= loginResult;
                AT= LR.getAccessToken();

                // ---------------
                setContentView(R.layout.sample_wall);
                MyFbLib.setAt(AT);
                ArrayList<MyPost> posts=MyFbLib.getTenNextPosts();
                Log.d(TAG,"Second Call-------------------------");
                list = (LinearLayout) findViewById(R.id.list);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                Button button2 = new Button(MainActivity.this);
                TextView txt2 = new TextView(MainActivity.this);
                LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
                list.addView(button2, params2);
                list.addView(txt2, params2);
                txt2.setText("ALALALAAL\n\n ALALAA");

                // ---------------
        }

            @Override
            public void onCancel() {
                //info.setText("Login attempt canceled.");
            }

            @Override
            public void onError(FacebookException exception) {
                //info.setText("Login attempt failed.");
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

}