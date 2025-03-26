package sg.edu.nus.iss.springboot.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.json.Json;
import jakarta.json.JsonObject;

import sg.edu.nus.iss.springboot.services.GeminiService;

@Controller
@RequestMapping("/api/private/gemini")
public class PrivateGeminiController {
    
    @Autowired
    GeminiService geminiService;

    @PostMapping("/generateRecipe")
    public ResponseEntity<String> generateRecipe(@RequestBody List<String> ingredients) {
        
        System.out.println("Recieved request to generate gemini recipe");

        try {
            String recipe = geminiService.generateRecipe(ingredients);
            System.out.println("Gemini recipe generated");
            // System.out.println(recipe);
            return ResponseEntity.ok().body(recipe);

        } catch (Exception e) {
            System.out.println("Error generating recipe: " + e.getMessage());
            e.printStackTrace();
            JsonObject error = Json.createObjectBuilder()
                .add("error", e.getMessage())
                .build();

            return ResponseEntity.status(500).body(error.toString());
        }
    }

}
