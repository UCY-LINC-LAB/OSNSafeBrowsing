package cy.ac.ucy.cs.safeBrowsing;

public class MyPost {

    private String id= null;
    private String authorName= null;
    private String authorImg= null;
    private String createdTime= null;
    private String message = null;
    private String likesCounter = null;
    private String commentsCounter = null;
    private String shareCounters = null;

    MyPost(){}

    MyPost(String postId,String pageName,String pagePicture,String createdTime,String message){
        this.id=postId;
        this.authorName=pageName;
        this.authorImg=pagePicture;
        this.createdTime=createdTime;
        this.message=message;
    }

    MyPost (MyPost tmp){
        this.id=tmp.id;
        this.authorName=tmp.authorName;
        this.authorImg=tmp.authorImg;
        this.createdTime=tmp.createdTime;
        this.message=tmp.message;
        this.likesCounter=tmp.likesCounter;
        this.shareCounters=tmp.shareCounters;
    }

    public String getLikesCounter() {
        return likesCounter;
    }

    public void setLikesCounter(String likesCounter) {
        this.likesCounter = likesCounter;
    }

    public String getCommentsCounter() {
        return commentsCounter;
    }

    public void setCommentsCounter(String commentsCounter) {
        this.commentsCounter = commentsCounter;
    }

    public String getShareCounters() {
        return shareCounters;
    }

    public void setShareCounters(String shareCounters) {
        this.shareCounters = shareCounters;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorImg() {
        return authorImg;
    }

    public void setAuthorImg(String authorImg) {
        this.authorImg = authorImg;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }





}
