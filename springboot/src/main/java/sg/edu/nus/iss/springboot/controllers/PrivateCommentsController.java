package sg.edu.nus.iss.springboot.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import sg.edu.nus.iss.springboot.models.Comment;
import sg.edu.nus.iss.springboot.services.CommentService;
import sg.edu.nus.iss.springboot.utilities.JsonParser;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/private/comments")
public class PrivateCommentsController {

    @Autowired
    CommentService commentService;

    @Autowired
    JsonParser jsonParser;

    @PostMapping("/new")
    public ResponseEntity<String> postNewComment(@RequestBody String payload) {

        System.out.println("Recieved request to post comment");

        try {
            
            commentService.postNewComment(payload);
            System.out.println("Comment saved to mySQL");

            return ResponseEntity.ok().body(null);
        
        } catch (Exception e) {

            System.out.println("Error posting new comment: " + e.getMessage());

            JsonObject error = Json.createObjectBuilder()
                .add("error", e.getMessage())
                .build();

            return ResponseEntity.status(500).body(error.toString());
        }
    
    }


    @GetMapping("/{recipeId}")
    public ResponseEntity<?> getComments(@PathVariable int recipeId) {
        
        System.out.println("Received request to get comments for recipe: " + recipeId);

        try {

            List<Comment> comments = commentService.getComments(recipeId);

            System.out.println("Comments retrieved, sending to angular");

            return ResponseEntity.ok().body(comments);

        } catch (Exception e) {
            
            System.out.println("Error fetching comments for recipe " + recipeId);
            e.printStackTrace();

            JsonObject error = Json.createObjectBuilder()
                .add("error", e.getMessage())
                .build();

            return ResponseEntity.status(500).body(error.toString());
        }
    }
    
}
