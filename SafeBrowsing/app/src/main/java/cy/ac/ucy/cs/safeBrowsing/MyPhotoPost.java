package cy.ac.ucy.cs.safeBrowsing;

public class MyPhotoPost extends MyPost {

    private String pictureLink= null;

    MyPhotoPost(String postId,String pageName,String pagePicture,String createdTime,String message,String pictureLink){
        super(postId,pageName,pagePicture,createdTime,message);
        this.pictureLink=pictureLink;
    }

    public String getPictureLink() {
        return pictureLink;
    }

    public void setPictureLink(String pictureLink) {
        this.pictureLink = pictureLink;
    }
}
