package apis;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import models.User;
import models.UserRecipe;
import models.UserRecipeComment;
import utils.CustomTask;
import utils.CustomTaskResult;

public class UserRecipeApi {
    private static CollectionReference userRecipes;
    private static  StorageReference userImagesRef;


    static {
        userRecipes = FirebaseFirestore.getInstance().collection("user_recipes");
        FirebaseStorage storage = FirebaseStorage.getInstance("gs://loginandregister-d2b3c.appspot.com");
        StorageReference storageRef = storage.getReference();
        userImagesRef = storageRef.child("user_images");


    }
    public static void createUserRecipes(String title,
                                         String description,
                                         List<String> ingredients,
                                         List<String> steps,  Uri uri,
                                         CustomTask<Void> customTask)
    {
        if(!AuthApi.isLoggedIn()){
            customTask.complete(new CustomTaskResult<Void>(false,"You need to be logged in",null));

            return;
        }
        HashMap<String, Object> userRecipeData = new HashMap<String, Object>();
        userRecipeData.put("title", title);
        userRecipeData.put("description", description);
        userRecipeData.put("ingredients", ingredients);
        userRecipeData.put("steps", steps);
        userRecipeData.put("user_id", AuthApi.getCurrentUser().getId());

        final String recipeId  =  userRecipes.document().getId();
        userRecipeData.put("user_recipe_id", recipeId);
       userRecipeData.put("user_comments",new ArrayList<>());

       userRecipeData.put("user", AuthApi.getCurrentUser().toMap());

        StorageReference ref = userImagesRef.child(recipeId);
        UploadTask uploadTask = ref.putFile(uri);

        Task<Uri> urlTask = uploadTask.continueWithTask(task -> {
            if (!task.isSuccessful()) {
                throw task.getException();
            }

            // Continue with the task to get the download URL
            return ref.getDownloadUrl();
        }).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Uri downloadUri = task.getResult();
                userRecipeData.put("img_url",downloadUri);
                userRecipes.document(recipeId).set(userRecipeData).addOnCompleteListener(new OnCompleteListener<Void>() {
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
        });
    }


    public static void addComment(
            String userRecipeId,
            String userID,
            User user,
            String recipeComment, CustomTask<UserRecipeComment> customTask
    ){

        if(!AuthApi.isLoggedIn()){
            customTask.complete(new CustomTaskResult<UserRecipeComment>(false,"You need to be logged in",null));

            return;
        }

        HashMap<String,Object> commentData = new HashMap<>();
        commentData.put("user_recipe_id",userRecipeId);
        commentData.put("user_id",userID);
        commentData.put("comment",recipeComment);
        commentData.put("user",user.toMap());

        userRecipes.document(userRecipeId).update("user_comments", FieldValue.arrayUnion(commentData)).addOnCompleteListener(task -> {

            if(task.isSuccessful()){
                customTask.complete(new CustomTaskResult<UserRecipeComment>(true,null, UserRecipeComment.fromMap(commentData)));
            }else{
                customTask.complete(new CustomTaskResult<UserRecipeComment>(false,"Failed to add comment", null));
            }

        });


    }

    public static void deleteRecipe(String recipe_id,CustomTask<Void> customTask){

        userRecipes.document(recipe_id).delete().addOnCompleteListener(task -> {

            if(task.isSuccessful()){
                customTask.complete(new CustomTaskResult<Void>(true,null,null));
            }else{
                customTask.complete(new CustomTaskResult<Void>(false,"Failed to delete user recipe", null));
            }
        });

    }


}
