package sg.edu.nus.iss.springboot.models;

public class RecipeFilterCriteria {
    
    private int minCalories;
    private int maxCalories;
    private int minProtein;
    private int maxProtein;
    private int minCarbs;
    private int maxCarbs;
    private int minFats;
    private int maxFats;


    public int getMinCalories() {
        return minCalories;
    }
    public void setMinCalories(int minCalories) {
        this.minCalories = minCalories;
    }
    public int getMaxCalories() {
        return maxCalories;
    }
    public void setMaxCalories(int maxCalories) {
        this.maxCalories = maxCalories;
    }
    public int getMinProtein() {
        return minProtein;
    }
    public void setMinProtein(int minProtein) {
        this.minProtein = minProtein;
    }
    public int getMaxProtein() {
        return maxProtein;
    }
    public void setMaxProtein(int maxProtein) {
        this.maxProtein = maxProtein;
    }
    public int getMinCarbs() {
        return minCarbs;
    }
    public void setMinCarbs(int minCarbs) {
        this.minCarbs = minCarbs;
    }
    public int getMaxCarbs() {
        return maxCarbs;
    }
    public void setMaxCarbs(int maxCarbs) {
        this.maxCarbs = maxCarbs;
    }
    public int getMinFats() {
        return minFats;
    }
    public void setMinFats(int minFats) {
        this.minFats = minFats;
    }
    public int getMaxFats() {
        return maxFats;
    }
    public void setMaxFats(int maxFats) {
        this.maxFats = maxFats;
    }

    @Override
    public String toString() {
        return "RecipeFilterCriteria [minCalories=" + minCalories + ", maxCalories=" + maxCalories + ", minProtein="
                + minProtein + ", maxProtein=" + maxProtein + ", minCarbs=" + minCarbs + ", maxCarbs=" + maxCarbs
                + ", minFats=" + minFats + ", maxFats=" + maxFats + "]";
    }

}
