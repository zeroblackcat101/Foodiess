package ae.ac.adu.joe.loginandregister.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import ae.ac.adu.joe.loginandregister.R;

public class DisplayRecipeContent extends AppCompatActivity {
    FirebaseFirestore fStore;
    TextView tvTitle,tvDesc;
    ImageView recipe_img;
    LinearLayout ingredientLinearLayout, stepsLinearLayout, nutritionLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_recipe_content);

        // let's set the statue bar to transparent
//        Window w = getWindow();
//        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        tvTitle = findViewById(R.id.tvTitle);
        tvDesc = findViewById(R.id.tvDesc);
        recipe_img = findViewById(R.id.recipe_img);
        ingredientLinearLayout = findViewById(R.id.ingredientLayout);
        stepsLinearLayout = findViewById(R.id.stepsLayout);
        nutritionLinearLayout = findViewById(R.id.nutritionLayout);

        LinearLayout carbLinearLayout = new LinearLayout(nutritionLinearLayout.getContext());
        carbLinearLayout.setOrientation(LinearLayout.HORIZONTAL);


        LinearLayout caloriesLinearLayout = new LinearLayout(nutritionLinearLayout.getContext());
        caloriesLinearLayout.setOrientation(LinearLayout.HORIZONTAL);


        LinearLayout sodiumLinearLayout = new LinearLayout(nutritionLinearLayout.getContext());
        sodiumLinearLayout.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout proteinLinearLayout = new LinearLayout(nutritionLinearLayout.getContext());
        proteinLinearLayout.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout fatLinearLayout = new LinearLayout(nutritionLinearLayout.getContext());
        fatLinearLayout.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout cholLinearLayout = new LinearLayout(nutritionLinearLayout.getContext());
        cholLinearLayout.setOrientation(LinearLayout.HORIZONTAL);



        TextView tvCarbsLabel = new TextView(carbLinearLayout.getContext());
        TextView tvCaloriesLabel = new TextView(caloriesLinearLayout.getContext());
        TextView tvSodiumLabel = new TextView(sodiumLinearLayout.getContext());
        TextView tvProteinLabel = new TextView(proteinLinearLayout.getContext());
        TextView tvFatLabel = new TextView(fatLinearLayout.getContext());
        TextView tvCholLabel = new TextView(cholLinearLayout.getContext());

        TextView tvCarbs = new TextView(carbLinearLayout.getContext());
        TextView tvCalories = new TextView(caloriesLinearLayout.getContext());
        TextView tvSodium = new TextView(sodiumLinearLayout.getContext());
        TextView tvProtein = new TextView(proteinLinearLayout.getContext());
        TextView tvFat = new TextView(fatLinearLayout.getContext());
        TextView tvChol = new TextView(cholLinearLayout.getContext());



        tvCarbsLabel.setText("Carbohydrates: ");
        tvCarbsLabel.setTextColor(Color.BLACK);
        tvCaloriesLabel.setText("Calories: ");
        tvCaloriesLabel.setTextColor(Color.BLACK);
        tvSodiumLabel.setText( "Sodium: ");
        tvSodiumLabel.setTextColor(Color.BLACK);
        tvProteinLabel.setText("Protein: ");
        tvProteinLabel.setTextColor(Color.BLACK);
        tvFatLabel.setText("Fat: ");
        tvFatLabel.setTextColor(Color.BLACK);
        tvCholLabel.setText( "Cholesterol: ");
        tvCholLabel.setTextColor(Color.BLACK);

        fStore = FirebaseFirestore.getInstance();

        Long calories = getIntent().getExtras().getLong("calories");
        tvCalories.setText(Long.toString(calories));
        tvCalories.setTextColor(Color.BLACK);
        Long carbs = getIntent().getExtras().getLong("carbs");
        tvCarbs.setText(Long.toString(carbs));
        tvCarbs.setTextColor(Color.BLACK);
        Long fat = getIntent().getExtras().getLong("fat");
        tvFat.setText(Long.toString(fat));
        tvFat.setTextColor(Color.BLACK);
        Long protein = getIntent().getExtras().getLong("protein");
        tvProtein.setText(Long.toString(protein));
        tvProtein.setTextColor(Color.BLACK);
        Long sodium = getIntent().getExtras().getLong("sodium");
        tvSodium.setText(Long.toString(sodium));
        tvSodium.setTextColor(Color.BLACK);
        Long cholesterol = getIntent().getExtras().getLong("cholesterol");
        tvChol.setText(Long.toString(cholesterol));
        tvChol.setTextColor(Color.BLACK);

        carbLinearLayout.addView(tvCarbsLabel);
        caloriesLinearLayout.addView(tvCaloriesLabel);
        sodiumLinearLayout.addView(tvSodiumLabel);
        proteinLinearLayout.addView(tvProteinLabel);
        fatLinearLayout.addView(tvFatLabel);
        cholLinearLayout.addView(tvCholLabel);

        carbLinearLayout.addView(tvCarbs);
        caloriesLinearLayout.addView(tvCalories);
        sodiumLinearLayout.addView(tvSodium);
        proteinLinearLayout.addView(tvProtein);
        fatLinearLayout.addView(tvFat);
        cholLinearLayout.addView(tvChol);



        nutritionLinearLayout.addView(carbLinearLayout);
        nutritionLinearLayout.addView(caloriesLinearLayout);
        nutritionLinearLayout.addView(sodiumLinearLayout);
        nutritionLinearLayout.addView(proteinLinearLayout);
        nutritionLinearLayout.addView(fatLinearLayout);
        nutritionLinearLayout.addView(cholLinearLayout);


        String recipeImg = getIntent().getExtras().getString("img_url");
        Glide.with(this).load(recipeImg).into(recipe_img);

        String title = getIntent().getExtras().getString("title");
        tvTitle.setText(title);
        String desc = getIntent().getExtras().getString("description");
        tvDesc.setText(desc);
        List<String> ingredients = getIntent().getExtras().getStringArrayList("ingredients");
        for (int i = 0; i < ingredients.size(); i++) {
            TextView textView = new TextView(ingredientLinearLayout.getContext());
            String ingredient = ingredients.get(i);
            textView.setTextColor(Color.BLACK);
            textView.setText(ingredient.replaceAll("_", " "));
            ingredientLinearLayout.addView(textView);
        }
        List<String> steps = getIntent().getExtras().getStringArrayList("steps");
        for (int i = 0; i < steps.size(); i++) {
            String step = steps.get(i);
            TextView textView = new TextView(stepsLinearLayout.getContext());
            textView.setTextColor(Color.BLACK);
            textView.setText((i + 1) + ". " + step);
            stepsLinearLayout.addView(textView);
        }

    }
}