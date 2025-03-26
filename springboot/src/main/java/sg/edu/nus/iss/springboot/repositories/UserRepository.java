package sg.edu.nus.iss.springboot.repositories;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import sg.edu.nus.iss.springboot.models.User;

@Repository
public class UserRepository {
    
    @Autowired
    private JdbcTemplate template;


    public Optional<User> getUserByUsername(String username) {
        
        System.out.println("Looking in database for " + username);

        SqlRowSet rs = template.queryForRowSet(Queries.SQL_GET_USER_BY_USERNAME, username);

        if (rs.next()) {
            User foundUser = User.toUser(rs);
            return Optional.of(foundUser);

        } else {
            return Optional.empty();
        }
    }


    public Optional<User> getUserByEmail(String email) {
        
        try {
            
            SqlRowSet rs = template.queryForRowSet(Queries.SQL_GET_USER_BY_EMAIL, email);

            if (rs.next()) {
                User foundUser = User.toUser(rs);
                return Optional.of(foundUser);

            } else {
                return Optional.empty();
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public void saveUserDetails(User user) {
        template.update(Queries.SQL_INSERT_USER, user.getName(), user.getUsername(), user.getEmail(), user.getPassword(), user.getProfile_picture(), user.getShort_bio());
    }


    public void updateProfilePicture(String username, String imgUrl){
        template.update(Queries.SQL_UPDATE_PROFILE_PICTURE, imgUrl, username);
    }


    public void updateBio(String username, String editedBio) {
        template.update(Queries.SQL_UPDATE_BIO, editedBio, username);
    }
}
