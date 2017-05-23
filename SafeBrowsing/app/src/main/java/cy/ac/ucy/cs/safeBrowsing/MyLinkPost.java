package cy.ac.ucy.cs.safeBrowsing;

public class MyLinkPost extends MyPost {

    private String URLLink= null;
    private String URLImg= null;

    MyLinkPost(String postId,String pageName,String pagePicture,String createdTime,String message,String urlImg,String urlLink){
        super(postId,pageName,pagePicture,createdTime,message);
        this.URLImg=urlImg;
        this.URLLink=urlLink;
    }

    public String getURLLink() {
        return URLLink;
    }

    public void setURLLink(String URLLink) {
        this.URLLink = URLLink;
    }

    public String getURLImg() {
        return URLImg;
    }

    public void setURLImg(String URLImg) {
        this.URLImg = URLImg;
    }
}
