package models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Post {
    String id, userID, title, description, imageURL;
    User user;
    List<PostComment> postComments;

    public Post(String id, String userID, String title, String description, String imageURL, List<PostComment> postComments, User user) {
        this.id = id;
        this.userID = userID;
        this.title = title;
        this.description = description;
        this.imageURL = imageURL;
        this.postComments = postComments;
        this.user = user;
    }

    public static Post fromMap(Map<String, Object> data){

        return new Post(
                (String) data.get("post_id"),
                (String) data.get("user_id"),
                (String) data.get("title"),
                (String) data.get("description"),
                (String) data.get("image_url"),
                data.get("post_comments")==null?new ArrayList<>():((List<Map<String, Object>>) data.get("post_comments")).stream().map(e -> PostComment.fromMap(e)).collect(Collectors.toList()),
                User.fromMap((Map<String, Object>)data.get("user"))
                );
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getImageURL() {
        return imageURL;
    }

    public List<PostComment> getPostComments() {
        return postComments;
    }

    public User getUser() {
        return user;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id='" + id + '\'' +
                ", userID='" + userID + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", imageURL='" + imageURL + '\'' +
                ", user=" + user +
                ", postComments=" + postComments +
                '}';
    }
}
