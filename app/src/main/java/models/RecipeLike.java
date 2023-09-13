package models;

import java.util.Map;

public class RecipeLike {

    String id, userId, recipeID;

    public RecipeLike(String id, String userId, String recipeID) {
        this.id  = id;
        this.userId = userId;
        this.recipeID = recipeID;
    }

    public static RecipeLike fromMap(Map<String, Object> data){

        return new RecipeLike(
                (String) data.get("id"),
                (String) data.get("user_id"),
                (String) data.get("recipe_id")

        );
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getRecipeID() {
        return recipeID;
    }

    @Override
    public String toString() {
        return "RecipeLike{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", recipeID='" + recipeID + '\'' +
                '}';
    }
}
