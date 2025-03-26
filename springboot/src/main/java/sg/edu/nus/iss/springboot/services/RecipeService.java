package sg.edu.nus.iss.springboot.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import sg.edu.nus.iss.springboot.models.Recipe;
import sg.edu.nus.iss.springboot.models.RecipeDTO;
import sg.edu.nus.iss.springboot.models.RecipeFilterCriteria;
import sg.edu.nus.iss.springboot.models.RecipeOverview;
import sg.edu.nus.iss.springboot.repositories.RecipeRepository;

@Service
public class RecipeService {
    
    @Autowired
    RecipeRepository recipeRepository;

    @Autowired
    S3Service s3Service;

    public int insertNewRecipe(Integer userId, RecipeDTO recipeDTO, MultipartFile imageFile) {
        Integer recipeId = null;
        boolean mongoInsertCompleted = false;
        
        try {
            // Step 1: Insert into MySQL and get the generated recipeId
            recipeId = recipeRepository.insertNewRecipeToMySQL(userId, recipeDTO);
            System.out.println("Generated recipeId: " + recipeId);
            
            // Step 2: Write ingredients and instructions to MongoDB
            recipeRepository.insertIngredientsAndInstructionsToMongo(recipeId, recipeDTO);
            mongoInsertCompleted = true;
            
            // Step 3: Handle image upload if provided
            if (imageFile != null && !imageFile.isEmpty()) {
                try {
                    String imageUrl = s3Service.uploadRecipeImageToDigitalOcean(imageFile, recipeId);
                    recipeRepository.updateRecipeImageUrl(imageUrl, recipeId);
                } catch (Exception e) {
                    throw new RuntimeException("Failed to upload recipe image to digital ocean: " + e.getMessage(), e);
                }
            }

            // Step 4: Update the activities table
            recipeRepository.insertPostIntoActivities(userId, recipeId);

            return recipeId;
            
            
        } catch (Exception e) {
            // If something failed, rollback and throw error
            compensateForFailure(recipeId, mongoInsertCompleted);
            throw new RuntimeException("Failed to insert recipe completely: " + e.getMessage(), e);
        }
    }

    private void compensateForFailure(Integer recipeId, boolean mongoInsertCompleted) {
        try {
            // if mongo insertion was successfull, rollback
            if (mongoInsertCompleted && recipeId != null) {
                recipeRepository.deleteRecipeFromMongo(recipeId);
            }
            
            // if mySQL insertion was successfull, rollback
            if (recipeId != null) {
                recipeRepository.deleteRecipeFromMySQL(recipeId);
            }

        } catch (Exception e) {
            // Log cleanup failures
            System.err.println("Failed to clean up after recipe insert failure: " + e.getMessage());
        }
    }


    public Optional<Recipe> getRecipeById(int recipeId) {
        
        Optional<Recipe> opt = recipeRepository.getRecipeById(recipeId);

        return opt;
    }


    public Page<RecipeOverview> getRecipeOverviews(Pageable pageable, RecipeFilterCriteria recipeFilterCriteria) {
        return recipeRepository.getRecipeOverviews(pageable, recipeFilterCriteria);
    }


    public Page<RecipeOverview> getRecipesByUserId(Pageable pageable, int userId) {
        return recipeRepository.getRecipesByUserId(pageable, userId);
    }


    public Page<RecipeOverview> getLikedRecipeOverviews(Pageable pageable, int userId) {
        return recipeRepository.getLikedRecipeOverviews(pageable, userId);
    }
}
