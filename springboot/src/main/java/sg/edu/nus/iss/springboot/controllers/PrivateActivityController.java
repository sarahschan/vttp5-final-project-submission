package sg.edu.nus.iss.springboot.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import sg.edu.nus.iss.springboot.models.Activity;
import sg.edu.nus.iss.springboot.services.ActivityService;

@RestController
@RequestMapping("/api/private/activity")
public class PrivateActivityController {
    
    @Autowired
    ActivityService activityService;


    @PostMapping()
    public ResponseEntity<?> getActivity(@RequestBody Map<String, Object> payload) {
        
        System.out.println("Request to get activities");
        // Request to get activities from users: {followingList=[23, 24, 25], page=0, size=10}

        try {

            List<Integer> followingList = (List<Integer>) payload.get("followingList");

            int page = Integer.parseInt(payload.get("page").toString());
            int size = Integer.parseInt(payload.get("size").toString());
        
            List<Activity> activities = activityService.getActivities(followingList, page, size);

            System.out.println("Got activities, page " + page + ", size " + size);
            
            return ResponseEntity.ok().body(activities);


        } catch (Exception e) {
            System.out.println("Error getting activies: " + e.getMessage());
            e.printStackTrace();

            JsonObject error = Json.createObjectBuilder()
                .add("error", e.getMessage())
                .build();

            return ResponseEntity.status(500).body(error.toString());
        }

    }
    
}
