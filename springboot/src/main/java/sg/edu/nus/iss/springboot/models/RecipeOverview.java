package sg.edu.nus.iss.springboot.models;

public class RecipeOverview {
    
    private int recipeId;
    private String title;
    private int protein;
    private int carbs;
    private int fats;
    private int calories;
    private String imageUrl;
    private String username;
    private int likes;

    public int getRecipeId() {
        return recipeId;
    }
    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public int getProtein() {
        return protein;
    }
    public void setProtein(int protein) {
        this.protein = protein;
    }
    public int getCarbs() {
        return carbs;
    }
    public void setCarbs(int carbs) {
        this.carbs = carbs;
    }
    public int getFats() {
        return fats;
    }
    public void setFats(int fats) {
        this.fats = fats;
    }
    public int getCalories() {
        return calories;
    }
    public void setCalories(int calories) {
        this.calories = calories;
    }
    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public int getLikes() {
        return likes;
    }
    public void setLikes(int likes) {
        this.likes = likes;
    }


    @Override
    public String toString() {
        return "RecipeOverview [recipeId=" + recipeId + ", title=" + title + ", protein=" + protein + ", carbs=" + carbs
                + ", fats=" + fats + ", calories=" + calories + ", imageUrl=" + imageUrl + ", username=" + username
                + ", likes=" + likes + "]";
    }

}
