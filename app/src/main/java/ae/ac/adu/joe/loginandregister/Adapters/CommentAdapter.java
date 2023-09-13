package ae.ac.adu.joe.loginandregister.Adapters;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import ae.ac.adu.joe.loginandregister.R;
import apis.AuthApi;
import models.PostComment;
import models.User;
import models.UserRecipeComment;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder>{

    private Context mContext;
    private List<UserRecipeComment> cData;

    public CommentAdapter(Context mContext, List<UserRecipeComment> cData) {
        this.mContext = mContext;
        this.cData = cData;
    }

    @NonNull
    @Override
    public CommentAdapter.CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(mContext).inflate(R.layout.row_comment,parent,false);
        return new CommentViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.CommentViewHolder holder, int position) {

        final UserRecipeComment recipeComment = cData.get(position);
        final User user = recipeComment.getUser();
        holder.tv_content.setText(recipeComment.getRecipeComment());
        holder.tv_username.setText(user.getFullName());
        holder.tv_username.setText(user.getFullName());

        Glide.with(mContext).load(user.getProfile_img()).into(holder.comment_user_img);
    }

    @Override
    public int getItemCount() {
        return cData.size();
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView tv_content, tv_username;
        CircularImageView comment_user_img;
        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_content = itemView.findViewById(R.id.comment_content);
            tv_username = itemView.findViewById(R.id.comment_username);

            comment_user_img = itemView.findViewById(R.id.comment_user_img);
        }
    }

}
