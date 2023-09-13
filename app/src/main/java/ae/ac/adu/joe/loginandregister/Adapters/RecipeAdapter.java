package ae.ac.adu.joe.loginandregister.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ae.ac.adu.joe.loginandregister.Activities.DisplayRecipe;
import ae.ac.adu.joe.loginandregister.Activities.DisplayRecipeContent;
import ae.ac.adu.joe.loginandregister.R;
import models.Nutrition;
import models.Recipe;
import models.UserRecipe;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.MyViewHolder> {

    Context rContext;
    List<Recipe> rData;

    public RecipeAdapter(Context rContext, List<Recipe> rData) {
        this.rContext = rContext;
        this.rData = rData;
    }

    @NonNull
    @Override
    public RecipeAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int index) {
        View v = LayoutInflater.from(rContext).inflate(R.layout.row_recipe_item,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeAdapter.MyViewHolder holder, int position) {

        Recipe recipe = rData.get(position);
        holder.row2_recipe_title.setText(recipe.getName());
//        holder.tvTitle.setText(recipe.getName());
//        holder.tvDesc.setText(recipe.getDescription());
        Glide.with(rContext).load(recipe.getImg_url()).into(holder.row2_recipe_img);
//
//        for(String ingredient : recipe.getIngredients()){
//            TextView textView = new TextView(holder.ingredientLinearLayout.getContext());
//            textView.setText(ingredient.replaceAll("_"," "));
//            textView.setTextColor(Color.BLACK);
//            holder.ingredientLinearLayout.addView(textView);
//        }
//        for(int i = 0; i< recipe.getSteps().size();i++){
//            String step = recipe.getSteps().get(i);
//
//            TextView textView = new TextView(holder.stepsLinearLayout.getContext());
//            textView.setText((i+1) + ". " + step);
//            textView.setTextColor(Color.BLACK);
//            holder.stepsLinearLayout.addView(textView);
//
//        }
//
//        Nutrition nutrition = recipe.getNutritionInfo();
//
//        LinearLayout carbLinearLayout = new LinearLayout(holder.nutritionLinearLayout.getContext());
//        carbLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
//
//
//        LinearLayout caloriesLinearLayout = new LinearLayout(holder.nutritionLinearLayout.getContext());
//        caloriesLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
//
//
//        LinearLayout sodiumLinearLayout = new LinearLayout(holder.nutritionLinearLayout.getContext());
//        sodiumLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
//
//        LinearLayout proteinLinearLayout = new LinearLayout(holder.nutritionLinearLayout.getContext());
//        proteinLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
//
//        LinearLayout fatLinearLayout = new LinearLayout(holder.nutritionLinearLayout.getContext());
//        fatLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
//
//        LinearLayout cholLinearLayout = new LinearLayout(holder.nutritionLinearLayout.getContext());
//        cholLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
//
//
//
//        TextView tvCarbsLabel = new TextView(carbLinearLayout.getContext());
//        TextView tvCaloriesLabel = new TextView(caloriesLinearLayout.getContext());
//        TextView tvSodiumLabel = new TextView(sodiumLinearLayout.getContext());
//        TextView tvProteinLabel = new TextView(proteinLinearLayout.getContext());
//        TextView tvFatLabel = new TextView(fatLinearLayout.getContext());
//        TextView tvCholLabel = new TextView(cholLinearLayout.getContext());
//
//        TextView tvCarbs = new TextView(carbLinearLayout.getContext());
//        TextView tvCalories = new TextView(caloriesLinearLayout.getContext());
//        TextView tvSodium = new TextView(sodiumLinearLayout.getContext());
//        TextView tvProtein = new TextView(proteinLinearLayout.getContext());
//        TextView tvFat = new TextView(fatLinearLayout.getContext());
//        TextView tvChol = new TextView(cholLinearLayout.getContext());
//
//
//
//        tvCarbsLabel.setText("Carbohydrates: ");
//        tvCarbsLabel.setTextColor(Color.BLACK);
//        tvCaloriesLabel.setText("Calories: ");
//        tvCaloriesLabel.setTextColor(Color.BLACK);
//        tvSodiumLabel.setText( "Sodium: ");
//        tvSodiumLabel.setTextColor(Color.BLACK);
//        tvProteinLabel.setText("Protein: ");
//        tvProteinLabel.setTextColor(Color.BLACK);
//        tvFatLabel.setText("Fat: ");
//        tvFatLabel.setTextColor(Color.BLACK);
//        tvCholLabel.setText( "Cholesterol: ");
//        tvCholLabel.setTextColor(Color.BLACK);
//
//
//        tvCarbs.setText( Long.toString(nutrition.getCarbohydrates()));
//        tvCarbs.setTextColor(Color.BLACK);
//        tvCalories.setText( Long.toString(nutrition.getCalories()));
//        tvCalories.setTextColor(Color.BLACK);
//        tvSodium.setText( Long.toString(nutrition.getSodium()));
//        tvSodium.setTextColor(Color.BLACK);
//        tvProtein.setText( Long.toString(nutrition.getProtein()));
//        tvProtein.setTextColor(Color.BLACK);
//        tvFat.setText( Long.toString(nutrition.getFat()));
//        tvFat.setTextColor(Color.BLACK);
//        tvChol.setText( Long.toString(nutrition.getCholesterol()));
//        tvChol.setTextColor(Color.BLACK);
//
//
//
//
//        carbLinearLayout.addView(tvCarbsLabel);
//        caloriesLinearLayout.addView(tvCaloriesLabel);
//        sodiumLinearLayout.addView(tvSodiumLabel);
//        proteinLinearLayout.addView(tvProteinLabel);
//        fatLinearLayout.addView(tvFatLabel);
//        cholLinearLayout.addView(tvCholLabel);
//
//       carbLinearLayout.addView(tvCarbs);
//        caloriesLinearLayout.addView(tvCalories);
//        sodiumLinearLayout.addView(tvSodium);
//        proteinLinearLayout.addView(tvProtein);
//       fatLinearLayout.addView(tvFat);
//        cholLinearLayout.addView(tvChol);
//
//
//
//        holder.nutritionLinearLayout.addView(carbLinearLayout);
//        holder.nutritionLinearLayout.addView(caloriesLinearLayout);
//        holder.nutritionLinearLayout.addView(sodiumLinearLayout);
//        holder.nutritionLinearLayout.addView(proteinLinearLayout);
//        holder.nutritionLinearLayout.addView(fatLinearLayout);
//        holder.nutritionLinearLayout.addView(cholLinearLayout);
//
//



    }

    @Override
    public int getItemCount() {
        return rData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView row2_recipe_title;
        ImageView row2_recipe_img;
        TextView tvTitle,tvDesc;
        ImageView recipe_img;
        LinearLayout ingredientLinearLayout, stepsLinearLayout, nutritionLinearLayout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            row2_recipe_title = itemView.findViewById(R.id.row2_recipe_title);
            row2_recipe_img = itemView.findViewById(R.id.row2_recipe_img);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent displayRecipeContent = new Intent(rContext, DisplayRecipeContent.class);
                    int position = getAdapterPosition();
                    final Recipe recipe = rData.get(position);
                    final Nutrition nutrition = recipe.getNutritionInfo();
                    displayRecipeContent.putExtra("title",recipe.getName());
                    displayRecipeContent.putExtra("description",recipe.getDescription());
                    displayRecipeContent.putStringArrayListExtra("ingredients", (ArrayList<String>) recipe.getIngredients());
                    displayRecipeContent.putStringArrayListExtra("steps", (ArrayList<String>) recipe.getSteps());
                    displayRecipeContent.putExtra("img_url", recipe.getImg_url());
                    displayRecipeContent.putExtra("calories",nutrition.getCalories());
                    displayRecipeContent.putExtra("carbs",nutrition.getCarbohydrates());
                    displayRecipeContent.putExtra("fat",nutrition.getFat());
                    displayRecipeContent.putExtra("protein",nutrition.getProtein());
                    displayRecipeContent.putExtra("sodium",nutrition.getSodium());
                    displayRecipeContent.putExtra("cholesterol",nutrition.getCholesterol());
                    rContext.startActivity(displayRecipeContent);
                }
            });
//            ingredientLinearLayout = itemView.findViewById(R.id.ingredientLayout);
//            stepsLinearLayout = itemView.findViewById(R.id.stepsLayout);
//            nutritionLinearLayout = itemView.findViewById(R.id.nutritionLayout);
//            recipe_img = itemView.findViewById(R.id.recipe_img);
//
//
//
//            tvTitle = itemView.findViewById(R.id.tvTitle);
//            tvDesc = itemView.findViewById(R.id.tvDesc);

        }
    }



}
