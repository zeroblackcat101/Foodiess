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

import org.w3c.dom.Comment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.grpc.Context;
import models.PostComment;
import models.User;
import models.UserRecipeComment;
import utils.CustomTask;
import utils.CustomTaskResult;

public class PostApi {
    private static CollectionReference posts;
    private static StorageReference postImagesRef;


    static {
        posts = FirebaseFirestore.getInstance().collection("posts");
        FirebaseStorage storage = FirebaseStorage.getInstance("gs://loginandregister-d2b3c.appspot.com");
        StorageReference storageRef = storage.getReference();
        postImagesRef = storageRef.child("post_images");
    }

    public static void createPost(
            String title,
            String description,
            Uri uri,
            User user,
            CustomTask<Void> customTask
    ) {

        if (!AuthApi.isLoggedIn()) {
            customTask.complete(new CustomTaskResult<Void>(false, "You need to be logged in", null));

            return;
        }


        HashMap<String, Object> postData = new HashMap<String, Object>();
        postData.put("title", title);
        postData.put("description", description);
        postData.put("user_id", AuthApi.getCurrentUser().getId());


        final String postId = posts.document().getId();
        postData.put("post_id", postId);
        postData.put("post_comments", new ArrayList<>());


        postData.put("user", user.toMap());

        StorageReference ref = postImagesRef.child(postId);


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
                    postData.put("image_url", downloadUri);
                    posts.document(postId).set(postData).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {
                                customTask.complete(new CustomTaskResult<Void>(true, null, null));

                            } else {
                                customTask.complete(new CustomTaskResult<Void>(false, "Unable to create post", null));


                            }

                        }
                    });

                } else {
                    customTask.complete(new CustomTaskResult<Void>(false, "Error ! " + task.getException().getMessage(), null));

                }
            }
        });
    }

    public static void addComment(
            String post_id,
            String userID,
            String postComment,
            User user,
            CustomTask<PostComment> customTask
    ) {
        if (!AuthApi.isLoggedIn()) {
            customTask.complete(new CustomTaskResult<PostComment>(false, "You need to be logged in", null));

            return;
        }

        HashMap<String, Object> commentData = new HashMap<>();
        commentData.put("post_id", post_id);
        commentData.put("user_id", userID);
        commentData.put("post_comment", postComment);
        commentData.put("user", user.toMap());

        posts.document(post_id).update("post_comments", FieldValue.arrayUnion(commentData)).addOnCompleteListener(task -> {

            if (task.isSuccessful()) {
                customTask.complete(new CustomTaskResult<PostComment>(true, null, PostComment.fromMap(commentData)));
            } else {
                customTask.complete(new CustomTaskResult<PostComment>(false, "Failed to add comment", null));
            }

        });

    }

    public static void deletePost(String post_id, CustomTask<Void> customTask) {

        posts.document(post_id).delete().addOnCompleteListener(task -> {

            if (task.isSuccessful()) {
                customTask.complete(new CustomTaskResult<Void>(true, null, null));
            } else {
                customTask.complete(new CustomTaskResult<Void>(false, "Failed to delete post", null));
            }

        });

    }
}
