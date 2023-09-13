package ae.ac.adu.joe.loginandregister.Adapters;

import android.app.Activity;
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
import ae.ac.adu.joe.loginandregister.Activities.Community;
import ae.ac.adu.joe.loginandregister.DisplayPost;
import ae.ac.adu.joe.loginandregister.R;
import apis.AuthApi;
import apis.PostApi;
import apis.UserRecipeApi;
import models.Post;
import models.User;
import models.UserRecipe;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder> {

    Context mContext;
    List<Post> mData ;


    public PostAdapter(Context mContext, List<Post> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int index) {

        View row = LayoutInflater.from(mContext).inflate(R.layout.row_post_item,parent,false);
        return new MyViewHolder(row,index);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        final Post post = mData.get(position);
        holder.tvTitle.setText(post.getTitle());
        Glide.with(mContext).load(post.getImageURL()).into(holder.imgPost);
        Glide.with(mContext).load(post.getUser().getProfile_img()).into(holder.rowPostProfileImg);

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;
        ImageView imgPost;
        CircularImageView rowPostProfileImg;

        public MyViewHolder(View itemView, int index) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.row_post_title);
            imgPost = itemView.findViewById(R.id.row_post_img);
            rowPostProfileImg = itemView.findViewById(R.id.row_post_profile_img);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent displayPost = new Intent(mContext, DisplayPost.class);
                    int position = getAdapterPosition();
                    final Post post = mData.get(position);

                    displayPost.putExtra("title",post.getTitle());
                    displayPost.putExtra("postImage",post.getImageURL());
                    displayPost.putExtra("description",post.getDescription());
                    displayPost.putExtra("post_id",post.getId());
                    final User user = post.getUser();
                    displayPost.putExtra("user", new ArrayList<>(Arrays.asList(user.getId(),user.getFullName(),user.getEmail(),user.getPhoneNumber(),user.getProfile_img())));
                    mContext.startActivity(displayPost);



                }
            });



                itemView.setOnLongClickListener(view -> {

                    final Post post = mData.get(getAdapterPosition());


                    if (AuthApi.isLoggedIn() &&post.getUser().getId().equals(AuthApi.getCurrentUser().getId())) {

                        final Context appContext = mContext.getApplicationContext();
                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                        builder.setTitle("Delete Post")
                                .setMessage("Do you really want to delete post")
                                .setNegativeButton("NO", (dialog, which) -> dialog.dismiss())
                                .setPositiveButton("YES", (dialog, which) -> {

                                    PostApi.deletePost(post.getId(), customTaskResult -> {
                                        if (customTaskResult.isSuccessFull()) {
                                            Toast.makeText(appContext, post.getId() + " has been deleted", Toast.LENGTH_SHORT).show();
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
                       };

                    return true;




                });




        }




    }
}
