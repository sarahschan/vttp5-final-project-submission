package sg.edu.nus.iss.springboot.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import sg.edu.nus.iss.springboot.models.User;
import sg.edu.nus.iss.springboot.services.EmailService;
import sg.edu.nus.iss.springboot.services.UserService;
import jakarta.mail.MessagingException;

@Controller
@RequestMapping("/api/public/users")
@CrossOrigin(origins = "*")
public class PublicUsersController {
    
    @Autowired
    UserService userService;

    @Autowired
    EmailService emailService;


    @PostMapping("/register")
    public ResponseEntity<String> registerUser(
        @RequestParam() String name,
        @RequestParam() String username,
        @RequestParam() String email,
        @RequestParam() String password,
        @RequestParam(required = false) MultipartFile profile_picture) throws MessagingException {

        User user = new User();
            user.setName(name);
            user.setUsername(username);
            user.setEmail(email);
            user.setPassword(password);
        
        boolean isRegistered = userService.registerNewUser(user);
        
        if (isRegistered) {
            
            System.out.println("User registered, attempting to send email");
            
            try {
                
                emailService.sendWelcomeEmail(email, name);
                System.out.println("Welcome email sent");

            } catch (MessagingException e) {

                System.out.println("User registered but welcome email unable to send");
            }

            JsonObject response = Json.createObjectBuilder()
                .add("success", "User registration successful")
                .build();
            return ResponseEntity.ok(response.toString());
            
        } else {

            System.out.println("User registration failed");
            
            JsonObject error = Json.createObjectBuilder()
                .add("error", "User registration failed")
                .build();

            return ResponseEntity.status(500).body(error.toString());
        }
    
    }


    @GetMapping("/{username}")
    public ResponseEntity<?> getUserPublicInfo(@PathVariable String username) {
        
        try {

            System.out.println("Request recieved to get user " + username);
            Optional<User> opt = userService.getUserByUsername(username);

            if (opt.isPresent()){
                User user = opt.get();
                System.out.println(String.format("User %s found", user.getUsername()));
                return ResponseEntity.ok().body(user);

            } else {
                System.out.println("User not found");
                return ResponseEntity.notFound().build();
            }

        } catch (Exception e) {
            System.out.println("An unknow error occurred: " + e.getMessage());
            e.printStackTrace();
            JsonObject error = Json.createObjectBuilder()
                .add("error", e.getMessage())
                .build();
            return ResponseEntity.status(500).body(error.toString());

        }
    }
}

