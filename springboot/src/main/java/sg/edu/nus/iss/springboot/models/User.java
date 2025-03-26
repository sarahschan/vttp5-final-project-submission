package sg.edu.nus.iss.springboot.models;

import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.lang.Nullable;

public class User {
    
    @Nullable
    private int userId;

    private String name;
    private String username;
    private String email;
    private String password;

    @Nullable
    private String profile_picture;

    @Nullable
    private String short_bio;
    


    @Override
    public String toString() {
        return "User [userId=" + userId + ", name=" + name + ", username=" + username + ", email=" + email
                + ", password=" + password + ", profile_picture=" + profile_picture + ", short_bio=" + short_bio + "]";
    }



    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfile_picture() {
        return profile_picture;
    }

    public void setProfile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
    }

    public String getShort_bio() {
        return short_bio;
    }

    public void setShort_bio(String short_bio) {
        this.short_bio = short_bio;
    }



    public static User toUser(SqlRowSet rs) {
        User user = new User();
        try {
            user.setUserId(rs.getInt("id"));
            user.setName(rs.getString("name"));
            user.setUsername(rs.getString("username"));
            user.setEmail(rs.getString("email"));
            user.setPassword(rs.getString("password_hash"));
            user.setProfile_picture(rs.getString("profile_picture"));
            user.setShort_bio(rs.getString("short_bio"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }
    
}
