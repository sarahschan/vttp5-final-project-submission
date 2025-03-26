package sg.edu.nus.iss.springboot.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import sg.edu.nus.iss.springboot.models.Recipe;
import sg.edu.nus.iss.springboot.models.RecipeDTO;
import sg.edu.nus.iss.springboot.models.RecipeFilterCriteria;
import sg.edu.nus.iss.springboot.models.RecipeOverview;
import sg.edu.nus.iss.springboot.services.RecipeService;
import sg.edu.nus.iss.springboot.utilities.JsonParser;

@RestController
@RequestMapping("/api/private/recipes")
public class PrivateRecipesController {
    
    @Autowired
    JsonParser jsonParser;

    @Autowired
    RecipeService recipeService;


    @PostMapping(value = "/new", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> submitNewRecipe(@RequestPart("userId") String userIdString, @RequestPart("recipe") String recipeJsonString, @RequestPart(value = "imageFile", required = false) MultipartFile imageFile) {

        System.out.println("Request to create new recipe recieved");

        try {
            
            RecipeDTO recipeDTO = jsonParser.toRecipeDTO(recipeJsonString);

            try {

                int userId = Integer.parseInt(userIdString);
                int recipeId = recipeService.insertNewRecipe(userId, recipeDTO, imageFile);

                System.out.println("Recipe created");

                JsonObject success = Json.createObjectBuilder()
                    .add("recipeId", recipeId)
                    .build();
                
                return ResponseEntity.ok().body(success.toString());

            } catch (Exception e) {
                
                System.out.println("Error creating recipe: " + e.getMessage());
                e.printStackTrace();

                JsonObject serverError = Json.createObjectBuilder()
                .add("error", e.getMessage())
                .build();

                return ResponseEntity.status(500).body(serverError.toString());
            }
            

        
        } catch (Exception e) {
            
            System.out.println("Error creating recipe: " + e.getMessage());
            e.printStackTrace();

            JsonObject badRequest = Json.createObjectBuilder()
                .add("error", e.getMessage())
                .build();

            return ResponseEntity.badRequest().body(badRequest.toString());
        }
    }


    @GetMapping("/{recipeId}")
    public ResponseEntity<Object> getRecipeById(@PathVariable int recipeId) {
        
        System.out.println("Request recieved to retrieve recipe: " + recipeId);
        
        try {
            
            Optional<Recipe> opt = recipeService.getRecipeById(recipeId);

            if (opt.isPresent()) {

                Recipe foundRecipe = opt.get();
                System.out.println("Recipe found");

                return ResponseEntity.ok().body(foundRecipe);

            } else {

                System.out.println("Recipe not found - opt is empty");
                
                JsonObject error = Json.createObjectBuilder()
                    .add("error", "Recipe details could not be found")
                    .build();
                
                return ResponseEntity.status(500).body(error.toString());

            }

        } catch (Exception e) {

            System.out.println("Database error: " + e.getMessage());
            JsonObject error = Json.createObjectBuilder()
                .add("error", e.getMessage())
                .build();
            
            return ResponseEntity.status(500).body(error.toString());

        }
        
    }


    @GetMapping("/overview")
    public ResponseEntity<?> getRecipeOverviews(@RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "12") int size,
                                                @RequestParam(defaultValue = "timestamp") String sortBy,
                                                @RequestParam(defaultValue = "desc") String direction,
                                                @RequestParam(defaultValue = "0") int minCalories,
                                                @RequestParam(defaultValue = "10000") int maxCalories,
                                                @RequestParam(defaultValue = "0") int minProtein,
                                                @RequestParam(defaultValue = "1000") int maxProtein,
                                                @RequestParam(defaultValue = "0") int minCarbs,
                                                @RequestParam(defaultValue = "1000") int maxCarbs,
                                                @RequestParam(defaultValue = "0") int minFats,
                                                @RequestParam(defaultValue = "1000") int maxFats) {
        
        System.out.println("Request to get recipe overviews recieved");

        try {

            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, sortBy));

            // Create a filter criteria object to pass to the service
            RecipeFilterCriteria filterCriteria = new RecipeFilterCriteria();
                filterCriteria.setMinCalories(minCalories);
                filterCriteria.setMaxCalories(maxCalories);
                filterCriteria.setMinProtein(minProtein);
                filterCriteria.setMaxProtein(maxProtein);
                filterCriteria.setMinCarbs(minCarbs);
                filterCriteria.setMaxCarbs(maxCarbs);
                filterCriteria.setMinFats(minFats);
                filterCriteria.setMaxFats(maxFats);

            Page<RecipeOverview> recipes = recipeService.getRecipeOverviews(pageable, filterCriteria);
        
            System.out.println("Got recipe overviews");

            return ResponseEntity.ok().body(recipes);

        } catch (Exception e) {

            System.out.println("Error getting recipe overviews: " + e.getMessage());
            e.printStackTrace();

            JsonObject error = Json.createObjectBuilder()
                    .add("error", e.getMessage())
                    .build();

            return ResponseEntity.status(500).body(error.toString());
        }
    }


    @GetMapping("/postedBy/{userId}")
    public ResponseEntity<?> getRecipesByUserId(@PathVariable int userId,
                                                @RequestParam int page,
                                                @RequestParam int size){

        System.out.println("Recieved request to get recipes by user " + userId);
        
        try {

            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "updated_at"));
            Page<RecipeOverview> recipes = recipeService.getRecipesByUserId(pageable, userId);
            System.out.println(String.format("Found %d posted recipes, sending to angular", recipes.getNumberOfElements()));

            return ResponseEntity.ok().body(recipes);

        } catch (Exception e) {

            System.out.println("Error getting recipes by user " + userId + ": " + e.getMessage());
            e.printStackTrace();
            
            JsonObject error = Json.createObjectBuilder()
                .add("error", e.getMessage())
                .build();
            return ResponseEntity.status(500).body(error.toString());

        }                                         
    }


    @GetMapping("/likedRecipes/{userId}")
    public ResponseEntity<?> getLikedRecipeOverviews(@PathVariable int userId,
                                                     @RequestParam int page,
                                                     @RequestParam int size) {

        System.out.println("Recieved request to get recipes liked by " + userId);

        try {

            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "timestamp"));
            Page<RecipeOverview> recipes = recipeService.getLikedRecipeOverviews(pageable, userId);
            System.out.println(String.format("Found %d liked recipes, sending to angular", recipes.getNumberOfElements()));

            return ResponseEntity.ok().body(recipes);

        } catch (Exception e) {

            System.out.println("Error retrieving liked recipes: " + e.getMessage());
            e.printStackTrace();
            JsonObject error = Json.createObjectBuilder()
                .add("error", e.getMessage())
                .build();
            return ResponseEntity.status(500).body(error.toString());

        }
    }
}
