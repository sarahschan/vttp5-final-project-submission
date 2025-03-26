package sg.edu.nus.iss.springboot.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import sg.edu.nus.iss.springboot.services.LikeService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/private/likes")
public class PrivateLikeController {
    
    @Autowired
    LikeService likeService;

    @GetMapping("/{userId}")
    public ResponseEntity<?> getAllLikesByUser(@PathVariable int userId) {

        System.out.println("Request to get all likes by user " + userId);

        try {
            
            List<Integer> likedRecipeIds = likeService.getLikedRecipesByUserId(userId);

            System.out.println("Got likes");

            return ResponseEntity.ok().body(likedRecipeIds);

        } catch (Exception e) {
            
            System.out.println("Error fetching likes: " + e.getMessage());
            e.printStackTrace();
            JsonObject error = Json.createObjectBuilder()
                .add("error", e.getMessage())
                .build();

            return ResponseEntity.status(500).body(error.toString());

        }
    }


    @PostMapping("/like")
    public ResponseEntity<String> likeRecipe(@RequestBody String payload) {
        
        System.out.println("Like request recieved");

        try {
            likeService.likeRecipe(payload);
            System.out.println("Inserted like");
            return ResponseEntity.ok().body(null);

        } catch (Exception e) {
            System.out.println("Error inserting like: " + e.getMessage());
            e.printStackTrace();
            JsonObject error = Json.createObjectBuilder()   
                .add("error", e.getMessage())
                .build();
            return ResponseEntity.status(500).body(error.toString());
        }

    }


    @DeleteMapping("/unlike/{userId}/{recipeId}")
    public ResponseEntity<String> unlikeRecipe(@PathVariable int userId, @PathVariable int recipeId) {

        System.out.println("Unlike request recieved");

        try {
            System.out.println("Unlike successfull");
            likeService.unlikeRecipe(userId, recipeId);
            return ResponseEntity.ok().body(null);

        } catch (Exception e) {
            System.out.println("Error deleting like: " + e.getMessage());
            JsonObject error = Json.createObjectBuilder()   
                .add("error", e.getMessage())
                .build();
            return ResponseEntity.status(500).body(error.toString());
        }
    }
    
}
