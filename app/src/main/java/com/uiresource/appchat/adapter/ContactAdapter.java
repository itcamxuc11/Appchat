package com.uiresource.appchat.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.uiresource.appchat.Model.Contact;
import com.uiresource.appchat.R;
import com.uiresource.appchat.SendMessenger;

import java.util.List;

/**
 * Created by Dytstudio.
 */

public class ContactAdapter extends SelectableAdapter<ContactAdapter.ViewHolder> {

    private List<Contact> mUser;
    private Context mContext;
    private ContactAdapter.ViewHolder.ClickListener clickListener;
    private  final String nameHigh="default";



    public ContactAdapter (Context mUser, List<Contact> arrayList,ContactAdapter.ViewHolder.ClickListener clickListener) {
        this.mUser = arrayList;
        this.mContext = mUser;


    }

    public ContactAdapter(List<Contact> mUser, Context mContext) {
        this.mUser = mUser;
        this.mContext = mContext;
    }

    // Create new views
    @Override
    public ContactAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {

        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.list_item_contact, null);

        ContactAdapter.ViewHolder viewHolder = new ContactAdapter.ViewHolder(itemLayoutView,clickListener);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        //Lấy dữ liệu của FragmentContact đổ len View
        final Contact user=mUser.get(position);
        viewHolder.tvName.setText(mUser.get(position).getName());

        if(user.getImage().equals(nameHigh)){
            viewHolder.userPhoto.setImageResource(R.drawable.avatar);
        } else {
            Glide.with(mContext).load(user.getImage()).into(viewHolder.userPhoto);
        }
       /* if (mUser.get(position).isOnline()){
            viewHolder.onlineView.setVisibility(View.VISIBLE);
        }else
            viewHolder.onlineView.setVisibility(View.INVISIBLE);*/
        //Click vao User chuyen sang Chat
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mContext, SendMessenger.class);
                intent.putExtra("userId",user.getUserId());
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mUser.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener,View.OnLongClickListener  {

        public TextView tvName;
        public ImageView userPhoto;
        public  View onlineView;
        private ContactAdapter.ViewHolder.ClickListener listener;
        //private final View selectedOverlay;


        public ViewHolder(View itemLayoutView,ContactAdapter.ViewHolder.ClickListener listener) {
            super(itemLayoutView);

            this.listener = listener;

            tvName = (TextView) itemLayoutView.findViewById(R.id.tv_user_name);
            userPhoto = (ImageView) itemLayoutView.findViewById(R.id.iv_user_photo);
            onlineView=(View)itemLayoutView.findViewById(R.id.online_indicator);

            itemLayoutView.setOnClickListener(this);

            itemLayoutView.setOnLongClickListener (this);
        }

        @Override
        public void onClick(View v) {
            if (listener != null) {
                listener.onItemClicked(getAdapterPosition ());
            }
        }
        @Override
        public boolean onLongClick (View view) {
            if (listener != null) {
                return listener.onItemLongClicked(getAdapterPosition ());
            }
            return false;
        }

        public interface ClickListener {
            public void onItemClicked(int position);

            public boolean onItemLongClicked(int position);

            boolean onCreateOptionsMenu(Menu menu);
        }
    }
}
