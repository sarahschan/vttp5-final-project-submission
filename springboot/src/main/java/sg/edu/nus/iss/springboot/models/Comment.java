package sg.edu.nus.iss.springboot.models;

import org.springframework.jdbc.support.rowset.SqlRowSet;

public class Comment {
    
    private String username;
    private String profile_picture;
    private String comment;


    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getProfile_picture() {
        return profile_picture;
    }
    public void setProfile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
    }
    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }


    @Override
    public String toString() {
        return "Comment [username=" + username + ", profile_picture=" + profile_picture + ", comment=" + comment + "]";
    }

    
    public static Comment toComment(SqlRowSet rs) {
        
        Comment comment = new Comment();
            comment.setUsername(rs.getString("username"));
            comment.setProfile_picture(rs.getString("profile_picture"));
            comment.setComment(rs.getString("comment"));

        return comment;
    }
}
