package sg.edu.nus.iss.springboot.services;

import java.io.StringReader;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import sg.edu.nus.iss.springboot.models.Comment;
import sg.edu.nus.iss.springboot.repositories.CommentRepository;

@Service
public class CommentService {

    @Autowired
    CommentRepository commentRepository;

    public void postNewComment(String payload) {
        
        JsonObject jObject = Json.createReader(new StringReader(payload)).readObject();
        int userId = Integer.parseInt(jObject.getString("userId"));
        int recipeId = Integer.parseInt(jObject.getString("recipeId"));
        String comment = jObject.getString("comment");

        try {
            commentRepository.insertNewComment(userId, recipeId, comment);

        } catch (Exception e) {
            System.out.println("Error inserting comment into database");
            throw new RuntimeException(e);
        }
    }


    public List<Comment> getComments(int recipeId) {
        return commentRepository.getComments(recipeId);
    }


}
