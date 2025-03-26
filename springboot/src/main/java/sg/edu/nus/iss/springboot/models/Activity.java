package sg.edu.nus.iss.springboot.models;

import java.time.LocalDateTime;

public class Activity {
    
    private int activityId;
    private String activityType;
    private Integer userId;
    private String username;
    private String userProfilePicture;
    private Integer recipeId;
    private String recipeTitle;
    private String recipeImageUrl;
    private Integer commentId;
    private String commentText;
    private Integer followedUserId;
    private String followedUsername;
    private String followedUserProfilePicture;
    private LocalDateTime timestamp;

    
    public int getActivityId() {
        return activityId;
    }
    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }
    public String getActivityType() {
        return activityType;
    }
    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }
    public Integer getUserId() {
        return userId;
    }
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getUserProfilePicture() {
        return userProfilePicture;
    }
    public void setUserProfilePicture(String userProfilePicture) {
        this.userProfilePicture = userProfilePicture;
    }
    public Integer getRecipeId() {
        return recipeId;
    }
    public void setRecipeId(Integer recipeId) {
        this.recipeId = recipeId;
    }
    public String getRecipeTitle() {
        return recipeTitle;
    }
    public void setRecipeTitle(String recipeTitle) {
        this.recipeTitle = recipeTitle;
    }
    public String getRecipeImageUrl() {
        return recipeImageUrl;
    }
    public void setRecipeImageUrl(String recipeImageUrl) {
        this.recipeImageUrl = recipeImageUrl;
    }
    public Integer getCommentId() {
        return commentId;
    }
    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }
    public String getCommentText() {
        return commentText;
    }
    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }
    public Integer getFollowedUserId() {
        return followedUserId;
    }
    public void setFollowedUserId(Integer followedUserId) {
        this.followedUserId = followedUserId;
    }
    public String getFollowedUsername() {
        return followedUsername;
    }
    public void setFollowedUsername(String followedUsername) {
        this.followedUsername = followedUsername;
    }
    public String getFollowedUserProfilePicture() {
        return followedUserProfilePicture;
    }
    public void setFollowedUserProfilePicture(String followedUserProfilePicture) {
        this.followedUserProfilePicture = followedUserProfilePicture;
    }
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }


    @Override
    public String toString() {
        return "Activity [activityId=" + activityId + ", activityType=" + activityType + ", userId=" + userId
                + ", username=" + username + ", userProfilePicture=" + userProfilePicture + ", recipeId=" + recipeId
                + ", recipeTitle=" + recipeTitle + ", recipeImageUrl=" + recipeImageUrl + ", commentId=" + commentId
                + ", commentText=" + commentText + ", followedUserId=" + followedUserId + ", followedUsername="
                + followedUsername + ", followedUserProfilePicture=" + followedUserProfilePicture + ", timestamp="
                + timestamp + "]";
    }

}