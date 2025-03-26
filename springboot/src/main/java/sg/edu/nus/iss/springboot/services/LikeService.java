package sg.edu.nus.iss.springboot.services;

import java.io.StringReader;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import sg.edu.nus.iss.springboot.repositories.ActivityRepository;
import sg.edu.nus.iss.springboot.repositories.LikeRepository;

@Service
public class LikeService {
    
    @Autowired
    LikeRepository likeRepository;

    @Autowired
    ActivityRepository activityRepository;

    public void likeRecipe(String jsonPayload) {
        
        // System.out.println(jsonPayload);
        // {"userId":"19","recipeId":11}
        
        JsonObject jObject = Json.createReader(new StringReader(jsonPayload)).readObject();
        int userId = Integer.parseInt(jObject.getString("userId"));
        int recipeId = jObject.getInt("recipeId");

        // System.out.println("UserId: " + userId + ", RecipeId: " + recipeId);

        likeRepository.likeRecipe(userId, recipeId);

    }


    public void unlikeRecipe(int userId, int recipeId) {
        likeRepository.unlikeRecipe(userId, recipeId);
    }


    public List<Integer> getLikedRecipesByUserId(int userId) {
        return likeRepository.getLikedRecipesByUserId(userId);
    }
}
