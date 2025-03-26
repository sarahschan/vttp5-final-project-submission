package sg.edu.nus.iss.springboot.models;

import java.util.Date;
import java.util.List;

public class Recipe {
    private int recipe_id;
    private int user_id;
    private String username;
    private String title;
    private String description;
    private int protein;
    private int carbs;
    private int fats;
    private int calories;
    private int servings;
    private int prep_time;
    private int cook_time;
    private String image_url;
    private Date updated_at;
    private List<Ingredient> ingredients;
    private List<Instruction> instructions;

    
    public int getRecipe_id() {
        return recipe_id;
    }
    public void setRecipe_id(int recipe_id) {
        this.recipe_id = recipe_id;
    }
    public int getUser_id() {
        return user_id;
    }
    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
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
    public int getServings() {
        return servings;
    }
    public void setServings(int servings) {
        this.servings = servings;
    }
    public int getPrep_time() {
        return prep_time;
    }
    public void setPrep_time(int prep_time) {
        this.prep_time = prep_time;
    }
    public int getCook_time() {
        return cook_time;
    }
    public void setCook_time(int cook_time) {
        this.cook_time = cook_time;
    }
    public String getImage_url() {
        return image_url;
    }
    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
    public Date getUpdated_at() {
        return updated_at;
    }
    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }
    public List<Ingredient> getIngredients() {
        return ingredients;
    }
    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }
    public List<Instruction> getInstructions() {
        return instructions;
    }
    public void setInstructions(List<Instruction> instructions) {
        this.instructions = instructions;
    }

    
    @Override
    public String toString() {
        return "Recipe [recipe_id=" + recipe_id + ", user_id=" + user_id + ", username=" + username + ", title=" + title
                + ", description=" + description + ", protein=" + protein + ", carbs=" + carbs + ", fats=" + fats
                + ", calories=" + calories + ", servings=" + servings + ", prep_time=" + prep_time + ", cook_time="
                + cook_time + ", image_url=" + image_url + ", updated_at=" + updated_at + ", ingredients=" + ingredients
                + ", instructions=" + instructions + "]";
    }

}
