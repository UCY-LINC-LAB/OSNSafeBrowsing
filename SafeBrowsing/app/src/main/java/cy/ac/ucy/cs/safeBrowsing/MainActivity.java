package cy.ac.ucy.cs.safeBrowsing;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();
    LoginButton login;
    CallbackManager callbackManager;
    LoginResult LR;
    AccessToken AT;

    int backButtonCount= 0;

    // ---------------
    LinearLayout list;
    // ---------------

    //Creating and initializing the feature(Code from Facebook install guide)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext()); //Calls for the facebook feature
        callbackManager = CallbackManager.Factory.create();
        //setContentView(R.layout.boot_screen_form);

        setContentView(R.layout.log_in_form);
        login = (LoginButton) findViewById(R.id.login_button);
        //login.setReadPermissions("user_friends", "email");

        // Callback registration
        login.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                LR= loginResult;
                AT= LR.getAccessToken();
                MyFbLib.setAt(AT);

                enterWall ();
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

        // Catch Log out Event
        AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {

                if (currentAccessToken == null){
                    AT= null;
                    setContentView(R.layout.log_in_form);
                }
            }
        };


        setContentView(R.layout.boot_screen_form);

        if (isLoggedIn()== true) {
            enterWall ();
        } else {
            setContentView(R.layout.log_in_form);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Back button listener.
     * Will close the application if the back button pressed twice.
     */
    @Override
    public void onBackPressed() {


        if(backButtonCount >= 1) {/*
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);*/
            //android.os.Process.killProcess(android.os.Process.myPid());
            finish ();
        }
        else {
            Toast.makeText(this, "Press the back button once again to close the application.", Toast.LENGTH_SHORT).show();
            backButtonCount++;
        }
    }

    public boolean isLoggedIn() {
        final AccessToken[] accessToken = new AccessToken[1];
        final MyProfile[] profile = new MyProfile[1];
        Thread t= new Thread(){
            @Override
            public void run() {
                accessToken[0] = AccessToken.getCurrentAccessToken();
                MyFbLib.setAt(accessToken[0]);
                profile[0]= MyFbLib.getMyProfile();
            }
        };
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
      //  AccessToken accessToken = AccessToken.getCurrentAccessToken();
        /*try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
        }*/

        return profile[0] != null;
    }

    public void enterWall () {
        setContentView(R.layout.wall_form);

        ArrayList<MyPost> posts=MyFbLib.getTenNextPosts();
        list = (LinearLayout) findViewById(R.id.list);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        Button button2 = new Button(MainActivity.this);
        TextView txt2 = new TextView(MainActivity.this);
        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
        list.addView(button2, params2);
        list.addView(txt2, params2);
        txt2.setText("ALALALAAL\n\n ALALAA");
    }

}