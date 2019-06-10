package com.uiresource.appchat.recylcerchat;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.uiresource.appchat.R;

/**
 * Created by Dytstudio.
 */

public class HolderMe extends RecyclerView.ViewHolder {

    private TextView time, chatText;
    public TextView isseen;

    public HolderMe(View v) {
        super(v);
        time = (TextView) v.findViewById(R.id.tv_time);
        chatText = (TextView) v.findViewById(R.id.tv_chat_text);
        isseen=(TextView)v.findViewById(R.id.idSeen);
    }

    public TextView getIsseen() {
        return isseen;
    }

    public void setIsseen(TextView isseen) {
        this.isseen = isseen;
    }

    public TextView getTime() {
        return time;
    }

    public void setTime(TextView time) {
        this.time = time;
    }

    public TextView getChatText() {
        return chatText;
    }

    public void setChatText(TextView chatText) {
        this.chatText = chatText;
    }
}
