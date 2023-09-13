package ae.ac.adu.joe.loginandregister.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mikhaellopez.circularimageview.CircularImageView;

import ae.ac.adu.joe.loginandregister.R;
import apis.AuthApi;

public class Profile extends AppCompatActivity {

    TextView nameTV,emailTV,phoneTV,fullNameTV,logOutTV;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    CircularImageView userProfileImg;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        nameTV = findViewById(R.id.nameTV);
        emailTV = findViewById(R.id.emailTV);
        phoneTV = findViewById(R.id.phoneTV);
        fullNameTV = findViewById(R.id.fullNameTV);
        logOutTV = findViewById(R.id.logOutTV);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userProfileImg = findViewById(R.id.userProfileImg);

        logOutTV.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(Profile.this);
            builder.setTitle("Sign out")
                    .setMessage("Do you really want to sign out")
                    .setNegativeButton("CANCEL", (dialog, which) -> dialog.dismiss())
                    .setPositiveButton("SIGN OUT", (dialog, which) -> {
                        AuthApi.logout(getApplicationContext(), customTaskResult -> {
                            Intent intent = new Intent(Profile.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
                            startActivity(intent);
                            finish();

                        });

                    })
                    .setCancelable(false);
            AlertDialog dialog = builder.create();
            dialog.setOnShowListener(dialog1 -> {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                        .setTextColor(ContextCompat.getColor(Profile.this,android.R.color.holo_red_dark));
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                        .setTextColor(ContextCompat.getColor(Profile.this,R.color.black));

            });

            dialog.show();

        });




        if (AuthApi.isLoggedIn()){
            nameTV.setText(AuthApi.getCurrentUser().getFullName());
            fullNameTV.setText(AuthApi.getCurrentUser().getFullName());
            emailTV.setText(AuthApi.getCurrentUser().getEmail());
            phoneTV.setText(AuthApi.getCurrentUser().getPhoneNumber());
            Glide.with(this).load(AuthApi.getCurrentUser().getProfile_img()).into(userProfileImg);


        }
        else{
            Toast.makeText(getApplicationContext(),"User is not logged in",Toast.LENGTH_SHORT).show();
        }

        //Initialize and Assign Variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //Set Dashboard Selected
        bottomNavigationView.setSelectedItemId(R.id.profile);

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
                        startActivity(new Intent(getApplicationContext(), Community.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.profile:
                        return true;
                }
                return false;
            }
        });

    }
}