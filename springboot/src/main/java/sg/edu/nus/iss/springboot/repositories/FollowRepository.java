package sg.edu.nus.iss.springboot.repositories;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import sg.edu.nus.iss.springboot.models.FollowProfile;

@Repository
public class FollowRepository {
    
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Transactional
    public void followUser(int myUserId, int userToFollow) {
        
        // insert into follows table
        jdbcTemplate.update(Queries.SQL_FOLLOW_USER, myUserId, userToFollow);
        
        // insert into activity table
        jdbcTemplate.update(Queries.SQL_INSERT_FOLLOW_ACTIVITY, myUserId, userToFollow);

    }


    @Transactional
    public void unfollowUser(int myUserId, int userToUnfollow) {

        // remove from follows table
        jdbcTemplate.update(Queries.SQL_UNFOLLOW_USER, myUserId, userToUnfollow);

        // remove from activities table
        jdbcTemplate.update(Queries.SQL_REMOVE_FOLLOW_ACTIVITY, myUserId, userToUnfollow);
    }


    public List<FollowProfile> getAllFollowedUsers(int myUserId) {
        List<FollowProfile> followProfiles = jdbcTemplate.query(Queries.SQL_GET_ALL_FOLLOWED_USERS, BeanPropertyRowMapper.newInstance(FollowProfile.class), myUserId);
        return followProfiles;
    }


    public FollowProfile getFollowProfileForUser(int userId) {
        FollowProfile followProfile = jdbcTemplate.queryForObject(Queries.SQL_GET_FOLLOW_PROFILE, BeanPropertyRowMapper.newInstance(FollowProfile.class), userId);
        return followProfile;
    }

}
