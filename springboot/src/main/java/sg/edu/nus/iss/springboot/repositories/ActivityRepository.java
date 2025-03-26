package sg.edu.nus.iss.springboot.repositories;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import sg.edu.nus.iss.springboot.models.Activity;

@Repository
public class ActivityRepository {
    
    @Autowired
    JdbcTemplate jdbcTemplate;

    public List<Activity> getActivities(List<Integer> followingUserIds, int page, int size) {
        
        if (followingUserIds == null || followingUserIds.isEmpty()) {
            return Collections.emptyList();
        }

        int offset = page * size;

        String placeholders = String.join(",", Collections.nCopies(followingUserIds.size(), "?"));
        String finalSQL = String.format(Queries.SQL_GET_ACTIVITY, placeholders);

        Object[] params = new Object[followingUserIds.size() + 2];
        
        for (int i = 0; i < followingUserIds.size(); i++) {
            params[i] = followingUserIds.get(i);
        }
        
        params[followingUserIds.size()] = size;
        params[followingUserIds.size() + 1] = offset;
        
        List<Activity> activities = jdbcTemplate.query(finalSQL, BeanPropertyRowMapper.newInstance(Activity.class), params);

        return activities;
    }


}
