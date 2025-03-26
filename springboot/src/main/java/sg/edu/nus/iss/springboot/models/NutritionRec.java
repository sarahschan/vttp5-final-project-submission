package sg.edu.nus.iss.springboot.models;

public class NutritionRec {
    
    private int carbs;
    private String carbsRange;

    private int protein;
    private String proteinRange;

    private int fat;
    private String fatRange;

    private String saturatedFat;

    private String sugar;

    private int calories;
    private int tdee;
    private int bmr;

    private double weight;
    private double height;
    private int age;
    private String sex;
    private String activityLevel;
    private String goal;

    
    
    public NutritionRec() {
    }

    
    public NutritionRec(int carbs, String carbsRange, int protein, String proteinRange, int fat, String fatRange,
            String saturatedFat, String sugar, int calories, int tdee, int bmr, double weight, double height, int age,
            String sex, String activityLevel, String goal) {
        this.carbs = carbs;
        this.carbsRange = carbsRange;
        this.protein = protein;
        this.proteinRange = proteinRange;
        this.fat = fat;
        this.fatRange = fatRange;
        this.saturatedFat = saturatedFat;
        this.sugar = sugar;
        this.calories = calories;
        this.tdee = tdee;
        this.bmr = bmr;
        this.weight = weight;
        this.height = height;
        this.age = age;
        this.sex = sex;
        this.activityLevel = activityLevel;
        this.goal = goal;
    }


    public int getCarbs() {
        return carbs;
    }
    public void setCarbs(int carbs) {
        this.carbs = carbs;
    }
    public String getCarbsRange() {
        return carbsRange;
    }
    public void setCarbsRange(String carbsRange) {
        this.carbsRange = carbsRange;
    }
    public int getProtein() {
        return protein;
    }
    public void setProtein(int protein) {
        this.protein = protein;
    }
    public String getProteinRange() {
        return proteinRange;
    }
    public void setProteinRange(String proteinRange) {
        this.proteinRange = proteinRange;
    }
    public int getFat() {
        return fat;
    }
    public void setFat(int fat) {
        this.fat = fat;
    }
    public String getFatRange() {
        return fatRange;
    }
    public void setFatRange(String fatRange) {
        this.fatRange = fatRange;
    }
    public String getSaturatedFat() {
        return saturatedFat;
    }
    public void setSaturatedFat(String saturatedFat) {
        this.saturatedFat = saturatedFat;
    }
    public String getSugar() {
        return sugar;
    }
    public void setSugar(String sugar) {
        this.sugar = sugar;
    }
    public int getCalories() {
        return calories;
    }
    public void setCalories(int calories) {
        this.calories = calories;
    }
    public int getTdee() {
        return tdee;
    }
    public void setTdee(int tdee) {
        this.tdee = tdee;
    }
    public int getBmr() {
        return bmr;
    }
    public void setBmr(int bmr) {
        this.bmr = bmr;
    }
    public double getWeight() {
        return weight;
    }
    public void setWeight(double weight) {
        this.weight = weight;
    }
    public double getHeight() {
        return height;
    }
    public void setHeight(double height) {
        this.height = height;
    }
    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }
    public String getSex() {
        return sex;
    }
    public void setSex(String sex) {
        this.sex = sex;
    }
    public String getActivityLevel() {
        return activityLevel;
    }
    public void setActivityLevel(String activityLevel) {
        this.activityLevel = activityLevel;
    }
    public String getGoal() {
        return goal;
    }
    public void setGoal(String goal) {
        this.goal = goal;
    }


    @Override
    public String toString() {
        return "NutritionRec [carbs=" + carbs + ", carbsRange=" + carbsRange + ", protein=" + protein
                + ", proteinRange=" + proteinRange + ", fat=" + fat + ", fatRange=" + fatRange + ", saturatedFat="
                + saturatedFat + ", sugar=" + sugar + ", calories=" + calories + ", tdee=" + tdee + ", bmr=" + bmr
                + ", weight=" + weight + ", height=" + height + ", age=" + age + ", gender=" + sex
                + ", activityLevel=" + activityLevel + ", goal=" + goal + "]";
    }

}
