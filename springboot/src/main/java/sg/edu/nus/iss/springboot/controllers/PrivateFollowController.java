package sg.edu.nus.iss.springboot.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import sg.edu.nus.iss.springboot.models.FollowProfile;
import sg.edu.nus.iss.springboot.services.FollowService;

@RestController
@RequestMapping("/api/private/follow")
public class PrivateFollowController {
    
    @Autowired
    FollowService followService;

    
    @PostMapping("/follow")
    public ResponseEntity<?> followUser(@RequestBody String payload) {
        
        System.out.println("Follow request recieved");
        // System.out.println(payload);

        try {
            
            FollowProfile followedProfile = followService.followUser(payload);
            System.out.println("Follow successful");

            return ResponseEntity.ok().body(followedProfile);

        } catch (Exception e){

            System.out.println("Follow unsuccessful: " + e.getMessage());
            e.printStackTrace();

            JsonObject error = Json.createObjectBuilder()
                .add("error", e.getMessage())
                .build();

            return ResponseEntity.status(500).body(error.toString());
        }

    }


    @DeleteMapping("/unfollow/{myUserId}/{userToUnfollow}")
    public ResponseEntity<String> unfollowUser(@PathVariable int myUserId, @PathVariable int userToUnfollow) {
        
        System.out.println("Unfollow request recieved");

        try {

            followService.unfollowUser(myUserId, userToUnfollow);
            System.out.println("Unfollow successful");

            return ResponseEntity.ok().body(null);

        } catch (Exception e) {
            
            System.out.println("Unfollow unsuccessful: " + e.getMessage());
            e.printStackTrace();

            JsonObject error = Json.createObjectBuilder()
                .add("error", e.getMessage())
                .build();

            return ResponseEntity.status(500).body(error.toString());

        }
    }


    @GetMapping("/{myUserId}")
    public ResponseEntity<?> getAllFollowedUsers(@PathVariable int myUserId) {

        System.out.println("Request recieved to get all users followed by: UserId " + myUserId);

        try {

            List<FollowProfile> followedProfiles = followService.getAllFollowedUsers(myUserId);

            System.out.println("Got followed profiles");

            return ResponseEntity.ok().body(followedProfiles);

        } catch (Exception e) {

            System.out.println("Error retrieving followed profiles: " + e.getMessage());
            e.printStackTrace();

            JsonObject error = Json.createObjectBuilder()
                .add("error", e.getMessage())
                .build();

            return ResponseEntity.status(500).body(error.toString());

        }
    }
}
