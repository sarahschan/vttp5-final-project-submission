package sg.edu.nus.iss.springboot.models;

public class Ingredient {
    
    private String ingredient;
    private String amount;
    private String unit;
    private String notes;
    
    public String getIngredient() {
        return ingredient;
    }
    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }
    public String getAmount() {
        return amount;
    }
    public void setAmount(String amount) {
        this.amount = amount;
    }
    public String getUnit() {
        return unit;
    }
    public void setUnit(String unit) {
        this.unit = unit;
    }
    public String getNotes() {
        return notes;
    }
    public void setNotes(String notes) {
        this.notes = notes;
    }


    @Override
    public String toString() {
        return "Ingredient [ingredient=" + ingredient + ", amount=" + amount + ", unit=" + unit + ", notes=" + notes
                + "]";
    }

}
