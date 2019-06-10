package com.uiresource.appchat.Model;

/**
 * Created by Dytstudio.
 */

public class Chat {

    private  String userId;
    private String mName;
    private String mLastChat;
    private String mTime;
    private String mImage;
    private boolean online;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getmImage() {
        return mImage;
    }

    public void setmImage(String mImage) {
        this.mImage = mImage;
    }
//   private String imageURL;

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getLastChat() {
        return mLastChat;
    }

    public void setLastChat(String lastChat) {
        mLastChat = lastChat;
    }

    public String getTime() {
        return mTime;
    }

    public void setmTime(String time) {
        mTime = time;
    }




    public boolean getOnline(){
        return online;
    }

    public void setOnline(boolean on){
        online = on;
    }
}