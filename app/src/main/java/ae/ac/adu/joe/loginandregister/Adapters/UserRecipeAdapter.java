package ae.ac.adu.joe.loginandregister.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ae.ac.adu.joe.loginandregister.Activities.AddRecipe;
import ae.ac.adu.joe.loginandregister.Activities.DisplayUserRecipe;
import ae.ac.adu.joe.loginandregister.R;
import apis.AuthApi;
import apis.UserRecipeApi;
import models.User;
import models.UserRecipe;

public class UserRecipeAdapter extends RecyclerView.Adapter<UserRecipeAdapter.MyViewHolder> {

    Context mContext;
    List<UserRecipe> rData;


    public UserRecipeAdapter(Context mContext, List<UserRecipe> rData) {
        this.mContext = mContext;
        this.rData = rData;
    }

    @NonNull
    @Override
    public UserRecipeAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int index) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.row_user_recipe_item,parent,false);
        return new UserRecipeAdapter.MyViewHolder(v,index);
    }

    @Override
    public void onBindViewHolder(@NonNull UserRecipeAdapter.MyViewHolder holder, int position) {
        final UserRecipe userRecipe = rData.get(position);
        final User user = userRecipe.getUser();
        holder.row_recipe_title.setText(userRecipe.getTitle());

        Glide.with(mContext).load(userRecipe.getImg_url()).into(holder.row_recipe_img);
        Glide.with(mContext).load(user.getProfile_img()).into(holder.row_recipe_profile_img);


    }

    @Override
    public int getItemCount() {
        return rData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView row_recipe_title;
        ImageView row_recipe_img;
        CircularImageView row_recipe_profile_img;

        public MyViewHolder(@NonNull View itemView, int index) {
            super(itemView);
            row_recipe_title = itemView.findViewById(R.id.row_recipe_title);
            row_recipe_img = itemView.findViewById(R.id.row_recipe_img);
            row_recipe_profile_img = itemView.findViewById(R.id.row_recipe_profile_img);

            itemView.setOnClickListener(view -> {
                Intent displayUserRecipe = new Intent(mContext, DisplayUserRecipe.class);
                int position = getAdapterPosition();
                final UserRecipe userRecipe = rData.get(position);
                final User user = userRecipe.getUser();
                displayUserRecipe.putExtra("user_recipe_id",userRecipe.getUser_recipe_id());
                displayUserRecipe.putExtra("title",userRecipe.getTitle());
                displayUserRecipe.putExtra("description",userRecipe.getDescription());
                displayUserRecipe.putStringArrayListExtra("ingredients", (ArrayList<String>) userRecipe.getIngredients());
                displayUserRecipe.putStringArrayListExtra("steps", (ArrayList<String>) userRecipe.getSteps());
                displayUserRecipe.putExtra("img_url", userRecipe.getImg_url());

                displayUserRecipe.putExtra("user", new ArrayList<>(Arrays.asList(user.getId(),user.getFullName(),user.getEmail(),user.getPhoneNumber(),user.getProfile_img())));

                mContext.startActivity(displayUserRecipe);

            });





                itemView.setOnLongClickListener(view -> {
                    final UserRecipe userRecipe = rData.get(getAdapterPosition());

                    if (AuthApi.isLoggedIn() &&userRecipe.getUser().getId().equals(AuthApi.getCurrentUser().getId()) ) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                        builder.setTitle("Delete Recipe")
                                .setMessage("Do you really want to delete recipe")
                                .setNegativeButton("NO", (dialog, which) -> dialog.dismiss())
                                .setPositiveButton("YES", (dialog, which) -> {

                                    UserRecipeApi.deleteRecipe(userRecipe.getUser_recipe_id(),
                                            customTaskResult -> {

                                                if (customTaskResult.isSuccessFull()) {
                                                    Toast.makeText(mContext.getApplicationContext(), "Recipe is deleted", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                })
                                .setCancelable(false);
                        AlertDialog dialog = builder.create();
                        dialog.setOnShowListener(dialog1 -> {
                            dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                                    .setTextColor(ContextCompat.getColor(mContext, android.R.color.holo_red_dark));
                            dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                                    .setTextColor(ContextCompat.getColor(mContext, R.color.black));
                        });
                        dialog.show();
                    }
                    return true;
                });
            }


        }
    }

