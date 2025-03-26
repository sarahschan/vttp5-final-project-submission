package sg.edu.nus.iss.springboot.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sg.edu.nus.iss.springboot.models.Activity;
import sg.edu.nus.iss.springboot.repositories.ActivityRepository;

@Service
public class ActivityService {
    
    @Autowired
    ActivityRepository activityRepository;

    public List<Activity> getActivities(List<Integer> followingUserIds, int page, int size) {
        return activityRepository.getActivities(followingUserIds, page, size);
    }
}
