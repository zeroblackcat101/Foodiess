package ae.ac.adu.joe.loginandregister.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;
import java.util.List;

import ae.ac.adu.joe.loginandregister.Adapters.CommentAdapter;
import ae.ac.adu.joe.loginandregister.Adapters.UserRecipeAdapter;
import ae.ac.adu.joe.loginandregister.R;
import apis.AuthApi;
import apis.UserRecipeApi;
import models.UserRecipe;
import models.UserRecipeComment;

public class DisplayUserRecipe extends AppCompatActivity {
    FirebaseFirestore fStore;
    TextView post_title, tvPostDesc;
    ImageView post_detail_img;
    LinearLayout postIngredientLayout, postStepsLayout;
    Button addCommentBtn;
    EditText postComment;
    RecyclerView rvComment;
    DocumentReference userRecipeRef;
    CircularImageView post_detail_user_img;
    List<UserRecipeComment> recipeComments = new ArrayList<>();
    ListenerRegistration commentListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_user_recipe);

        // let's set the statue bar to transparent
//        Window w = getWindow();
//        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);


        //ini views
        post_title = findViewById(R.id.post_title);
        tvPostDesc = findViewById(R.id.tvPostDesc);
        post_detail_img = findViewById(R.id.post_detail_img);
        post_detail_user_img = findViewById(R.id.post_detail_user_img);
        post_detail_user_img.setVisibility(AuthApi.isLoggedIn() ? View.VISIBLE : View.INVISIBLE);


        postIngredientLayout = findViewById(R.id.postIngredientLayout);
        postStepsLayout = findViewById(R.id.postStepsLayout);
        postComment = findViewById(R.id.post_comment);
        postComment.setVisibility(AuthApi.isLoggedIn() ? View.VISIBLE : View.INVISIBLE);

        rvComment = findViewById(R.id.rv_comment);


        fStore = FirebaseFirestore.getInstance();

        addCommentBtn = findViewById(R.id.add_comment_btn);
        addCommentBtn.setVisibility(AuthApi.isLoggedIn() ? View.VISIBLE : View.INVISIBLE);

        addCommentBtn.setOnClickListener(view -> {
            String comment = postComment.getText().toString();
            String uid = AuthApi.getCurrentUser().getId();
            UserRecipeApi.addComment(getIntent().getExtras().getString("user_recipe_id"), uid, AuthApi.getCurrentUser(), comment, customTaskResult -> {
                if (customTaskResult.isSuccessFull()) {
                    showMessage("Comment Added");
                    postComment.setText("");

                } else {

                    showMessage("Failed to add comment : " + customTaskResult.errorMessage());
                }

            });
        });

        final List<String> userInfo = getIntent().getExtras().getStringArrayList("user");

        String postImage = getIntent().getExtras().getString("img_url");
        Glide.with(this).load(postImage).into(post_detail_img);
        Glide.with(this).load(userInfo.get(4)).into(post_detail_user_img);

        String title = getIntent().getExtras().getString("title");
        post_title.setText(title);
        String desc = getIntent().getExtras().getString("description");
        tvPostDesc.setText(desc);
        List<String> ingredients = getIntent().getExtras().getStringArrayList("ingredients");
        for (int i = 0; i < ingredients.size(); i++) {
            TextView textView = new TextView(postIngredientLayout.getContext());
            String ingredient = ingredients.get(i);
            textView.setTextColor(Color.BLACK);
            textView.setText(ingredient);
            postIngredientLayout.addView(textView);
        }
        List<String> steps = getIntent().getExtras().getStringArrayList("steps");
        for (int i = 0; i < steps.size(); i++) {
            String step = steps.get(i);
            TextView textView = new TextView(postStepsLayout.getContext());
            textView.setTextColor(Color.BLACK);
            textView.setText((i + 1) + ". " + step);
            postStepsLayout.addView(textView);
        }

        iniRvComment();


    }
    @Override
    protected void onDestroy() {
        commentListener.remove();
        super.onDestroy();
    }

    private void iniRvComment() {
        rvComment.setLayoutManager(new LinearLayoutManager(this));

        CommentAdapter commentAdapter =
                new CommentAdapter(DisplayUserRecipe.this, recipeComments);

        rvComment.setAdapter(commentAdapter);

        userRecipeRef = FirebaseFirestore.getInstance().document("user_recipes/" + getIntent().getExtras().getString("user_recipe_id"));
        commentListener =userRecipeRef.addSnapshotListener((value, error) -> {

            if (error == null) {

                UserRecipe userRecipe = UserRecipe.fromMap(value.getData());

                this.recipeComments.clear();
                this.recipeComments.addAll(userRecipe.getUserRecipeComments());
                commentAdapter.notifyDataSetChanged();


            } else {

                Log.i("userrecipeerror", "Unable to get user recipe");
            }

        });


    }

    private void showMessage(String message) {

        Toast.makeText(this, message, Toast.LENGTH_LONG).show();

    }


}
