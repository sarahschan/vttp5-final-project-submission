package sg.edu.nus.iss.springboot.utilities;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.springframework.stereotype.Component;

import sg.edu.nus.iss.springboot.models.Ingredient;
import sg.edu.nus.iss.springboot.models.Instruction;
import sg.edu.nus.iss.springboot.models.NutritionRec;
import sg.edu.nus.iss.springboot.models.RecipeDTO;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonValue;

@Component
public class JsonParser {
    
    public RecipeDTO toRecipeDTO(String recipeJString) {
        // {   
        //     "title":"Chicken Recipe",
        //     "description":"I love CHICKEN",
        //     "protein":10,
        //     "carbs":2,
        //     "fats":1,
        //     "calories":206,
        //     "servings":1,
        //     "prep_time":15,
        //     "cook_time":15,
        //     "ingredients":[
        //         {   "ingredient":"chicken",
        //             "amount":200,
        //             "unit":"g",
        //             "notes":"season however you like"},
        //         {   "ingredient":"garlic",
        //             "amount":1,
        //             "unit":"clove",
        //             "notes":"to taste"}
        //         ],
        //     "instructions":[
        //         {"step":"prep the chicken and garlic"},
        //         {"step":"cook the chicken and garlic"},
        //         {"step":"serve the chicken and garlic"}
        //     ]
        // }
        
        JsonObject recipeJObject = Json.createReader(new StringReader(recipeJString)).readObject();

        List<Ingredient> ingredients = new ArrayList<>();
        JsonArray ingredientsJArray = recipeJObject.getJsonArray("ingredients");
        for (JsonValue ingredientJValue : ingredientsJArray) {
            JsonObject ingredientJObject = ingredientJValue.asJsonObject();
            Ingredient i = new Ingredient();
                i.setIngredient(ingredientJObject.getString("ingredient"));
                i.setAmount(ingredientJObject.getString("amount"));
                i.setUnit(ingredientJObject.getString("unit"));
                i.setNotes(ingredientJObject.getString("notes"));
            ingredients.add(i);
        }

        
        List<Instruction> instructions = new ArrayList<>();
        JsonArray instructionsJArray = recipeJObject.getJsonArray("instructions");
        for (JsonValue instructionJValue : instructionsJArray) {
            JsonObject instructionJObject = instructionJValue.asJsonObject();
            Instruction i = new Instruction();
                i.setStep(instructionJObject.getString("step"));
            instructions.add(i);
        }

        RecipeDTO recipeDTO = new RecipeDTO();
            recipeDTO.setTitle(recipeJObject.getString("title"));
            recipeDTO.setDescription(recipeJObject.getString("description"));
            recipeDTO.setProtein(recipeJObject.getInt("protein"));
            recipeDTO.setCarbs(recipeJObject.getInt("carbs"));
            recipeDTO.setFats(recipeJObject.getInt("fats"));
            recipeDTO.setCalories(recipeJObject.getInt("calories"));
            recipeDTO.setServings(recipeJObject.getInt("servings"));
            recipeDTO.setPrep_time(recipeJObject.getInt("prep_time"));
            recipeDTO.setCook_time(recipeJObject.getInt("cook_time"));
            recipeDTO.setIngredients(ingredients);
            recipeDTO.setInstructions(instructions);
        
        return recipeDTO;
    }


    public List<Ingredient> docToIngredients(List<Document> ingredientDocs) {

        List<Ingredient> ingredients = new ArrayList<>();
        for (Document i : ingredientDocs) {
            Ingredient ingredient = new Ingredient();
                ingredient.setIngredient(i.getString("ingredient"));
                ingredient.setAmount(i.getString("amount"));
                ingredient.setUnit(i.getString("unit"));
                ingredient.setNotes(i.getString("notes"));
            ingredients.add(ingredient);
        }
        return ingredients;
    }


    public List<Instruction> docToInstructions(List<Document> instructionDocs) {
        
        List<Instruction> instructions = new ArrayList<>();
        for (Document i : instructionDocs) {
            Instruction instruction = new Instruction();
                instruction.setStep(i.getString("instruction"));
            instructions.add(instruction);
        }
        return instructions;
    }


    public NutritionRec toNutritionRec(String response, int height, int weight, int age, String sex, String activityLevel, String goal) {

        JsonObject responseObject = Json.createReader(new StringReader(response)).readObject();

        // extract nutrition information
        JsonObject nutritionObject = responseObject.getJsonObject("nutrition");

        int carbs = nutritionObject.getJsonObject("carbs").getInt("value");
        String carbsRange = nutritionObject.getJsonObject("carbs").getString("range");

        int protein = nutritionObject.getJsonObject("protein").getInt("value");
        String protieinRange = nutritionObject.getJsonObject("protein").getString("range");

        int fat = nutritionObject.getJsonObject("fat").getInt("value");
        String fatRange = nutritionObject.getJsonObject("fat").getString("range");

        String saturatedFat = nutritionObject.getJsonObject("saturatedFat").getString("value");

        String sugar = nutritionObject.getJsonObject("sugar").getString("value");

        int calories = nutritionObject.getJsonObject("foodEnergy").getInt("value");

        JsonObject calculations = responseObject.getJsonObject("calculations");

        int tdee = calculations.getInt("tdee");
        int bmr = calculations.getInt("bmr");

        NutritionRec nr = new NutritionRec(carbs, carbsRange, protein, protieinRange, fat, fatRange, saturatedFat, sugar, calories, tdee, bmr, weight, height, age, sex, activityLevel, goal);

        // System.out.println(nr);
        
        return nr;
    }


    public String cleanGeminiRecipe(String responseText) {
        
        // ```json
        // {
        // "title": "Ginger Garlic Chicken and Broccoli Rice Bowl",
        // "description": "A quick and easy one-bowl meal with flavorful chicken and tender broccoli.",
        // "ingredients": [
        //     "1 lb chicken breast, cubed",
        //     "1 cup rice",
        //     "1 head broccoli, cut into florets",
        //     "1/4 cup soy sauce",
        //     "4 cloves garlic, minced",
        //     "1 inch ginger, grated",
        //     "2 tbsp olive oil",
        //     "Salt to taste",
        //     "Pepper to taste"
        // ],
        // "prepTime": "15 minutes",
        // "cookTime": "25 minutes",
        // "steps": [
        //     "Cook rice according to package directions.",
        //     "While rice cooks, heat olive oil in a large skillet over medium-high heat. Add chicken and cook until browned and cooked through. Season with salt and pepper.",
        //     "Add garlic and ginger to the skillet and cook for 1 minute, until fragrant.",
        //     "Pour soy sauce over the chicken and garlic-ginger mixture. Stir to coat.",
        //     "Add broccoli florets to the skillet and cook until tender-crisp, about 5-7 minutes.",
        //     "Serve chicken and broccoli mixture over cooked rice."
        // ]
        // }
        // ```

        String recipeJsonText = "";
        if (responseText.startsWith("```json")) {
            String removeLeading = responseText.substring(responseText.indexOf("```json") + 7);
            int closingBackticksPos = removeLeading.lastIndexOf("```");
            recipeJsonText = removeLeading.substring(0, closingBackticksPos).trim();
            System.out.println("Cleaned json response");
        } else {
            throw new RuntimeException("Unable to clean JSON response");
        }

        return recipeJsonText;
    
    }

}
