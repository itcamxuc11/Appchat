package com.uiresource.appchat.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseUser;
import com.uiresource.appchat.R;
import com.uiresource.appchat.Model.ChatData;
import com.uiresource.appchat.recylcerchat.HolderDate;
import com.uiresource.appchat.recylcerchat.HolderMe;
import com.uiresource.appchat.recylcerchat.HolderYou;

import java.util.List;


//Sending Messenger
public class MessengerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // The items to display in your RecyclerView
    private List<ChatData> items;

    private Context mContext;

    FirebaseUser firebaseUser;

    private final int DATE = 0, YOU = 1, ME = 2;

    // Provide a suitable constructor (depends on the kind of dataset)
    public MessengerAdapter(Context context, List<ChatData> items) {
        this.mContext = context;
        this.items = items;
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return this.items.size();
    }

    @Override
    public int getItemViewType(int position) {
        //More to come
        if (items.get(position).getType().equals("0")) {
            return DATE;
        } else if (items.get(position).getType().equals("1")) {
            return YOU;
        }else if (items.get(position).getType().equals("2")) {
            return ME;
        }
        return -1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        switch (viewType) {
            case DATE:
                View v1 = inflater.inflate(R.layout.layout_holder_date, viewGroup, false);
                viewHolder = new HolderDate(v1);
                break;
            case YOU:
                View v2 = inflater.inflate(R.layout.layout_holder_you, viewGroup, false);
                viewHolder = new HolderYou(v2);
                break;
            default:
                View v = inflater.inflate(R.layout.layout_holder_me, viewGroup, false);
                viewHolder = new HolderMe(v);
                break;
        }
        return viewHolder;
    }
    public void addItem(List<ChatData> item) {
        items.addAll(item);
        notifyDataSetChanged();
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (viewHolder.getItemViewType()) {
            case DATE:
                HolderDate vh1 = (HolderDate) viewHolder;
                configureViewHolder1(vh1, position);
                break;
            case YOU:
                HolderYou vh2 = (HolderYou) viewHolder;
                configureViewHolder2(vh2, position);
                break;
            default:
                HolderMe vh = (HolderMe) viewHolder;
                configureViewHolder3(vh, position);
                break;
        }
    }

    private void configureViewHolder3(HolderMe vh1, int position) {

            ChatData chatData=items.get(position);
            vh1.getTime().setText(items.get(position).getTime());
            vh1.getChatText().setText(items.get(position).getText());
            boolean a=items.get(position).getIsseen();
            if(position==items.size()-1){
                if(chatData.getIsseen()==true){
                    vh1.getIsseen().setText("Đã xem.");
                }
                else {
                    if(chatData.getIsseen()==false){
                        vh1.getIsseen().setText("Đã gửi.");
                    }
                }
            }
            else {
                vh1.getIsseen().setVisibility(View.GONE);
            }

    }

    private void configureViewHolder2(HolderYou vh1, int position) {
            vh1.getTime().setText(items.get(position).getTime());
            vh1.getChatText().setText(items.get(position).getText());

            //vh1.getImage().setImageURI(items.get(position).ge);
    }
    private void configureViewHolder1(HolderDate vh1, int position) {
            vh1.getDate().setText(items.get(position).getText());
    }

}
