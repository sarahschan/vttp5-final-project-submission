package sg.edu.nus.iss.springboot.services;

import java.io.StringReader;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import sg.edu.nus.iss.springboot.models.FollowProfile;
import sg.edu.nus.iss.springboot.repositories.FollowRepository;

@Service
public class FollowService {
    
    @Autowired
    FollowRepository followRepository;
    
    public FollowProfile followUser(String payload) {
        
        // {"myUserId":20,"userToFollow":25}
        JsonObject jObject = Json.createReader(new StringReader(payload)).readObject();
        int myUserId = jObject.getInt("myUserId");
        int userToFollow = jObject.getInt("userToFollow");

        followRepository.followUser(myUserId, userToFollow);

        FollowProfile followedProfile = followRepository.getFollowProfileForUser(userToFollow);

        return followedProfile;
    }


    public void unfollowUser(int myUserId, int userToUnfollow) {

        followRepository.unfollowUser(myUserId, userToUnfollow);
        
    }


    public List<FollowProfile> getAllFollowedUsers(int myUserId) {
        return followRepository.getAllFollowedUsers(myUserId);
    }
}
