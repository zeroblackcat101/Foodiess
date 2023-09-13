package models;

import java.util.Map;

public class PostComment {
    String user_id, post_id, postComment;
    User user;

    public PostComment(String post_id, String user_id, String postComment,User user) {
        this.post_id = post_id;
        this.user_id = user_id;
        this.postComment = postComment;
        this.user = user;
    }

    public static PostComment fromMap(Map<String,Object> data){
        return new PostComment(
                (String) data.get("post_id"),
                (String) data.get("user_id"),
                (String) data.get("post_comment"),
                User.fromMap((Map<String, Object>) data.get("user"))
        );
    }


    public String getUser_id() {
        return user_id;
    }

    public String getPost_id() {
        return post_id;
    }

    public String getPostComment() {
        return postComment;
    }

    public User getUser() {
        return user;
    }

    @Override
    public String toString() {
        return "PostComment{" +
                "user_id='" + user_id + '\'' +
                ", post_id='" + post_id + '\'' +
                ", postComment='" + postComment + '\'' +
                ", user=" + user +
                '}';
    }
}
