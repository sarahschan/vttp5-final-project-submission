package sg.edu.nus.iss.springboot.models;

public class FollowProfile {
    
    private int userId;
    private String username;
    private String profilePicture;


    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getProfilePicture() {
        return profilePicture;
    }
    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    
    @Override
    public String toString() {
        return "FollowProfile [userId=" + userId + ", username=" + username + ", profilePicture=" + profilePicture
                + "]";
    }

}