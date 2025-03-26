package sg.edu.nus.iss.springboot.repositories;


import sg.edu.nus.iss.springboot.models.Instruction;
import sg.edu.nus.iss.springboot.models.Recipe;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import sg.edu.nus.iss.springboot.models.Ingredient;
import sg.edu.nus.iss.springboot.models.RecipeDTO;
import sg.edu.nus.iss.springboot.models.RecipeFilterCriteria;
import sg.edu.nus.iss.springboot.models.RecipeOverview;
import sg.edu.nus.iss.springboot.utilities.JsonParser;

@Repository
public class RecipeRepository {
    
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    JsonParser jsonParser;

    public Integer insertNewRecipeToMySQL(Integer userId, RecipeDTO recipeDTO) {

        KeyHolder keyHolder = new GeneratedKeyHolder();

        PreparedStatementCreator psc = new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement(Queries.SQL_INSERT_NEW_RECIPE, new String[] {"recipe_id"});
                    ps.setInt(1, userId);
                    ps.setString(2, recipeDTO.getTitle());
                    ps.setString(3, recipeDTO.getDescription());
                    ps.setInt(4, recipeDTO.getProtein());
                    ps.setInt(5, recipeDTO.getCarbs());
                    ps.setInt(6, recipeDTO.getFats());
                    ps.setInt(7, recipeDTO.getCalories());
                    ps.setInt(8, recipeDTO.getServings());
                    ps.setInt(9, recipeDTO.getPrep_time());
                    ps.setInt(10, recipeDTO.getCook_time());
                return ps;
            }
        };

        try {
            jdbcTemplate.update(psc, keyHolder);
            
            Number key = keyHolder.getKey();
            if (key != null) {
                return key.intValue();
            } else {
                throw new RuntimeException("Failed to retrieve generated recipe_id");
            }

        } catch (Exception e) {
            throw new RuntimeException("Error writing recipe to database", e);

        }

    }


    public void insertIngredientsAndInstructionsToMongo(Integer recipeId, RecipeDTO recipeDTO) {
        
        Document recipeDocument = new Document("recipe_id", recipeId);
        
        // Add ingredients list
        List<Document> ingredientDocuments = new ArrayList<>();
        for (Ingredient i : recipeDTO.getIngredients()) {
            Document ingredientDoc = new Document()
                .append("ingredient", i.getIngredient())
                .append("amount", i.getAmount())
                .append("unit", i.getUnit())
                .append("notes", i.getNotes());
            ingredientDocuments.add(ingredientDoc);
        }
        recipeDocument.append("ingredients", ingredientDocuments);
        
        // Add instructions list
        List<Document> instructionDocuments = new ArrayList<>();
        int stepNumber = 1;
        for (Instruction i : recipeDTO.getInstructions()) {
            Document instructionDoc = new Document()
                .append("step_number", stepNumber++)
                .append("instruction", i.getStep());
            instructionDocuments.add(instructionDoc);
        }
        recipeDocument.append("instructions", instructionDocuments);
        

        mongoTemplate.insert(recipeDocument, "recipes");

    }


    public void deleteRecipeFromMongo(Integer recipeId) {
        Query query = new Query(Criteria.where("recipeId").is(recipeId));
        mongoTemplate.remove(query, "recipes");
    }

    public void deleteRecipeFromMySQL(Integer recipeId) {
        jdbcTemplate.update(Queries.SQL_DELETE_RECIPE, recipeId);
    }

    public void updateRecipeImageUrl(String imageUrl, Integer recipeId) {
        jdbcTemplate.update(Queries.SQL_UPDATE_RECIPE_IMAGE_URL, imageUrl, recipeId);
    }


    public void insertPostIntoActivities(int userId, int recipeId) {
        jdbcTemplate.update(Queries.SQL_INSERT_POST_ACTIVITY, userId, recipeId);
    }


    public Optional<Recipe> getRecipeById(int recipeId) {
        
        try {
            
            // get recipe from mySQL
            Recipe foundRecipe = jdbcTemplate.queryForObject(Queries.SQL_GET_RECIPE_BY_ID, BeanPropertyRowMapper.newInstance(Recipe.class), recipeId);
            System.out.println("Got recipe from mySQL");

            // get ingredients and instructions from mongo
            Criteria criteria = Criteria.where("recipe_id").is(recipeId);
            Query query = Query.query(criteria);
    
            Document d = mongoTemplate.findOne(query, Document.class, "recipes");

            if (d != null) {
            
                System.out.println("Got ingredients and instructions from mongo");
                List<Document> ingredientsDocuments = d.getList("ingredients", Document.class);
                List<Ingredient> ingredients = jsonParser.docToIngredients(ingredientsDocuments);

                List<Document> instructionsDocuments = d.getList("instructions", Document.class);
                List<Instruction> instructions = jsonParser.docToInstructions(instructionsDocuments);

                foundRecipe.setIngredients(ingredients);
                foundRecipe.setInstructions(instructions);
                
                return Optional.of(foundRecipe);

            } else {
                System.out.println("Recipe ingredients and instructions cannot be found in mongoDB");
                return Optional.empty();
            }

        } catch (DataAccessException e) {
            System.out.println("Recipe cannot be found in mySQL and/or mongo");
            return Optional.empty();
        }
    }


    public Page<RecipeOverview> getRecipeOverviews(Pageable pageable, RecipeFilterCriteria filterCriteria) {

        StringBuilder whereClause = new StringBuilder();
        List<Object> params = new ArrayList<>();
        
        whereClause.append(" WHERE 1=1 ");
        
        // Add filter conditions
        if (filterCriteria != null) {
            whereClause.append(" AND r.calories BETWEEN ? AND ? ");
            params.add(filterCriteria.getMinCalories());
            params.add(filterCriteria.getMaxCalories());
            
            whereClause.append(" AND r.protein BETWEEN ? AND ? ");
            params.add(filterCriteria.getMinProtein());
            params.add(filterCriteria.getMaxProtein());
            
            whereClause.append(" AND r.carbs BETWEEN ? AND ? ");
            params.add(filterCriteria.getMinCarbs());
            params.add(filterCriteria.getMaxCarbs());
            
            whereClause.append(" AND r.fats BETWEEN ? AND ? ");
            params.add(filterCriteria.getMinFats());
            params.add(filterCriteria.getMaxFats());
        }
        
        // Count query with filters
        String countSql = "SELECT COUNT(DISTINCT r.recipe_id) FROM recipes r" + whereClause;
        int totalRecipes = jdbcTemplate.queryForObject(countSql, Integer.class, params.toArray());
        
        String orderBy;
        if (pageable.getSort().isEmpty()) {
            orderBy = "r.updated_at";
        } else {
            String property = pageable.getSort().iterator().next().getProperty();
            switch (property) {
                case "likes":
                    orderBy = "likes";
                    break;
                default:
                    orderBy = "r.updated_at";
            }
        }
        
        int pageSize = pageable.getPageSize();
        long offset = pageable.getOffset();
        
        // Full query with filtering, sorting and pagination
        String sql = "SELECT " +
                "r.recipe_id AS recipeId, " +
                "r.title, " +
                "r.protein, " +
                "r.carbs, " +
                "r.fats, " +
                "r.calories, " +
                "r.image_url AS imageUrl, " +
                "u.username, " +
                "COUNT(DISTINCT l.id) AS likes " +
                "FROM recipes r " +
                "LEFT JOIN users u ON r.user_id = u.id " +
                "LEFT JOIN likes l ON r.recipe_id = l.recipe_id " +
                whereClause +
                "GROUP BY r.recipe_id " +
                "ORDER BY " + orderBy + " DESC " +
                "LIMIT ? OFFSET ?";
        
        // Add pagination parameters
        params.add(pageSize);
        params.add(offset);
        
        System.out.println("Executing query: " + sql);
        System.out.println("Parameters: " + params);
        
        // Execute
        List<RecipeOverview> recipeOverviews = jdbcTemplate.query(
            sql, 
            BeanPropertyRowMapper.newInstance(RecipeOverview.class), 
            params.toArray()
        );
        
        return new PageImpl<>(recipeOverviews, pageable, totalRecipes);
    }


    public Page<RecipeOverview> getRecipesByUserId(Pageable pageable, int userId) {
        
        String countSql = String.format("SELECT COUNT(DISTINCT r.recipe_id) FROM recipes r WHERE r.user_id = %s", userId);
        int totalRecipes = jdbcTemplate.queryForObject(countSql, Integer.class);

        int pageSize = pageable.getPageSize();
        long offset = pageable.getOffset();

        List<RecipeOverview> recipeOverviews = jdbcTemplate.query(Queries.SQL_GET_RECIPES_BY_USERID, BeanPropertyRowMapper.newInstance(RecipeOverview.class), userId, pageSize, offset);

        return new PageImpl<>(recipeOverviews, pageable, totalRecipes);
    }


    public Page<RecipeOverview> getLikedRecipeOverviews(Pageable pageable, int userId) {

        String countSql = String.format("SELECT COUNT(DISTINCT l.id) FROM likes l WHERE l.user_id = %s", userId);
        int totalRecipes = jdbcTemplate.queryForObject(countSql, Integer.class);

        int pageSize = pageable.getPageSize();
        long offset = pageable.getOffset();

        List<RecipeOverview> recipeOverviews = jdbcTemplate.query(Queries.SQL_GET_LIKED_RECIPE_OVERVIEWS_BY_USERID, BeanPropertyRowMapper.newInstance(RecipeOverview.class), userId, pageSize, offset);

        return new PageImpl<>(recipeOverviews, pageable, totalRecipes);
    }

}