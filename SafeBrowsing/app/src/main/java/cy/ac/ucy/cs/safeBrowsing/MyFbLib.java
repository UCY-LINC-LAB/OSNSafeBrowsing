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

public class MyFbLib {

    private static final String TAG = MyFbLib.class.getSimpleName();
    private static AccessToken at= null;
    private static ArrayList<MyPost> givenPosts = new ArrayList<MyPost> ();
    private static ArrayList<MyPost> queuePost=new ArrayList<MyPost>();

    public static AccessToken getAt() {
        return at;
    }

    public static void setAt(AccessToken accessT) {
        at = accessT;
    }

    public static void fillPostQueue(){
        int i=0;
        String userId=at.getUserId();
        GraphRequest requestPages=GraphRequest.newGraphPathRequest(at, "/" + userId + "/likes",
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        try {
                            JSONObject json=response.getJSONObject();
                            JSONArray jarray=json.getJSONArray("data");
                            for(int i=0; i<jarray.length(); i++){
                                final JSONObject page=jarray.getJSONObject(i);

                                GraphRequest request = GraphRequest.newGraphPathRequest(
                                        at,
                                        "/"+page.getString("id")+"/posts",      //get posts of the page using page id
                                        new GraphRequest.Callback() {
                                            @Override
                                            public void onCompleted(GraphResponse response) {
                                                String postID=null;
                                                try {
                                                    JSONObject json=response.getJSONObject();
                                                    JSONArray jarray=json.getJSONArray("data");
                                                    JSONObject pagePost=jarray.getJSONObject(0);
                                                    postID=pagePost.getString("id");

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
                                                                        message= json.getString("message");
                                                                        pageName=page.getString("name");
                                                                        JSONObject pic=page.getJSONObject("data");
                                                                        pagePicture=pic.getString("url");
                                                                        created_time=json.getString("created_time");
                                                                    }catch (Exception e){
                                                                        e.printStackTrace();
                                                                    }

                                                                    MyPost status=new MyPost(postID,pageName,pagePicture,created_time,message);
                                                                    queuePost.add(status);

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
                                                                    queuePost.add(link);

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

                                                                    MyPost link=new MyVideoPost(postID,pageName,pagePicture,created_time,message,urlImg,urlLink);
                                                                    queuePost.add(link);

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

                                                                    MyPost link=new MyPhotoPost(postID,pageName,pagePicture,created_time,message,pictureLink);
                                                                    queuePost.add(link);

                                                                }


                                                            }
                                                        });

                                                Bundle parameters = new Bundle();
                                                parameters.putString("fields", "type,full_picture,message,link,likes");
                                                request.setParameters(parameters);
                                                request.executeAndWait();

                                            }
                                        });
                                Bundle parameters = new Bundle();
                                parameters.putString("fields", "picture,name");
                                request.setParameters(parameters);
                                request.executeAndWait();
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });

        requestPages.executeAndWait();


        //----------------------------------------------------------------------------------------
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

        /*  Bundle parameters = new Bundle();
            parameters.putString("fields", "name");
            request.setParameters(parameters);
           */
        request.executeAsync();
        i++;

    }

    public static ArrayList<MyPost> getTenNextPosts () {
        ArrayList<MyPost> posts=new ArrayList<MyPost>();
        fillPostQueue();
        for(int i=0; i<queuePost.size(); i++){
            MyPost tmp=new MyPost(queuePost.get(i));
            queuePost.remove(i);
            givenPosts.add(tmp);
            queuePost.add(tmp);
        }

        return posts;
    }

    public static MyProfile getMyProfile () {
        return null;
    }

}
