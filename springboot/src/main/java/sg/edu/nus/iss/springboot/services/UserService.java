package sg.edu.nus.iss.springboot.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import sg.edu.nus.iss.springboot.models.LoginRequest;
import sg.edu.nus.iss.springboot.models.User;
import sg.edu.nus.iss.springboot.repositories.UserRepository;

@Service
public class UserService {
    
    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    S3Service s3Service;


    // check if username is available
    public boolean isUsernameAvailable(String username) {
        
        Optional<User> user = userRepository.getUserByUsername(username);
        
        if (user.isEmpty()) {
            return true;
        }

        return false;
    }


    // check if email is available
    public boolean isEmailAvailable(String email) {
        
        Optional<User> user = userRepository.getUserByEmail(email);
        
        if (user.isEmpty()) {
            return true;
        }

        return false;
    }


    // create new user
    public boolean registerNewUser(User userData) {

        User user = new User();
            user.setName(userData.getName());
            user.setUsername(userData.getUsername());
            user.setEmail(userData.getEmail());
            user.setPassword(passwordEncoder.encode(userData.getPassword()));
        
        System.out.println("Attempting to register new user");

        try {
            userRepository.saveUserDetails(user);
            System.out.println("User saved successfully");
            return true;

        } catch (Exception e) {
            System.out.println("User save failed");
            e.printStackTrace();
            return false;
        }
    }


    // authenticate user
    public Optional<User> authenticateUser(LoginRequest loginRequest) {
        
        // System.out.println("In User service - authenticateUser");
        try {
            
            Optional<User> user = userRepository.getUserByUsername(loginRequest.getUsername());
        
            if (user.isEmpty()) {
                // System.out.println("User not found");
                return Optional.empty();

            } else {

                // System.out.println("User found: " + user.get().toString());
                
                if (passwordEncoder.matches(loginRequest.getPassword(), user.get().getPassword())) {
                    // System.out.println("password match");
                    return Optional.of(user.get());

                } else {
                    // System.out.println("password mismatch");
                    return Optional.empty();
                }

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


    // get user by username
    public Optional<User> getUserByUsername(String username) {
        Optional<User> foundUser = userRepository.getUserByUsername(username);
        return foundUser;
    }


    // change profile picture
    public String updateProfilePicture(String username, MultipartFile imageFile) throws Exception {
        
        String imageUrl = s3Service.uploadPfpToDigitalOcean(imageFile, username);
        userRepository.updateProfilePicture(username, imageUrl);
        System.out.println("Success saving to mySQL");
        
        return imageUrl;
    }


    // change bio
    public void updateUserBio(String username, String editedBio) {
        userRepository.updateBio(username, editedBio);
    }
}
