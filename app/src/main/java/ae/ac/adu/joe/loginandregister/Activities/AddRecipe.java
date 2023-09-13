package ae.ac.adu.joe.loginandregister.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import ae.ac.adu.joe.loginandregister.Fragments.AddRecipeFragment;
import ae.ac.adu.joe.loginandregister.R;
import apis.AuthApi;
import apis.UserRecipeApi;

public class AddRecipe extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Uri imgUri = null;
    Dialog popAddRecipe;
    FloatingActionButton fab;
    Toolbar toolbar;
    FirebaseAuth fAuth;
    FirebaseUser currentUser;
    ImageView popupRecipeImg, popupAddBtn;
    TextView popupTitle, popupDesc;
    LinearLayout ingredientsLinearLayout, rootLinearLayout, stepsLinearLayout;
    ProgressBar popupProgress;
    Button addIngredientBtn, addStepsBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fab = findViewById(R.id.fab);

        //ini
        fAuth = FirebaseAuth.getInstance();
        currentUser = fAuth.getCurrentUser();

        //ini popup
        iniPopup();
        setUpPopupImgClick();
        fab.setVisibility(AuthApi.isLoggedIn() ? View.VISIBLE : View.INVISIBLE);

        fab.setOnClickListener(view -> popAddRecipe.show());

        //Initialize and Assign Variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //Set Dashboard Selected
        bottomNavigationView.setSelectedItemId(R.id.add);

        //Perform ItemSelectedListener
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.search:
                    startActivity(new Intent(getApplicationContext(), Search.class));
                    overridePendingTransition(0, 0);
                    return true;
                case R.id.home:
                    startActivity(new Intent(getApplicationContext(), Home.class));
                    overridePendingTransition(0, 0);
                    return true;
                case R.id.add:
                    return true;
                case R.id.community:
                    startActivity(new Intent(getApplicationContext(), Community.class));
                    overridePendingTransition(0, 0);
                    return true;
                case R.id.profile:
                    if (AuthApi.isLoggedIn()) {
                        startActivity(new Intent(getApplicationContext(), Profile.class));
                    } else {
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    }
                    overridePendingTransition(0, 0);
                    return true;
            }
            return false;
        });

        getSupportFragmentManager().beginTransaction().replace(R.id.container1, new AddRecipeFragment()).commit();

    }

    private void setUpPopupImgClick() {
        ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
                uri -> {
                    // Handle the returned Uri
                    popupRecipeImg.setImageURI(uri);
                    imgUri = uri;

                });
        popupRecipeImg.setOnClickListener(view -> {
            //open gallery when img clicked
            mGetContent.launch("image/*");
        });
    }


    private void iniPopup() {
        popAddRecipe = new Dialog(this);
        popAddRecipe.setContentView(R.layout.popup_add_recipe);
        popAddRecipe.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popAddRecipe.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);
        popAddRecipe.getWindow().getAttributes().gravity = Gravity.TOP;


        //ini popup widgets
        popupRecipeImg = popAddRecipe.findViewById(R.id.popup_recipe_img);
        popupTitle = popAddRecipe.findViewById(R.id.popup_recipe_title);
        popupDesc = popAddRecipe.findViewById(R.id.popup_recipe_description);

        ingredientsLinearLayout = popAddRecipe.findViewById(R.id.ingredientLinearLayout);
        stepsLinearLayout = popAddRecipe.findViewById(R.id.stepsLinearLayout);


        popupProgress = popAddRecipe.findViewById(R.id.popup_progressBar);
        popupAddBtn = popAddRecipe.findViewById(R.id.popup_add);
        addIngredientBtn = popAddRecipe.findViewById(R.id.addIngredientBtn);
        addStepsBtn = popAddRecipe.findViewById(R.id.addStepsBtn);
        rootLinearLayout = popAddRecipe.findViewById(R.id.rootLinearLayout);

        addIngredientBtn.setOnClickListener(view -> {
            EditText textField = new EditText(ingredientsLinearLayout.getContext());
            textField.setMinimumWidth((int) Math.round(rootLinearLayout.getWidth() * 0.5));
            ingredientsLinearLayout.addView(textField);
        });

        addStepsBtn.setOnClickListener(view -> {
            EditText textField = new EditText(stepsLinearLayout.getContext());
            textField.setMinimumWidth((int) Math.round(rootLinearLayout.getWidth() * 0.5));
            stepsLinearLayout.addView(textField);
        });


        // add recipe click listener
        popupAddBtn.setOnClickListener(view -> {
            String recipeName = popupTitle.getText().toString().trim();
            String recipeDescription = popupDesc.getText().toString().trim();

            popupAddBtn.setVisibility(View.INVISIBLE);
            popupProgress.setVisibility(View.VISIBLE);
            List<String> ingredients = new ArrayList<>();

            for (int i = 0; i < ingredientsLinearLayout.getChildCount(); i++) {
                View myView = ingredientsLinearLayout.getChildAt(i);
                if (myView instanceof EditText) {
                    EditText editText = (EditText) myView;

                    ingredients.add(editText.getText().toString());
                }
            }

            List<String> steps = new ArrayList<>();

            for (int i = 0; i < stepsLinearLayout.getChildCount(); i++) {
                View stepView = stepsLinearLayout.getChildAt(i);
                if (stepView instanceof EditText) {
                    EditText editText = (EditText) stepView;

                    steps.add(editText.getText().toString());
                }
            }


            //check all input fields and image
            if (!recipeName.isEmpty()
                    && !recipeDescription.isEmpty()
                    && !ingredients.isEmpty()
                    && !steps.isEmpty()
                    && imgUri != null) {

                //use api to create user recipe
                UserRecipeApi.createUserRecipes(recipeName, recipeDescription, ingredients, steps, imgUri, customTaskResult -> {
                    if (customTaskResult.isSuccessFull()) {

                        Toast.makeText(AddRecipe.this, "Recipe Created", Toast.LENGTH_SHORT).show();
                        popupProgress.setVisibility(View.INVISIBLE);
                        popupAddBtn.setVisibility(View.VISIBLE);
                        popAddRecipe.dismiss();

                    } else {

                        Toast.makeText(AddRecipe.this, customTaskResult.errorMessage(), Toast.LENGTH_LONG).show();
                        popupAddBtn.setVisibility(View.VISIBLE);
                    }

                });

            } else {
                Toast.makeText(AddRecipe.this, "Please verify all input fields and post image", Toast.LENGTH_LONG).show();
                popupAddBtn.setVisibility(View.VISIBLE);
                popupProgress.setVisibility(View.INVISIBLE);
            }
        });

    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}