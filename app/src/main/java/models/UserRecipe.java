package models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UserRecipe {
    private String user_id, user_recipe_id, title, description,img_url;
    private List<String> ingredients, steps;
    private List<UserRecipeComment> userRecipeComments;
    private User user;


    public UserRecipe(String user_recipe_id, String user_id,  String title, String description, List<String> ingredients, List<String> steps, String img_url,List<UserRecipeComment> userRecipeComments, User user) {
        this.user_id = user_id;
        this.user_recipe_id = user_recipe_id;
        this.title = title;
        this.description = description;
        this.ingredients = ingredients;
        this.steps = steps;
        this.img_url = img_url;
        this.userRecipeComments = userRecipeComments;
        this.user = user;
    }
    public static UserRecipe fromMap(Map<String,Object> data){
        return new UserRecipe(
                (String) data.get("user_recipe_id")
                , (String) data.get("user_id")
                , (String) data.get("title")
                , (String) data.get("description")
                , (List<String>) data.get("ingredients")
                , (List<String>) data.get("steps")
                , (String) data.get("img_url"),
                (data.get("user_comments")==null)? new ArrayList<UserRecipeComment>() :((List<Map<String, Object>>) data.get("user_comments")).stream().map(e -> UserRecipeComment.fromMap(e)).collect(Collectors.toList()),
                User.fromMap((Map<String,Object>)data.get("user"))
        );
    }

    public void setUser_recipe_id(String user_recipe_id) {
        this.user_recipe_id = user_recipe_id;
    }


    public String getUser_recipe_id() {
        return user_recipe_id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public List<String> getSteps() {
        return steps;
    }

    public String getImg_url() {
        return img_url;
    }

    public List<UserRecipeComment> getUserRecipeComments() {
        return userRecipeComments;
    }

    public User getUser() {
        return user;
    }

    public String getUser_id() {
        return user_id;
    }

    @Override
    public String toString() {
        return "UserRecipe{" +
                "user_id='" + user_id + '\'' +
                ", user_recipe_id='" + user_recipe_id + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", img_url='" + img_url + '\'' +
                ", ingredients=" + ingredients +
                ", steps=" + steps +
                ", userRecipeComments=" + userRecipeComments +
                ", user=" + user +
                '}';
    }
}
