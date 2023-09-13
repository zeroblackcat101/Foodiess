package models;

import java.util.HashMap;
import java.util.Map;

public class User {

    private String id, fullName, email, phoneNumber,profile_img;


    public User(String id, String fullName, String email, String phoneNumber,String profile_img) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.profile_img = profile_img;
    }

    public static User fromMap(Map<String, Object> data){

        return new User(
                (String)data.get("id"),
                (String)data.get("fullName"),
                (String)data.get("email"),
                (String)data.get("phoneNumber"),
        (String)data.get("img_url")

        );


    }
    public HashMap<String, Object> toMap(){

        final HashMap<String, Object> userDataMap = new HashMap<>();
        userDataMap.put("id",this.id );
        userDataMap.put("fullName",this.fullName );
        userDataMap.put("email", this.email );
        userDataMap.put("phoneNumber",this.phoneNumber);
        userDataMap.put("img_url",this.profile_img);

        return userDataMap;
    }
    public String getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getProfile_img() { return profile_img;}

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", img_url='" + profile_img + '\'' +
                '}';
    }
}
