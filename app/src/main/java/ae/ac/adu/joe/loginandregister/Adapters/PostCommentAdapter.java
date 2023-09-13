package ae.ac.adu.joe.loginandregister.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.List;

import ae.ac.adu.joe.loginandregister.R;
import apis.AuthApi;
import models.PostComment;
import models.User;

public class PostCommentAdapter extends RecyclerView.Adapter<PostCommentAdapter.CommentViewHolder> {

    private Context pContext;
    private List<PostComment> pData;

    public PostCommentAdapter(Context pContext, List<PostComment> pData) {
        this.pContext = pContext;
        this.pData = pData;
    }

    @NonNull
    @Override
    public PostCommentAdapter.CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(pContext).inflate(R.layout.row_post_comment,parent,false);
        return new PostCommentAdapter.CommentViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull PostCommentAdapter.CommentViewHolder holder, int position) {
        final PostComment comment = pData.get(position);
        final User user = comment.getUser();
        holder.tv_content.setText(comment.getPostComment());
        holder.tv_username.setText(user.getFullName());

        Glide.with(pContext).load(user.getProfile_img()).into(holder.comment_user_img);

    }

    @Override
    public int getItemCount() {return pData.size();}

    public class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView tv_content, tv_username;
        CircularImageView comment_user_img;
        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_content = itemView.findViewById(R.id.post_comment_content);
            tv_username = itemView.findViewById(R.id.post_comment_username);

            comment_user_img = itemView.findViewById(R.id.post_comment_user_img);

        }
    }
}
