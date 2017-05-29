package cy.ac.ucy.cs.safeBrowsing;

public class MyVideoPost extends MyPost {

    private String videoLink= null;
    private String videoImg= null;

    MyVideoPost(String postId, String pageName, String pagePicture, String createdTime, String message, String videoImg, String videoLink){
        super(postId,pageName,pagePicture,createdTime,message);
        this.videoImg=videoImg;
        this.videoLink=videoLink;
    }

    public String getVideoLink() {
        return videoLink;
    }

    public void setVideoLink(String videoLink) {
        this.videoLink = videoLink;
    }

    public String getVideoImg() {
        return videoImg;
    }

    public void setVideoImg(String videoImg) {
        this.videoImg = videoImg;
    }


    @Override
    public String toString() {
        return super.toString()+"MyVideoPost{" +
                "videoLink='" + videoLink + '\'' +
                ", videoImg='" + videoImg + '\'' +
                '}';
    }


}
