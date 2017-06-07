package cy.ac.ucy.cs.safeBrowsing;


public class MyProfile {

    private String id = null;
    private String name = null;
    private String profilePicLink = null;
    private String about = null;
    private String birthday = null;
    private String email = null;
    private String gender = null;
    private String homeTown = null;
    private String timelineLink = null;

    public MyProfile(String id, String name,String profilePicLink, String about, String birthday, String email, String gender, String homeTown, String timelineLink) {
        this.id = id;
        this.name=name;
        this.profilePicLink = profilePicLink;
        this.about = about;
        this.birthday = birthday;
        this.email = email;
        this.gender = gender;
        this.homeTown = homeTown;
        this.timelineLink = timelineLink;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProfilePicLink() {
        return profilePicLink;
    }

    public void setProfilePicLink(String profilePicLink) {
        this.profilePicLink = profilePicLink;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getHomeTown() {
        return homeTown;
    }

    public void setHomeTown(String homeTown) {
        this.homeTown = homeTown;
    }

    public String getTimelineLink() {
        return timelineLink;
    }

    public void setTimelineLink(String timelineLink) {
        this.timelineLink = timelineLink;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }


    @Override
    public String toString() {
        return "MyProfile{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", profilePicLink='" + profilePicLink + '\'' +
                ", about='" + about + '\'' +
                ", birthday='" + birthday + '\'' +
                ", email='" + email + '\'' +
                ", gender='" + gender + '\'' +
                ", homeTown='" + homeTown + '\'' +
                ", timelineLink='" + timelineLink + '\'' +
                '}';
    }

}
