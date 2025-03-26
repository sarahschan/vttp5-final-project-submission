package sg.edu.nus.iss.springboot.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import sg.edu.nus.iss.springboot.models.Comment;

@Repository
public class CommentRepository {
    
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Transactional
    public void insertNewComment(int userId, int recipeId, String comment) {

         // Insert comment and get generated ID
        KeyHolder keyHolder = new GeneratedKeyHolder();

        PreparedStatementCreator psc = new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement(Queries.SQL_INSERT_NEW_COMMENT, new String[] {"id"});
                    ps.setInt(1, userId);
                    ps.setInt(2, recipeId);
                    ps.setString(3, comment);
                return ps;
            }
        };

        jdbcTemplate.update(psc, keyHolder);

        int commentId = keyHolder.getKey().intValue();

        // inesrt into activity table
        jdbcTemplate.update(Queries.SQL_INSERT_COMMENT_ACTIVITY, userId, recipeId, commentId);

    }


    public List<Comment> getComments(int recipeId) {
        
        SqlRowSet rs = jdbcTemplate.queryForRowSet(Queries.SQL_GET_COMMENTS_FOR_RECIPE, recipeId);

        List<Comment> comments = new ArrayList<>();

        while (rs.next()) {
            Comment c = Comment.toComment(rs);
            comments.add(c);
        }

        return comments;
        
    }
}
