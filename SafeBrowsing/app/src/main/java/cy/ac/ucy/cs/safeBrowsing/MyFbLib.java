package cy.ac.ucy.cs.safeBrowsing;

import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.spec.ECField;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class MyFbLib  {

    private static AtomicBoolean flag = new AtomicBoolean(false);
    private static final String TAG = MyFbLib.class.getSimpleName();
    private static AccessToken at= null;
    private static ArrayList<MyPost> givenPosts = new ArrayList<MyPost> ();
    private static ArrayList<MyPost> queuePost=new ArrayList<MyPost>();
    private static int counter=0;
//    private static boolean flag=false;
    static Thread t;
    public static AccessToken getAt() {
        return at;
    }
    public static ArrayList<MyPost> getGivenPosts(){
        return queuePost;
    }

    public  static void setAt(AccessToken accessT) {
        at = accessT;
    }

    public  static void fillPostQueue(){
        int i=0;
        String userId=at.getUserId();
        final GraphRequest requestPages=GraphRequest.newGraphPathRequest(at, "/" + userId + "/likes",
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        try {
                            Log.d(TAG,"1st request");
                            JSONObject json=response.getJSONObject();
                            JSONArray jarray=json.getJSONArray("data");
                           // JSONObject paging=response.getJSONObject();

                            for(int i=0; i<jarray.length() || counter>=2; i++){
                                final JSONObject page=jarray.getJSONObject(i);
                                GraphRequest request = GraphRequest.newGraphPathRequest(
                                        at,
                                        "/"+page.getString("id")+"/posts",      //get posts of the page using page id
                                        new GraphRequest.Callback() {
                                            @Override
                                            public void onCompleted(GraphResponse response) {
                                                String postID=null;
                                                Log.d(TAG,"2nd request");
                                                try {
                                                    JSONObject json=response.getJSONObject();
                                                    JSONArray jarray=json.getJSONArray("data");
                                                    JSONObject pagePost=jarray.getJSONObject(0);
                                                    postID=pagePost.getString("id");
                                                    Log.d(TAG,"thisLalalaala:"+postID);

                                                }catch (Exception e){
                                                    e.printStackTrace();
                                                }
                                                GraphRequest request = GraphRequest.newGraphPathRequest(
                                                        at,
                                                        postID,                     //get fields of the post
                                                        new GraphRequest.Callback() {
                                                            @Override
                                                            public void onCompleted(GraphResponse response) {
                                                                JSONObject json=response.getJSONObject();
                                                                String postType=null;
                                                                Log.d(TAG,"3rd request");
                                                                try {
                                                                    postType= json.getString("type");
                                                                }catch (Exception e){
                                                                    e.printStackTrace();
                                                                }
                                                                if(postType.compareTo("status")==0){
                                                                    String message=null;
                                                                    String postID=null;
                                                                    String pageName=null;
                                                                    String pagePicture=null;
                                                                    String created_time=null;

                                                                    //Lipoun ta likes,comments,shares
                                                                    try {

                                                                        postID=json.getString("id");
                                                                        message= json.getString("message");
                                                                        pageName=page.getString("name");
                                                                        created_time=json.getString("created_time");
                                                                        JSONObject pic=page.getJSONObject("picture");
                                                                        JSONObject data=pic.getJSONObject("data");
                                                                        pagePicture=data.getString("url");


                                                                    }catch (Exception e){
                                                                        e.printStackTrace();
                                                                    }

                                                                    MyPost status=new MyPost(postID,pageName,pagePicture,created_time,message);
                                                                    //if(checkDuplicates(status)){

                                                                        queuePost.add(status);
                                                                    Log.d(TAG,"insert post"+queuePost.size());
                                                                        counter++;
                                                                   // }
                                                                    Log.d(TAG,status.toString());


                                                                }
                                                                if(postType.compareTo("link")==0){
                                                                    String message=null;
                                                                    String postID=null;
                                                                    String pageName=null;
                                                                    String pagePicture=null;
                                                                    String created_time=null;
                                                                    String urlLink=null;
                                                                    String urlImg=null;
                                                                    //Lipoun ta likes,comments,shares
                                                                    try {
                                                                        if(json.has("message")) {
                                                                            message = json.getString("message");
                                                                        }
                                                                        pageName=page.getString("name");
                                                                        JSONObject pic=page.getJSONObject("data");
                                                                        pagePicture=pic.getString("url");
                                                                        created_time=json.getString("created_time");
                                                                        urlLink=json.getString("link");
                                                                        urlImg=json.getString("full_picture");
                                                                    }catch (Exception e){
                                                                        e.printStackTrace();
                                                                    }

                                                                    MyPost link=new MyLinkPost(postID,pageName,pagePicture,created_time,message,urlImg,urlLink);
                                                                    if(!checkDuplicates(link)){
                                                                        queuePost.add(link);
                                                                        counter++;
                                                                    }


                                                                }
                                                                if(postType.compareTo("video")==0){
                                                                    String message=null;
                                                                    String postID=null;
                                                                    String pageName=null;
                                                                    String pagePicture=null;
                                                                    String created_time=null;
                                                                    String urlLink=null;
                                                                    String urlImg=null;
                                                                    //Lipoun ta likes,comments,shares
                                                                    try {
                                                                        if(json.has("message")) {
                                                                            message = json.getString("message");
                                                                        }
                                                                        pageName=page.getString("name");
                                                                        JSONObject pic=page.getJSONObject("data");
                                                                        pagePicture=pic.getString("url");
                                                                        created_time=json.getString("created_time");
                                                                        urlLink=json.getString("link");
                                                                        urlImg=json.getString("full_picture");
                                                                    }catch (Exception e){
                                                                        e.printStackTrace();
                                                                    }

                                                                    MyPost video=new MyVideoPost(postID,pageName,pagePicture,created_time,message,urlImg,urlLink);
                                                                    if(!checkDuplicates(video)){
                                                                        queuePost.add(video);
                                                                        counter++;

                                                                    }

                                                                }
                                                                if(postType.compareTo("photo")==0){
                                                                    String message=null;
                                                                    String postID=null;
                                                                    String pageName=null;
                                                                    String pagePicture=null;
                                                                    String created_time=null;
                                                                    String pictureLink=null;
                                                                    //Lipoun ta likes,comments,shares
                                                                    try {
                                                                        if(json.has("message")) {
                                                                            message = json.getString("message");
                                                                        }
                                                                        pageName=page.getString("name");
                                                                        JSONObject pic=page.getJSONObject("data");
                                                                        pagePicture=pic.getString("url");
                                                                        created_time=json.getString("created_time");
                                                                        pictureLink=json.getString("full_picture");
                                                                    }catch (Exception e){
                                                                        e.printStackTrace();
                                                                    }

                                                                    MyPost photo=new MyPhotoPost(postID,pageName,pagePicture,created_time,message,pictureLink);
                                                                    if(!checkDuplicates(photo)) {
                                                                        queuePost.add(photo);
                                                                        counter++;
                                                                    }

                                                                }
                                                                flag.set(true);

                                                            }
                                                        });

                                                Bundle parameters = new Bundle();
                                                parameters.putString("fields", "type,full_picture,message,link,likes,created_time");
                                                request.setParameters(parameters);
                                                request.executeAsync();

                                            }
                                        });
                                Bundle parameters = new Bundle();
                                parameters.putString("fields", "picture,name");
                                request.setParameters(parameters);
                                request.executeAsync();
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "picture,name");
        requestPages.setParameters(parameters);
        requestPages.executeAsync();


        //----------------------------------------------------------------------------------------


    }

    private static Boolean checkDuplicates(MyPost post){
        for(int i=0; i< givenPosts.size(); i++){
            if (post.equals(givenPosts.get(i))){
                return true;
            }
        }
        return false;
    }

//    public static boolean

    public  static ArrayList<MyPost> getTenNextPosts () {
        ArrayList<MyPost> posts=new ArrayList<MyPost>();
        counter=0;
//        flag=false;
        fillPostQueue();

        for (int i = 0; i < queuePost.size(); i++) {
            MyPost tmp = new MyPost(queuePost.get(i));
            if (!checkDuplicates(tmp)) {
                queuePost.remove(i);
                givenPosts.add(tmp);
                Log.d(TAG, tmp.toString());
                queuePost.add(tmp);
            }
        }

        return posts;
    }

    public static MyProfile getMyProfile () {
        int i=0;
        String userId=at.getUserId();
        GraphRequest request = GraphRequest.newGraphPathRequest(
                at,
                "/"+userId+"/posts",
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        String message=null;
                            String createdTime=null;
                        String postId=null;
                        try {
                            JSONObject json = response.getJSONObject();
                            JSONArray jarray = json.getJSONArray("data");
                            for(int i=0; i<jarray.length(); i++) {
                                JSONObject post = jarray.getJSONObject(i);
                                message = post.getString("message");
                                createdTime=post.getString("created_time");
                                postId=post.getString("id");
                                Log.d(TAG,message);
                                Log.d(TAG,createdTime);
                                Log.d(TAG,postId);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });

            Bundle parameters = new Bundle();
            parameters.putString("fields", "name");
            request.setParameters(parameters);

        request.executeAsync();
        i++;
        return null;
    }

}
