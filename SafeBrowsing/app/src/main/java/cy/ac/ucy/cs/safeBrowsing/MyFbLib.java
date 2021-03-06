package cy.ac.ucy.cs.safeBrowsing;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;


public class MyFbLib  {

    private static AtomicBoolean flag = new AtomicBoolean(false);
    private static final String TAG = MyFbLib.class.getSimpleName();
    private static AccessToken at= null;
    private static ArrayList<MyPost> givenPosts = new ArrayList<MyPost> ();
    private static ArrayList<MyPost> queuePost=new ArrayList<MyPost>();
    private static int counter=0;
//    private static boolean flag=false;
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
        final String userId =at.getUserId();
        Thread t= new Thread(){
            @Override
            public void run() {
                final GraphRequest requestPages=GraphRequest.newGraphPathRequest(at, "/" + userId + "/likes",
                        new GraphRequest.Callback() {
                            @Override
                            public void onCompleted(GraphResponse response) {
                                try {
                                    Log.d(TAG,"1st request");
                                    if(response.getError()!=null){
                                        return;
                                    }
                                    JSONObject json=response.getJSONObject();
                                    JSONArray jarray=json.getJSONArray("data");
                                    // JSONObject paging=response.getJSONObject();

                                    for(int i=0; i<jarray.length() || counter<10; i++){
                                        final JSONObject page=jarray.getJSONObject(i);
                                        GraphRequest request = GraphRequest.newGraphPathRequest(
                                                at,
                                                "/"+page.getString("id")+"/posts",      //get posts of the page using page id
                                                new GraphRequest.Callback() {
                                                    @Override
                                                    public void onCompleted(GraphResponse response) {
                                                        String postID=null;
                                                        Log.d(TAG,"2nd request");
                                                        if(response.getError()!=null){
                                                            return;
                                                        }
                                                        try {
                                                            JSONObject json=response.getJSONObject();
                                                            JSONArray jarray=null;
                                                            if(json.has("data")) {
                                                                jarray = json.getJSONArray("data");
                                                            }
                                                            JSONObject pagePost=jarray.getJSONObject(0);
                                                            if(pagePost.has("id")) {
                                                                postID = pagePost.getString("id");
                                                            }

                                                        }catch (Exception e){
                                                            e.printStackTrace();
                                                        }
                                                        GraphRequest request = GraphRequest.newGraphPathRequest(
                                                                at,
                                                                postID,                     //get fields of the post
                                                                new GraphRequest.Callback() {
                                                                    @Override
                                                                    public void onCompleted(GraphResponse response) {
                                                                        if(response.getError()!=null){
                                                                            return;
                                                                        }
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
                                                                                if(json.has("id")){
                                                                                    postID=json.getString("id");
                                                                                }
                                                                                if(json.has("message")){
                                                                                    message= json.getString("message");
                                                                                }
                                                                                if(page.has("name")){
                                                                                    pageName=page.getString("name");
                                                                                }
                                                                                if(json.has("created_time")){
                                                                                    created_time=json.getString("created_time");
                                                                                }
                                                                                if(page.has("picture")) {
                                                                                    JSONObject pic = page.getJSONObject("picture");
                                                                                    JSONObject data = pic.getJSONObject("data");
                                                                                    pagePicture = data.getString("url");
                                                                                }

                                                                            }catch (Exception e){
                                                                                e.printStackTrace();
                                                                            }

                                                                            MyPost status=new MyPost(postID,pageName,pagePicture,created_time,message);
                                                                            if(!checkDuplicates(status)){
                                                                                queuePost.add(status);
                                                                                Log.d(TAG,"insert post"+queuePost.size());
                                                                                counter++;
                                                                            }
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
                                                                                if(json.has("id")){
                                                                                    postID=json.getString("id");
                                                                                }
                                                                                if(page.has("name")){
                                                                                    pageName=page.getString("name");
                                                                                }
                                                                                if(page.has("picture")) {
                                                                                    JSONObject pic=page.getJSONObject("picture");
                                                                                    JSONObject data=pic.getJSONObject("data");
                                                                                    pagePicture=data.getString("url");
                                                                                }
                                                                                if(json.has("created_time")) {
                                                                                    created_time=json.getString("created_time");
                                                                                }
                                                                                if(json.has("link")){
                                                                                    urlLink=json.getString("link");
                                                                                }
                                                                                if(json.has("full_picture")){
                                                                                    urlImg=json.getString("full_picture");
                                                                                }
                                                                            }catch (Exception e){
                                                                                e.printStackTrace();
                                                                            }

                                                                            MyPost link=new MyLinkPost(postID,pageName,pagePicture,created_time,message,urlImg,urlLink);
                                                                            if(!checkDuplicates(link)){
                                                                                queuePost.add(link);
                                                                                counter++;
                                                                            }
                                                                            Log.d(TAG,link.toString());


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
                                                                                if(json.has("id")){
                                                                                    postID=json.getString("id");
                                                                                }
                                                                                if(page.has("name")){
                                                                                    pageName=page.getString("name");
                                                                                }
                                                                                if(page.has("picture")) {
                                                                                    JSONObject pic=page.getJSONObject("picture");
                                                                                    JSONObject data=pic.getJSONObject("data");
                                                                                    pagePicture=data.getString("url");
                                                                                }
                                                                                if(json.has("created_time")) {
                                                                                    created_time=json.getString("created_time");
                                                                                }
                                                                                if(json.has("link")){
                                                                                    urlLink=json.getString("link");
                                                                                }
                                                                                if(json.has("full_picture")){
                                                                                    urlImg=json.getString("full_picture");
                                                                                }

                                                                            }catch (Exception e){
                                                                                e.printStackTrace();
                                                                            }

                                                                            MyPost video=new MyVideoPost(postID,pageName,pagePicture,created_time,message,urlImg,urlLink);
                                                                            if(!checkDuplicates(video)){
                                                                                queuePost.add(video);
                                                                                counter++;
                                                                            }
                                                                            Log.d(TAG,video.toString());

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
                                                                                if(json.has("id")){
                                                                                    postID=json.getString("id");
                                                                                }
                                                                                if(page.has("name")){
                                                                                    pageName=page.getString("name");
                                                                                }
                                                                                if(page.has("picture")) {
                                                                                    JSONObject pic=page.getJSONObject("picture");
                                                                                    JSONObject data=pic.getJSONObject("data");
                                                                                    pagePicture=data.getString("url");
                                                                                }
                                                                                if(json.has("created_time")) {
                                                                                    created_time=json.getString("created_time");
                                                                                }
                                                                                if(json.has("full_picture")) {
                                                                                    pictureLink=json.getString("full_picture");
                                                                                }
                                                                            }catch (Exception e){
                                                                                e.printStackTrace();
                                                                            }

                                                                            MyPost photo=new MyPhotoPost(postID,pageName,pagePicture,created_time,message,pictureLink);
                                                                            if(!checkDuplicates(photo)) {
                                                                                queuePost.add(photo);
                                                                                counter++;
                                                                            }
                                                                            Log.d(TAG,photo.toString());

                                                                        }

                                                                    }
                                                                });

                                                        Bundle parameters = new Bundle();
                                                        parameters.putString("fields", "type,full_picture,message,link,likes,created_time");
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
                Bundle parameters = new Bundle();
                parameters.putString("fields", "picture,name");
                requestPages.setParameters(parameters);
                requestPages.executeAndWait();
            }
        };
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        //----------------------------------------------------------------------------------------


    }


    @NonNull
    private static Boolean checkDuplicates(MyPost post){
        for(int i=0; i< givenPosts.size(); i++){
            if (post.equals(givenPosts.get(i))){
                return true;
            }
        }
        return false;
    }



    public  static ArrayList<MyPost> getTenNextPosts () {
        ArrayList<MyPost> posts=new ArrayList<MyPost>();
        counter=0;

        fillPostQueue();
        MyProfile userProfile=getMyProfile();
        for (int i = 0; i < queuePost.size(); i++) {
            MyPost tmp = new MyPost(queuePost.get(i));
            if (!checkDuplicates(queuePost.get(i))) {
                givenPosts.add(queuePost.get(i));
                posts.add(queuePost.get(i));
            }
        }

        return posts;
    }

    public static MyProfile getMyProfile () {
        final MyProfile[] userProfileOrg = new MyProfile[1];
        final boolean[] flag = new boolean[1];
        flag[0]= false;
        Thread t= new Thread() {
            @Override
            public void run() {
                GraphRequest request = GraphRequest.newMeRequest(
                        at,
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                String id = null;
                                String name=null;
                                String profilePicLink = null;
                                String about = null;
                                String birthday = null;
                                String email = null;
                                String gender = null;
                                String homeTown = null;
                                String timelineLink = null;

                                try{
                                    JSONObject profile= response.getJSONObject();
                                    if(response.getError()!=null){
                                        flag[0]= true;
                                    }
                                    if(profile.has("birthday")){
                                        birthday=profile.getString("birthday");
                                    }
                                    if(profile.has("name")){
                                        name=profile.getString("name");
                                    }
                                    if(profile.has("email")){
                                        email=profile.getString("email");
                                    }
                                    if(profile.has("picture")){
                                        JSONObject picture=profile.getJSONObject("picture");
                                        JSONObject picData=picture.getJSONObject("data");
                                        profilePicLink=picData.getString("url");
                                    }

                                    if(profile.has("about")){
                                        about=profile.getString("about");
                                    }
                                    if(profile.has("gender")){
                                        gender=profile.getString("gender");
                                    }
                                    if(profile.has("hometown")){
                                        JSONObject town=profile.getJSONObject("hometown");
                                        homeTown=town.getString("name");
                                    }
                                    if(profile.has("link")){
                                        timelineLink=profile.getString("link");
                                    }
                                    if(profile.has("id")){
                                        id=profile.getString("id");
                                    }



                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                                MyProfile userProfile;
                                if (flag[0]==true) {
                                    userProfile= null;
                                } else {
                                    userProfile=new MyProfile(id,name,profilePicLink,about,birthday,email,gender,homeTown,timelineLink);
                                    Log.d(TAG,userProfile.toString());
                                }
                                userProfileOrg[0] =userProfile;
                            }
                        });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "birthday,email,picture,about,gender,hometown,link,name");
                request.setParameters(parameters);
                request.executeAndWait();

            }

        };
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return userProfileOrg[0];
    }

}
