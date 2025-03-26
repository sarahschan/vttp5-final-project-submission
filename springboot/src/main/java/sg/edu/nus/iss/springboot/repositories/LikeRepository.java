package sg.edu.nus.iss.springboot.repositories;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class LikeRepository {
    
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Transactional
    public void likeRecipe(int userId, int recipeId) {

        // insert into likes table
        jdbcTemplate.update(Queries.SQL_LIKE_RECIPE, userId, recipeId);

        // insert into activity table
        jdbcTemplate.update(Queries.SQL_INSERT_LIKE_ACTIVITY, userId, recipeId);
        
    }


    @Transactional
    public void unlikeRecipe(int userId, int recipeId){
        
        // update the likes table
        jdbcTemplate.update(Queries.SQL_UNLIKE_RECIPE, userId, recipeId);

        // remove from the activities table
        jdbcTemplate.update(Queries.SQL_REMOVE_LIKE_ACTIVITY, userId, recipeId);
    }


    public List<Integer> getLikedRecipesByUserId(int userId){
        
        SqlRowSet rs = jdbcTemplate.queryForRowSet(Queries.SQL_GET_LIKES_BY_USERID, userId);
        
        List<Integer> likedRecipeIds = new ArrayList<>();

        while (rs.next()) {
            int recipeId = Integer.parseInt(rs.getString("recipe_id"));
            likedRecipeIds.add(recipeId);
        }

        return likedRecipeIds;
        
    }
}
