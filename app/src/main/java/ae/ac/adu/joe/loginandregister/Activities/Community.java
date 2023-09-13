package ae.ac.adu.joe.loginandregister.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.ListenerRegistration;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import ae.ac.adu.joe.loginandregister.Fragments.CommunityFragment;
import ae.ac.adu.joe.loginandregister.R;
import apis.AuthApi;
import apis.PostApi;

public class Community extends AppCompatActivity {

    Dialog popAddPost;
    FloatingActionButton fab;
    Toolbar toolbar;
    TextView postTitle, postDesc;
    ImageView postImg, createBtn;
    FirebaseAuth fAuth;
    FirebaseUser currentUser;
    ProgressBar clickProgress;
    Uri imgUri = null;

    ListenerRegistration postListener;

    @Override
    protected void onDestroy() {
//        postListener.remove();
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fab = findViewById(R.id.fab1);

        //ini
        fAuth = FirebaseAuth.getInstance();
        currentUser = fAuth.getCurrentUser();



        //ini popup
        iniPopup();
        setUpPopupImgClick();


        fab.setVisibility(AuthApi.isLoggedIn()?View.VISIBLE:View.INVISIBLE);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popAddPost.show();
            }
        });
        //Initialize and Assign Variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //Set Dashboard Selected
        bottomNavigationView.setSelectedItemId(R.id.community);

        //Perform ItemSelectedListener
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.search:
                        startActivity(new Intent(getApplicationContext(), Search.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), Home.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.add:
                        startActivity(new Intent(getApplicationContext(), AddRecipe.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.community:
                        return true;
                    case R.id.profile:
                        if (AuthApi.isLoggedIn()){
                            startActivity(new Intent(getApplicationContext(), Profile.class));
                        }
                        else{
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        }
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

        // set the home fragment as the default one
//
        getSupportFragmentManager().beginTransaction().replace(R.id.container,new CommunityFragment()).commit();
    }

    private void setUpPopupImgClick() {
        ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
                uri -> {
                    // Handle the returned Uri
                   postImg.setImageURI(uri);
                   imgUri = uri;
                });


        postImg.setOnClickListener(view -> {
            //open gallery when img clicked
            mGetContent.launch("image/*");

        });
    }

    private void iniPopup() {
        popAddPost = new Dialog(this);
        popAddPost.setContentView(R.layout.popup_add_post);
        popAddPost.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popAddPost.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT,Toolbar.LayoutParams.WRAP_CONTENT);
        popAddPost.getWindow().getAttributes().gravity = Gravity.TOP;

        //ini popup widgets
        postImg = popAddPost.findViewById(R.id.popup_img);
        postTitle = popAddPost.findViewById(R.id.popup_title);
        postDesc = popAddPost.findViewById(R.id.popup_description);
        createBtn = popAddPost.findViewById(R.id.popup_create);
        clickProgress = popAddPost.findViewById(R.id.popup_progressBar);


        //add post click listener

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createBtn.setVisibility(View.INVISIBLE);
                clickProgress.setVisibility(View.VISIBLE);
                String popTitle = postTitle.getText().toString().trim();
                String popDesc = postDesc.getText().toString().trim();

                if (!popTitle.isEmpty()
                && !popDesc.isEmpty() && imgUri != null
                 )
                {
                    //create post object and add to firebase
                    PostApi.createPost(popTitle,popDesc,imgUri,AuthApi.getCurrentUser(), customTaskResult -> {
                        if (customTaskResult.isSuccessFull()) {

                            Toast.makeText(Community.this, "Post Created", Toast.LENGTH_SHORT).show();
                            clickProgress.setVisibility(View.INVISIBLE);
                            createBtn.setVisibility(View.VISIBLE);
                            popAddPost.dismiss();

                        } else {

                            Toast.makeText(Community.this,  customTaskResult.errorMessage(),Toast.LENGTH_LONG).show();
                            createBtn.setVisibility(View.VISIBLE);



                        }

                    });
                }
                else{
                    Toast.makeText(Community.this, "Please verify all input fields and post image",Toast.LENGTH_LONG).show();
                    createBtn.setVisibility(View.VISIBLE);
                    clickProgress.setVisibility(View.INVISIBLE);
                }
            }
        });
    }
}