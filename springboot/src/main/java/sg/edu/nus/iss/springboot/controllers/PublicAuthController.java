package sg.edu.nus.iss.springboot.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import sg.edu.nus.iss.springboot.models.LoginRequest;
import sg.edu.nus.iss.springboot.models.User;
import sg.edu.nus.iss.springboot.services.UserService;
import sg.edu.nus.iss.springboot.utilities.JwtUtil;

@RestController
@RequestMapping("/api/public/auth")
@CrossOrigin(origins = "*")
public class PublicAuthController {

    @Autowired
    UserService userService;

    @Autowired
    JwtUtil jwtUtil;
    

    @GetMapping("/check/username/{username}")
    public ResponseEntity<Boolean> checkUsernameExists(@PathVariable String username) {
        boolean isAvailable = userService.isUsernameAvailable(username);
        return ResponseEntity.ok(isAvailable);
    }


    @GetMapping("/check/email/{email}")
    public ResponseEntity<Boolean> checkEmailExists(@PathVariable String email) {
        boolean isAvailable = userService.isEmailAvailable(email);
        return ResponseEntity.ok(isAvailable);
    }


    @PostMapping("/login")
    public ResponseEntity<String> authenticate(@RequestBody LoginRequest loginRequest) {

        System.out.println("Login request received");

        try {
            
            Optional<User> authenticatedUser = userService.authenticateUser(loginRequest);

            if (authenticatedUser.isPresent()) {

                System.out.println("Password match, generating token");

                String token = jwtUtil.generateToken(authenticatedUser.get().getUsername());
                // System.out.println("Token generated: " + token);

                System.out.println("Token generated, sending user authentication details to angular");

                JsonObject response = Json.createObjectBuilder()
                    .add("authenticated", token)
                    .add("username", authenticatedUser.get().getUsername())
                    .add("userId", authenticatedUser.get().getUserId())
                    .build();
                return ResponseEntity.ok(response.toString());
            }

            JsonObject error = Json.createObjectBuilder()
                .add("error", "Invalid username or password")
                .build();

            return ResponseEntity.status(401).body(error.toString());

        } catch (Exception e) {
            
            System.out.println("Error logging in: " + e.getMessage());
            JsonObject error = Json.createObjectBuilder()
                .add("error", e.getMessage())
                .build();

            return ResponseEntity.status(500).body(error.toString());
        }
    }
}
