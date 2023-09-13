package apis;


import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

import ae.ac.adu.joe.loginandregister.R;
import at.favre.lib.crypto.bcrypt.BCrypt;
import models.User;
import utils.CustomTask;
import utils.CustomTaskResult;

public class AuthApi {

    private static CollectionReference users;
    private static FirebaseAuth firebaseAuth;
    private static StorageReference userImagesRef;

    private static User currentUser;

    static {
        firebaseAuth = FirebaseAuth.getInstance();
        users = FirebaseFirestore.getInstance().collection("users");
        FirebaseStorage storage = FirebaseStorage.getInstance("gs://loginandregister-d2b3c.appspot.com");
        StorageReference storageRef = storage.getReference();
        userImagesRef = storageRef.child("user_profile_images");

    }

    public static  boolean  isLoggedIn(){
        return currentUser !=null;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void register(String fullName, String email, String phoneNumber, String password, Uri uri, CustomTask<Void> customTask){
        HashMap<String,Object> data = new  HashMap<String,Object>();
        data.put("fullName", fullName);
        data.put("email", email);
        data.put("phoneNumber", phoneNumber);
        String hashedPassword = BCrypt.withDefaults().hashToString(12, password.toCharArray());
        data.put("hashPassword", hashedPassword);

        final String user_id = users.document().getId();
        StorageReference ref = userImagesRef.child(user_id);

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
                data.put("img_url",downloadUri);
                firebaseAuth.createUserWithEmailAndPassword(email,password).
                        addOnCompleteListener(authResultTask -> {
                            if(authResultTask.isSuccessful()){
                                final String uid =  firebaseAuth.getUid();
                                data.put("id", uid);
                                users.document(uid).set(data).addOnCompleteListener(task2 -> {
                                    if(task2.isSuccessful()){
                                        customTask.complete(new CustomTaskResult<Void>(true, null, null));
                                    }else{
                                        customTask.complete(new CustomTaskResult<Void>(false,"Was unable to add user",null ));
                                        return;
                                    }
                                });
                            }else{
                                customTask.complete(new CustomTaskResult<Void>(false,"Error ! " + authResultTask.getException().getMessage(),null ));
                            }
                        });


            } else {
                customTask.complete(new CustomTaskResult<Void>(false,"Error ! " + task.getException().getMessage(),null));

            }
        });







    }


    public static void login(Context context, String email, String password, CustomTask<User> onComplete){

        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getResources().getString(R.string.app_id), Context.MODE_PRIVATE);

        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(task -> {

            if(task.isSuccessful()){
                final String uid = firebaseAuth.getUid();
                users.document(uid).get()

                        .addOnCompleteListener(task1 -> {

                    if(task1.isSuccessful()){
                        final Map<String,Object> data = task1.getResult().getData();
                        final User user =  User.fromMap(data);
                        currentUser = user;
                       Log.e("USER",user.toString());
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("user_id", currentUser.getId());
                        editor.apply();
                        onComplete.complete(new CustomTaskResult<User>(true,null, user));
                    }else{
                        onComplete.complete(new CustomTaskResult<User>(false,"Couldn't Retrieve user with uid", null));

                    }


                });



            }else{
                onComplete.complete(new CustomTaskResult<User>(false,"Error ! " + task.getException().getMessage(), null));

            }

        });




    }


    public static void logout(Context context, CustomTask<User> onComplete){
        firebaseAuth.signOut();
        currentUser = null;
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getResources().getString(R.string.app_id), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("user_id", null);
        editor.apply();
        onComplete.complete(new CustomTaskResult<User>(true,null, null));
    }

    public static void autologin(Context  context, CustomTask<User> onComplete){
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getResources().getString(R.string.app_id), Context.MODE_PRIVATE);
      String user_id =   sharedPref.getString("user_id", null);

      if(user_id == null){
          onComplete.complete(new CustomTaskResult<User>(true,null, null));

          return ;
      }else{

          users.document(user_id).get().addOnCompleteListener(task -> {

              if(task.isSuccessful()){
                  currentUser = User.fromMap(task.getResult().getData());

                  onComplete.complete(new CustomTaskResult<User>(true,null, currentUser));

              }else{

                  onComplete.complete(new CustomTaskResult<User>(false,"Unable to auto login", null));

              }

          });



      }
    }




}
