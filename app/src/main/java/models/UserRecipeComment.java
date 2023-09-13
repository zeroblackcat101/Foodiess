package models;

import com.google.firestore.v1.DocumentTransform;

import java.util.Map;

public class UserRecipeComment {
    String  user_recipe_id, userID, recipeComment;User user;

    public UserRecipeComment(String user_recipe_id, String userID, String recipeComment, User user) {

        this.user_recipe_id = user_recipe_id;
        this.userID = userID;
        this.recipeComment = recipeComment;
        this.user = user;


    }

    public static UserRecipeComment fromMap(Map<String, Object>  data){
        return new UserRecipeComment(
                (String) data.get("user_recipe_id"),
                (String) data.get("user_id"),
                (String) data.get("comment"),
                User.fromMap((Map<String, Object>) data.get("user"))
                );
    }



    public String getUser_recipe_id() {
        return user_recipe_id;
    }

    public String getUserID() {
        return userID;
    }

    public String getRecipeComment() {
        return recipeComment;
    }

    public User getUser() {
        return user;
    }

    @Override
    public String toString() {
        return "UserRecipeComment{" +
                "user_recipe_id='" + user_recipe_id + '\'' +
                ", userID='" + userID + '\'' +
                ", recipeComment='" + recipeComment + '\'' +
                ", user=" + user +
                '}';
    }
}
