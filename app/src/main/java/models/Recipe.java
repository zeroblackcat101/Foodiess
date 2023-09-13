package models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Recipe {


    private String user_id, recipe_id, name, description, category, img_url;
    private List<String> ingredients, steps;
    private List<RecipeLike> recipeLikes;
    private List<UserRecipeComment> userRecipeComments;
    private Nutrition nutritionInfo;
    private List<String> keyWords;

    public Recipe(String recipe_id, String user_id, String name, String category, String description, List<String> ingredients, List<String> steps, String img_url, List<RecipeLike> recipeLikes, List<UserRecipeComment> userRecipeComments, Nutrition nutritionInfo, List<String> keyWords) {
        this.recipe_id = recipe_id;

        this.user_id = user_id;
        this.name = name;
        this.category = category;
        this.description = description;
        this.ingredients = ingredients;
        this.steps = steps;
        this.img_url = img_url;
        this.recipeLikes = recipeLikes;
        this.userRecipeComments = userRecipeComments;
        this.nutritionInfo = nutritionInfo;
        this.keyWords  = keyWords;
    }


    public static Recipe fromMap(Map<String, Object> data) {

        return new Recipe(
                (String) data.get("recipe_id")

                , (String) data.get("user_id")
                , (String) data.get("name")
                , (String) data.get("category")
                , (String) data.get("description")
                , (List<String>) data.get("ingredients")
                , (List<String>) data.get("steps")
                , (String) data.get("img_url")
                //converts list of string to MAP of RecipeLike
                ,
                new ArrayList<RecipeLike>()
//                ((List<Map<String, Object>>) data.get("recipeLikes")).stream().map(e -> RecipeLike.fromMap(e)).collect(Collectors.toList())
                ,
                new ArrayList<UserRecipeComment>()

//                ((List<Map<String, Object>>) data.get("userRecipeComments")).stream().map(e -> UserRecipeComment.fromMap(e)).collect(Collectors.toList())

                , Nutrition.fromMap((Map<String, Object>)data.get("nutritional_info")),

                (List<String>) data.get("key_words")
        );


    }

    public String getUser_id() {
        return user_id;
    }

    public String getRecipe_id() {
        return recipe_id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
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

    public List<RecipeLike> getRecipeLikes() {
        return recipeLikes;
    }

    public List<UserRecipeComment> getRecipeComments() {
        return userRecipeComments;
    }

    public Nutrition getNutritionInfo() {
        return nutritionInfo;
    }

    public List<String> getKeyWords() {
        return keyWords;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "user_id='" + user_id + '\'' +
                ", recipe_id='" + recipe_id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", img_url='" + img_url + '\'' +
                ", ingredients=" + ingredients +
                ", steps=" + steps +
                ", recipeLikes=" + recipeLikes +
                ", userRecipeComments=" + userRecipeComments +
                ", nutritionInfo=" + nutritionInfo +
                '}';
    }
}
