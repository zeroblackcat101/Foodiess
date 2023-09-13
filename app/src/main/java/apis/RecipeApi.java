package apis;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import models.Recipe;
import models.RecipeLike;
import models.User;
import models.UserRecipe;
import utils.CustomTask;
import utils.CustomTaskResult;

public class RecipeApi {

    private static CollectionReference recipes;
    private static StorageReference imagesRef;

    static {
        recipes = FirebaseFirestore.getInstance().collection("recipes");
        FirebaseStorage storage = FirebaseStorage.getInstance("gs://loginandregister-d2b3c.appspot.com");
        StorageReference storageRef = storage.getReference();
         imagesRef = storageRef.child("images");
    }


    public static void createRecipe(
            String name,
            String description,
            String category,
            List<String> ingredients,
            List<String> steps,
            long calories,
            long carbs,
            long fat,
            long protein,
            long sodium,
            long cholesterol,
            Uri uri,
            CustomTask<Void> customTask
    ) {

        if(!AuthApi.isLoggedIn()){
            customTask.complete(new CustomTaskResult<Void>(false,"You need to be logged in",null));

            return;
        }

        HashMap<String, Object> recipeData = new HashMap<String, Object>();
        recipeData.put("name", name);
        recipeData.put("category", category);
        recipeData.put("description", description);
        recipeData.put("ingredients", ingredients);
        recipeData.put("steps", steps);
        recipeData.put("user_id", AuthApi.getCurrentUser().getId());

        ArrayList<String> keyWordsList = new ArrayList<>();

        for(String ingredient: ingredients){
            ArrayList<String> splittedIngredients = new ArrayList<String>(Arrays.asList(ingredient.split(" ")));



            splittedIngredients.removeIf(s -> !s.matches(("(.*[a-zA-Z].*)")));
            keyWordsList.addAll(splittedIngredients);


        }
        recipeData.put("key_words",keyWordsList);


        HashMap<String, Object> nutritionData = new HashMap<String, Object>();
        nutritionData.put("calories", calories);
        nutritionData.put("carbohydrates", carbs);
        nutritionData.put("fat", fat);
        nutritionData.put("protein", protein);
        nutritionData.put("sodium", sodium);
        nutritionData.put("cholesterol", cholesterol);

        recipeData.put("nutritional_info", nutritionData);



       final String recipeId  =  recipes.document().getId();
        recipeData.put("recipe_id", recipeId);

        StorageReference ref = imagesRef.child(recipeId);


       UploadTask uploadTask = ref.putFile(uri);


        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return ref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    recipeData.put("img_url",downloadUri);
                    recipes.document(recipeId).set(recipeData).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful()){
                                customTask.complete(new CustomTaskResult<Void>(true,null,null));

                            }else{
                                customTask.complete(new CustomTaskResult<Void>(false,"Unable to create recipe",null));


                            }

                        }
                    });

                } else {
                    customTask.complete(new CustomTaskResult<Void>(false,"Error ! " + task.getException().getMessage(),null));

                }
            }
        });
    }





    public static void getAllRecipes(CustomTask<List<Recipe>> customTask){

    recipes.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
             @Override
             public void onComplete(@NonNull Task<QuerySnapshot> task) {
                 if(task.isSuccessful()){

                    final List<Recipe>  recipeList =  task.getResult().getDocuments().stream().map(e -> Recipe.fromMap(e.getData())).collect(Collectors.toList());
                     customTask.complete(new CustomTaskResult<>(true,null,recipeList));


                 }else{
                     customTask.complete(new CustomTaskResult<>(false,"Error ! " + task.getException().getMessage(),null));

                 }

             }
         });
    }



    public static void updateAllRecipesWithKeywordsField(){


        recipes.get().addOnCompleteListener(task -> {

            if(task.isSuccessful()){
               List<Recipe>  recipesList=   task.getResult().getDocuments().stream().map(e -> Recipe.fromMap(e.getData())).collect(Collectors.toList());
               for (Recipe recipe:recipesList){
                   ArrayList<String> keyWordsList = new ArrayList<>();

                   for(String ingredient: recipe.getIngredients()){
                       ArrayList<String> splittedIngredients = new ArrayList<String>(Arrays.asList(ingredient.split("[^a-zA-Z]")));



                       splittedIngredients.removeIf(s -> !s.matches((  "(.*[a-zA-Z].*)")));
                       keyWordsList.addAll(splittedIngredients);

                   }


                   recipes.document(recipe.getRecipe_id()).update("key_words", keyWordsList);
               }


            }

        });
    }
}
