package sg.edu.nus.iss.springboot.repositories;

public class Queries {
    
    public static final String SQL_GET_USER_BY_USERNAME = """
        SELECT * FROM users WHERE username = ?;
    """;


    public static final String SQL_GET_USER_BY_EMAIL = """
        SELECT * FROM users WHERE email = ?;
    """;


    public static final String SQL_INSERT_USER = """
        INSERT INTO users 
            (name, username, email, password_hash, profile_picture, short_bio) 
        VALUES 
            (?, ?, ?, ?, ?, ?);
    """;


    public static final String SQL_UPDATE_PROFILE_PICTURE = """
        UPDATE users
            set profile_picture = ?
            where username = ?
    """;


    public static final String SQL_UPDATE_BIO = """
        UPDATE users
            set short_bio = ?
            where username = ?        
    """;


    public static final String SQL_INSERT_NEW_RECIPE = """
        INSERT into recipes
            (user_id, title, description, protein, carbs, fats, calories, servings, prep_time, cook_time)
        VALUES
            (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
    """;


    public static final String SQL_DELETE_RECIPE = """
        DELETE from recipes
            where recipe_id = ?
    """;


    public static final String SQL_UPDATE_RECIPE_IMAGE_URL = """
        UPDATE recipes
            set image_url = ?
            where recipe_id = ?
    """;


    public static final String SQL_GET_RECIPE_BY_ID = """
        SELECT r.recipe_id,
                r.user_id, 
                u.username, 
                r.title, 
                r.description, 
                r.protein, 
                r.carbs, 
                r.fats, 
                r.calories, 
                r.servings, 
                r.prep_time, 
                r.cook_time, 
                r.image_url, 
                r.updated_at
        FROM recipes r
        JOIN users u ON r.user_id = u.id
        WHERE r.recipe_id = ?
    """;


    public static final String SQL_INSERT_NEW_COMMENT = """
        INSERT into comments
            (user_id, recipe_id, comment)
        values
            (?, ?, ?)        
    """;


    public static final String SQL_GET_COMMENTS_FOR_RECIPE = """
        SELECT u.username,
               u.profile_picture,
               c.comment
            FROM comments c
            JOIN users u ON c.user_id = u.id
        WHERE c.recipe_id = ?
        ORDER BY c.timestamp DESC;
    """;


    public static final String SQL_LIKE_RECIPE = """
        INSERT IGNORE into likes
            (user_id, recipe_id)
        VALUES
            (?, ?)
    """;


    public static final String SQL_UNLIKE_RECIPE = """
        DELETE from likes
            where user_id = ? AND recipe_id = ?
    """;


    public static final String SQL_GET_LIKES_BY_USERID = """
        SELECT 
            recipe_id 
        FROM
            likes
        WHERE 
            user_id = ?
    """;


    public static final String SQL_GET_RECIPES_BY_USERID = """
        SELECT 
            r.recipe_id AS recipeId,
            r.title,
            r.protein,
            r.carbs,
            r.fats,
            r.calories,
            r.image_url AS image_Url,
            u.username,
            COUNT(DISTINCT l.id) AS likes
        FROM 
            recipes r
        LEFT JOIN 
            users u ON r.user_id = u.id
        LEFT JOIN 
            likes l ON r.recipe_id = l.recipe_id
        WHERE
            r.user_id = ?
        GROUP BY
            r.recipe_id
        ORDER BY 
            r.updated_at DESC
        LIMIT ? OFFSET ?
    """;


    public static final String SQL_GET_LIKED_RECIPE_OVERVIEWS_BY_USERID = """
        SELECT 
            r.recipe_id, 
            r.title,
            r.protein,
            r.carbs,
            r.fats,
            r.calories,
            r.image_url,
            u.username,
            COUNT(DISTINCT l.id) AS likes
        FROM 
            recipes r
        JOIN 
            likes l ON r.recipe_id = l.recipe_id
        JOIN 
            users u ON r.user_id = u.id
        WHERE 
            l.user_id = ?
        GROUP BY 
            r.recipe_id
        ORDER BY 
            l.timestamp DESC
        LIMIT ? OFFSET ?
    """;
    

    public static final String SQL_FOLLOW_USER = """
        INSERT IGNORE into follows
            (follower_id, following_id)
        VALUES
            (?, ?)
    """;


    public static final String SQL_UNFOLLOW_USER = """
        DELETE from follows
            where follower_id = ? AND following_id = ?        
    """;


    public static final String SQL_GET_ALL_FOLLOWED_USERS = """
        SELECT 
            u.id AS userId,
            u.username, 
            u.profile_picture AS profilePicture
        FROM
            users u
        JOIN
            follows f ON u.id = f.following_id
        WHERE
            f.follower_id = ?
    """;


    public static final String SQL_GET_FOLLOW_PROFILE = """
        SELECT
            u.id AS userId,
            u.username,
            u.profile_picture AS profilePicture
        FROM
            users u
        WHERE u.id = ?        
    """;


    public static final String SQL_INSERT_POST_ACTIVITY = """
        INSERT into activities
            (user_id, activity_type, recipe_id)
        VALUES
            (?, "post", ?)
    """;


    public static final String SQL_INSERT_LIKE_ACTIVITY = """
        INSERT into activities
            (user_id, activity_type, recipe_id)
        VALUES
            (?, "like", ?)
    """;


    public static final String SQL_REMOVE_LIKE_ACTIVITY = """
        DELETE from activities
            WHERE user_id = ? AND activity_type = "like" AND recipe_id = ?
    """;


    public static final String SQL_INSERT_COMMENT_ACTIVITY = """
        INSERT into activities
            (user_id, activity_type, recipe_id, comment_id)
        VALUES
            (?, "comment", ?, ?)
    """;


    public static final String SQL_INSERT_FOLLOW_ACTIVITY = """
        INSERT into activities
            (user_id, activity_type, followed_user_id)
        VALUES
            (?, "follow", ?)
    """;

    public static final String SQL_REMOVE_FOLLOW_ACTIVITY = """
        DELETE from activities
            WHERE user_id = ? AND activity_type = "follow" AND followed_user_id = ?        
    """;


    public static final String SQL_GET_ACTIVITY = """
        SELECT * from activity_feed_view
        WHERE user_id IN (%s)
        ORDER BY timestamp DESC
        LIMIT ? OFFSET ?
    """;


    public static final String SQL_GET_PARTNER_STORES = """
        SELECT 
            id AS partnerStoreId,
            name,
            address_line_1 AS addressLine1,
            address_line_2 AS addressLine2,
            postal_code,
            phone,
            latitude,
            longitude,
            website
        FROM partner_stores
        ORDER BY name ASC
    """;

}