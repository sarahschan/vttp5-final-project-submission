package sg.edu.nus.iss.springboot.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import sg.edu.nus.iss.springboot.models.BioUpdateRequest;
import sg.edu.nus.iss.springboot.models.User;
import sg.edu.nus.iss.springboot.services.UserService;

@RestController
@RequestMapping("/api/private/user")
public class PrivateUsersController {
    
    @Autowired
    UserService userService;


    @GetMapping("/{username}")
    public ResponseEntity<User> getUserById(@PathVariable String username) {
        
        System.out.println("Request recieved to retrieve user details: " + username);

        Optional<User> opt = userService.getUserByUsername(username);
        
        if (opt.isPresent()) {
            User foundUser = opt.get();
            System.out.println("Found user " + foundUser.getUsername());
            return ResponseEntity.ok().body(foundUser);

        } else {
            System.out.println(String.format("User %s not found", username));
            return ResponseEntity.notFound().build();
        }

    }


    @PatchMapping("/updateProfilePicture/{username}")
    public ResponseEntity<String> updateProfilePicture(@PathVariable String username, @RequestPart("profile_picture") MultipartFile imageFile) {
        
        System.out.println("Request to change profile picture recieved from " + username);

        try {
            String imageUrl = userService.updateProfilePicture(username, imageFile);
            System.out.println("Profile picture changed");
            JsonObject response = Json.createObjectBuilder()
                .add("imageUrl", imageUrl)
                .build();
            return ResponseEntity.ok().body(response.toString());
        
        } catch (Exception e) {
            System.out.println("Error changing profile picture: " + e.getMessage());
            e.printStackTrace();
            JsonObject response = Json.createObjectBuilder()
                .add("error", e.getMessage())
                .build();
            return ResponseEntity.status(500).body(response.toString());
        }

    }


    @PatchMapping("/updateBio/{username}")
    public ResponseEntity<String> updateBio(@PathVariable String username, @RequestBody BioUpdateRequest bioUpdateRequest) {

        System.out.println("Request to update bio recieved from: " + username);

        try {
            userService.updateUserBio(username, bioUpdateRequest.getBio());
            System.out.println("Updated user bio");
            return ResponseEntity.ok().body(null);

        } catch (Exception e) {
            System.out.println("Error updating user bio: " + e.getMessage());
            e.printStackTrace();
            JsonObject response = Json.createObjectBuilder()
                .add("error", e.getMessage())
                .build();
            return ResponseEntity.status(500).body(response.toString());
        }
    }

}
