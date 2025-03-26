package sg.edu.nus.iss.springboot.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import sg.edu.nus.iss.springboot.models.NutritionRec;
import sg.edu.nus.iss.springboot.services.NutritionRecService;

@RestController
@RequestMapping("/api/private/nutrition")
public class PrivateNutritionController {
    
    @Autowired
    NutritionRecService nutritionRecService;

    
    @GetMapping()
    public ResponseEntity<?> getNutritionRec(@RequestParam int height,
                                             @RequestParam int weight,
                                             @RequestParam int age,
                                             @RequestParam String sex,
                                             @RequestParam String activityLevel,
                                             @RequestParam String goal) {

        System.out.println("Request to get nutrition recommendation recieved");

        try {

            NutritionRec response = nutritionRecService.getNutrition(height, weight, age, sex, activityLevel, goal);

            System.out.println("Got nutrition recommendation");

            return ResponseEntity.ok().body(response);

        } catch (Exception e) {
            System.out.println("Error getting nutrition recommendation");
            e.printStackTrace();

            JsonObject error = Json.createObjectBuilder()
                .add("error", e.getMessage())
                .build();

            return ResponseEntity.status(500).body(error.toString());
        }
    }
}
