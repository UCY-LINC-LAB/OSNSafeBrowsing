package cy.ac.ucy.cs.safeBrowsing;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;

import android.support.constraint.ConstraintLayout;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();
    LoginButton login;
    CallbackManager callbackManager;
    LoginResult LR;
    AccessToken AT;

    int backButtonCount = 0;

    static boolean isWallOpened = false;
    static ArrayList<MyPost> allPosts = new ArrayList();

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

        openLoginForm();

        // Catch Log out Event
        AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {

                if (currentAccessToken == null) {
                    AT = null;
                    isWallOpened = false;
                    allPosts = new ArrayList();
                    openLoginForm();
                }
            }
        };

        setContentView(R.layout.boot_screen_form);

        if (isLoggedIn() == true) {
            openWallForm();
        } else {
            openLoginForm();
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


        if (backButtonCount >= 1) {/*
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);*/
            //android.os.Process.killProcess(android.os.Process.myPid());
            finish();
        } else {
            Toast.makeText(this, "Press the back button once again to close the application.", Toast.LENGTH_SHORT).show();
            backButtonCount++;
        }
    }

    public boolean isLoggedIn() {
        final AccessToken[] accessToken = new AccessToken[1];
        final MyProfile[] profile = new MyProfile[1];
        Thread t = new Thread() {
            @Override
            public void run() {
                accessToken[0] = AccessToken.getCurrentAccessToken();
                MyFbLib.setAt(accessToken[0]);
                profile[0] = MyFbLib.getMyProfile();
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

    public void openWallForm() {
        setContentView(R.layout.wall_form);

        ImageView imgSettings = (ImageView) findViewById(R.id.imgSettings);
        imgSettings.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                openSettingsForm();
            }
        });

        Button btnLoadMore = (Button) findViewById(R.id.btnLoadMore);
        btnLoadMore.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                ArrayList<MyPost> posts = MyFbLib.getTenNextPosts();
                allPosts.addAll(posts);
                list = (LinearLayout) findViewById(R.id.list);

                for (int i = 0; i < posts.size(); i++) {
                    addNewPostToView(posts.get(i));
                }
            }
        });

        /*
        btnvideo.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {

                startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("http://www.youtube.com/watch?v=Hxy8BZGQ5Jo")));
                Log.i("Video", "Video Playing....");

            }
        });*/

        ImageView imgProfPic = (ImageView) findViewById(R.id.imgProfPic);
        MyProfile myProf = MyFbLib.getMyProfile();
        if (myProf.getProfilePicLink() != null) {
            Picasso.with(MainActivity.this).load(myProf.getProfilePicLink()).into(imgProfPic);
        }
        imgProfPic.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                openProfileForm();
            }
        });

        TextView txtName = (TextView) findViewById(R.id.txtName);
        txtName.setText(Html.fromHtml("<b><u>" + MyFbLib.getMyProfile().getName() + "</u></b>"));

        txtName.setTextColor(Color.BLACK);
        txtName.setTextSize(18);
        txtName.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                openProfileForm();
            }
        });

        if (isWallOpened == false) {
            isWallOpened = true;
            ArrayList<MyPost> posts = MyFbLib.getTenNextPosts();
            allPosts.addAll(posts);
            list = (LinearLayout) findViewById(R.id.list);

            for (int i = 0; i < posts.size(); i++) {
                addNewPostToView(posts.get(i));
            }
        } else {
            list = (LinearLayout) findViewById(R.id.list);

            for (int i = 0; i < allPosts.size(); i++) {
                addNewPostToView(allPosts.get(i));
            }
        }


                /*
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        Button button2 = new Button(MainActivity.this);
        TextView txt2 = new TextView(MainActivity.this);
        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
        list.addView(button2, params2);
        list.addView(txt2, params2);
        txt2.setText("Hello World");
        */


    }

    public void addNewPostToView(MyPost post) {
        list = (LinearLayout) findViewById(R.id.list);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams params2_1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        ConstraintLayout.LayoutParams params4 = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        ConstraintLayout.LayoutParams params5 = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.HORIZONTAL, ConstraintLayout.LayoutParams.MATCH_CONSTRAINT_WRAP);

        GridLayout gridAuthor = new GridLayout(MainActivity.this);
        list.addView(gridAuthor, params3);

        ImageView imgAuthor = new ImageView(MainActivity.this);
        Picasso.with(MainActivity.this).load(post.getAuthorImg()).into(imgAuthor);
        params2_1.width = 100;
        params2_1.height = 100;
        gridAuthor.addView(imgAuthor, params2_1);

        /*ViewGroup.LayoutParams paramsInstance = imgAuthor.getLayoutParams();
        params2.width= 550;
        params2.height= 500;
        imgAuthor.setLayoutParams(paramsInstance);*/

        TextView txtAuthor = new TextView(MainActivity.this);
        txtAuthor.setText("   " + post.getAuthorName());
        txtAuthor.setText(Html.fromHtml("<a>&nbsp;&nbsp;&nbsp;</a><u>" + post.getAuthorName() + "</u>"));
        txtAuthor.setTextColor(Color.BLACK);
        txtAuthor.setTextSize(16);
        gridAuthor.addView(txtAuthor, params2);
        //txtAuthor.setLayoutParams(new LinearLayout.LayoutParams(230, 40));


        TextView txtMessage = new TextView(MainActivity.this);
        txtMessage.setText(post.getMessage());
        list.addView(txtMessage, params2);

        if (post instanceof MyPhotoPost) {
            ImageView imgPhotoPost = new ImageView(MainActivity.this);
            Picasso.with(MainActivity.this).load(((MyPhotoPost) post).getPictureLink()).into(imgPhotoPost);
            list.addView(imgPhotoPost, params2);
        }

        TextView txtPadding = new TextView(MainActivity.this);
        txtPadding.setText("\n\n");
        list.addView(txtPadding, params2);

    }

    public void openLoginForm() {
        setContentView(R.layout.log_in_form);
        login = (LoginButton) findViewById(R.id.login_button);
        //login.setReadPermissions("user_friends", "email");

        // Callback registration
        login.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                LR = loginResult;
                AT = LR.getAccessToken();
                MyFbLib.setAt(AT);

                openWallForm();
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

        TextView lblAppName = (TextView) findViewById(R.id.lblAppName);
        lblAppName.setTextColor(Color.BLACK);
        lblAppName.setTextSize(40);
    }


    public void openProfileForm() {
        setContentView(R.layout.profile_form);
        Button btnBackToWall2 = (Button) findViewById(R.id.btnBackToWall2);
        btnBackToWall2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openWallForm();
            }
        });

        TextView lblProfileTitle = (TextView) findViewById(R.id.lblProfileTitle);
        lblProfileTitle.setText(Html.fromHtml("<u>My Profile</u>"));
        lblProfileTitle.setTextColor(Color.BLACK);
        lblProfileTitle.setTextSize(30);
    }

    public void openSettingsForm() {
        setContentView(R.layout.settings_form);
        Button btnBackToWall = (Button) findViewById(R.id.btnBackToWall);
        btnBackToWall.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openWallForm();
            }
        });

        TextView lblSettingsTitle = (TextView) findViewById(R.id.lblSettingsTitle);
        lblSettingsTitle.setText(Html.fromHtml("<u>Settings</u>"));
        lblSettingsTitle.setTextColor(Color.BLACK);
        lblSettingsTitle.setTextSize(30);
    }

}