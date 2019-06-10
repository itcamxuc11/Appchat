package com.uiresource.appchat.Model;

public class User {
    private  String id;
    private  String username;
    private  String imageURL;
    private String status;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }





    public String getId() {
        return id;
    }

    public User(String id, String username, String imageURL,String status,String search) {
        this.id = id;
        this.username = username;
        this.imageURL = imageURL;
        this.status=status;

    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }






    public User(){

    }
}
