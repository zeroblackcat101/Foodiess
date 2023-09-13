package ae.ac.adu.joe.loginandregister.Activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Arrays;

import ae.ac.adu.joe.loginandregister.R;
import apis.RecipeApi;
import utils.CustomTask;
import utils.CustomTaskResult;

public class dummyCreateRecipe extends AppCompatActivity {
  Button postImg;
    Uri imgUri = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dummy_create_recipe);
        postImg = findViewById(R.id.postBtn);

        setUpPopupImgClick();


    }


    private void setUpPopupImgClick() {
        ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
                uri -> {
                    // Handle the returned Uri

                    imgUri = uri;

                    RecipeApi.createRecipe("Thai Basil Beef",
                            "A speedy, savory Thai beef basil stir fry that’s just a bit spicy and really hits the spot.",
                            "Italian",
                            Arrays.asList("2 tbsp olive_oil",
                                    "1 sweet onion, thinly sliced",
                                    "1 capiscum, thinly sliced",
                                    "6 garlic cloves, minced",
                                    "1lb ground_beef 90/10",
                                    "fresh cilantro (optional)",
                                    "1 tbsp chili_paste",
                                    "2 tbsp soy_sauce",
                                    "1 tbsp fish_sauce",
                                    "1 tbsp brown_sugar",
                                    "2 tbsp fresh lime juice",
                                    "1 cup loosely packed fresh basil leaves, divided"
                                   ),
                            Arrays.asList("In a small bowl combine chili paste, soy sauce, fish sauce, brown sugar and lime juice until incorporated, set aside.",
                                    "Heat oil in a large skillet set over medium high heat. Add the ground beef and cook until browned, breaking it up with a spoon and stirring often, about 6 minutes.",
                                    "Add the capsicum, onion and garlic to the beef and cook until vegetables start to soften, about 5 minutes.",
                                    "Pour the sauce mixture along with the fresh basil (reserving a fresh few leaves for the top) and continue cooking until basil starts to wilt.",
                                    "Serve over rice topped with fresh basil and fresh cilantro. Enjoy!"
                                    ),
                            296,
                            11,
                            18,
                            25,
                            540,
                            70,
                            imgUri,
                            customTaskResult -> {
                                if(customTaskResult.isSuccessFull()){
                                    Toast.makeText(getApplicationContext(), "Successfully Created a recipe", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(getApplicationContext(), customTaskResult.errorMessage(), Toast.LENGTH_SHORT).show();

                                }



                            }
                    );
                });


        postImg.setOnClickListener(view -> {
            //open gallery when img clicked
            mGetContent.launch("image/*");



        });









    }



}