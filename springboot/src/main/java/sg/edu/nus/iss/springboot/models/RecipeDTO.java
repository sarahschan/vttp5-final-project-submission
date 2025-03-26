package sg.edu.nus.iss.springboot.models;

import java.util.List;

public class RecipeDTO {
    
    private String title;
    private String description;
    private Integer protein;
    private Integer carbs;
    private Integer fats;
    private Integer calories;
    private Integer servings;
    private Integer prep_time;
    private Integer cook_time;

    private List<Ingredient> ingredients;
    private List<Instruction> instructions;

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
    public Integer getProtein() {
        return protein;
    }
    public void setProtein(Integer protein) {
        this.protein = protein;
    }
    public Integer getCarbs() {
        return carbs;
    }
    public void setCarbs(Integer carbs) {
        this.carbs = carbs;
    }
    public Integer getFats() {
        return fats;
    }
    public void setFats(Integer fats) {
        this.fats = fats;
    }
    public Integer getCalories() {
        return calories;
    }
    public void setCalories(Integer calories) {
        this.calories = calories;
    }
    public Integer getServings() {
        return servings;
    }
    public void setServings(Integer servings) {
        this.servings = servings;
    }
    public Integer getPrep_time() {
        return prep_time;
    }
    public void setPrep_time(Integer prep_time) {
        this.prep_time = prep_time;
    }
    public Integer getCook_time() {
        return cook_time;
    }
    public void setCook_time(Integer cook_time) {
        this.cook_time = cook_time;
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
        return "RecipeDTO [title=" + title + ", description=" + description + ", protein=" + protein + ", carbs="
                + carbs + ", fats=" + fats + ", calories=" + calories + ", servings=" + servings + ", prep_time="
                + prep_time + ", cook_time=" + cook_time + ", ingredients=" + ingredients + ", instructions="
                + instructions + "]";
    }

}
