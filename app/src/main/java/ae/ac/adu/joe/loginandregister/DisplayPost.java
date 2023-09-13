package ae.ac.adu.joe.loginandregister;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;
import java.util.List;

import ae.ac.adu.joe.loginandregister.Activities.DisplayUserRecipe;
import ae.ac.adu.joe.loginandregister.Adapters.CommentAdapter;
import ae.ac.adu.joe.loginandregister.Adapters.PostCommentAdapter;
import apis.AuthApi;
import apis.PostApi;
import apis.UserRecipeApi;
import models.Post;
import models.PostComment;
import models.UserRecipe;

public class DisplayPost extends AppCompatActivity {

    TextView displayPostTitle, displayPostDesc;
    EditText displayPostComment;
    ImageView displayPostImg;
    CircularImageView displayPostUserImg;
    Button addPostCommentBtn;
    RecyclerView rvPostComment;
    DocumentReference postRef;
    FirebaseFirestore fStore;
    List<PostComment> postComments = new ArrayList<>();

    ListenerRegistration commentListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_post);


//        Window w = getWindow();
//        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        //ini views
        displayPostTitle = findViewById(R.id.display_post_title);
        displayPostDesc = findViewById(R.id.display_post_desc);
        displayPostImg = findViewById(R.id.display_post_img);
        displayPostComment = findViewById(R.id.display_post_comment);
        displayPostComment.setVisibility(AuthApi.isLoggedIn()?View.VISIBLE:View.INVISIBLE);

        displayPostUserImg = findViewById(R.id.display_post_user_img);
        displayPostUserImg.setVisibility(AuthApi.isLoggedIn()?View.VISIBLE:View.INVISIBLE);


        rvPostComment = findViewById(R.id.rv_post_comment);

        addPostCommentBtn = findViewById(R.id.add_post_comment_btn);
        addPostCommentBtn.setVisibility(AuthApi.isLoggedIn()?View.VISIBLE:View.INVISIBLE);
        addPostCommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String comment = displayPostComment.getText().toString();
                String uid = AuthApi.getCurrentUser().getId();

                PostApi.addComment(getIntent().getExtras().getString("post_id"), uid, comment,AuthApi.getCurrentUser(), customTaskResult -> {
                    if (customTaskResult.isSuccessFull()) {
                        showMessage("Comment Added");
                        displayPostComment.setText("");
//                        addCommentBtn.setVisibility(View.VISIBLE);

                    } else {

                        showMessage("Failed to add comment : " + customTaskResult.errorMessage());



                    }

                });
            }
        });

        fStore = FirebaseFirestore.getInstance();


        String postImgDisplay = getIntent().getExtras().getString("postImage");
        Glide.with(this).load(postImgDisplay).into(displayPostImg);
        final List<String> userInfo = getIntent().getExtras().getStringArrayList("user");
        Glide.with(this).load(userInfo.get(4)).into(displayPostUserImg);

        String postTitle = getIntent().getExtras().getString("title");
        displayPostTitle.setText(postTitle);
        String postDesc = getIntent().getExtras().getString("description");
        displayPostDesc.setText(postDesc);

        iniPRvComment();



    }

    @Override
    protected void onDestroy() {
        commentListener.remove();
        super.onDestroy();
    }

    private void iniPRvComment() {
        rvPostComment.setLayoutManager(new LinearLayoutManager(this));

        PostCommentAdapter postCommentAdapter =
                new PostCommentAdapter(DisplayPost.this, postComments);

        rvPostComment.setAdapter(postCommentAdapter);

       postRef = FirebaseFirestore.getInstance().document("posts/" +  getIntent().getExtras().getString("post_id"));
        commentListener=   postRef.
                addSnapshotListener((value, error) -> {

            if(error ==null){

               Post post = Post.fromMap(value.getData());

                this.postComments.clear();
                this.postComments.addAll(post.getPostComments());
                postCommentAdapter.notifyDataSetChanged();




            }else{

                Log.i("postError", "Unable to get post");
            }

        });


    }
    private void showMessage(String message) {

        Toast.makeText(this,message,Toast.LENGTH_LONG).show();

    }


}