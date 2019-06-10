package com.uiresource.appchat.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseUser;
import com.uiresource.appchat.Model.Chat;
import com.uiresource.appchat.Model.User;
import com.uiresource.appchat.R;
import com.uiresource.appchat.SendMessenger;

import java.util.List;




public class ChatAdapter extends SelectableAdapter<com.uiresource.appchat.adapter.ChatAdapter.ViewHolder> {

    private List<Chat> mUser;
    private List<User>user;
    private Context mContext;
    private com.uiresource.appchat.adapter.ChatAdapter.ViewHolder.ClickListener clickListener;
    public static final int MSG_TYPE_LEFT=0;
    public static final int MSG_TYPE_RIGHT=1;
    private  final String nameHigh="default";
    private boolean isChat;//Kiem tra online

    FirebaseUser firebaseUser;



    public ChatAdapter (Context context, List<Chat> mUser, com.uiresource.appchat.adapter.ChatAdapter.ViewHolder.ClickListener clickListener) {
        this.mUser = mUser;
        this.mContext = context;
        this.clickListener = clickListener;

    }


    // Create new views
    @Override
    public com.uiresource.appchat.adapter.ChatAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                                    int viewType) {

        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.list_item_chat, null);

        com.uiresource.appchat.adapter.ChatAdapter.ViewHolder viewHolder = new com.uiresource.appchat.adapter.ChatAdapter.ViewHolder(itemLayoutView,clickListener);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(com.uiresource.appchat.adapter.ChatAdapter.ViewHolder viewHolder, int position) {

        //Lấy dữ liệu của FragmentContact đổ len View
        int a=position;
        final Chat user=mUser.get(position);
        viewHolder.tvName.setText(mUser.get(position).getName());
        if (isSelected(position)) {
            viewHolder.checked.setChecked(true);
            viewHolder.checked.setVisibility(View.VISIBLE);
        }else{
            viewHolder.checked.setChecked(false);
            viewHolder.checked.setVisibility(View.GONE);
        }
        viewHolder.tvTime.setText(mUser.get(position).getTime());
        if(user.getmImage().equals(nameHigh)){
            viewHolder.userPhoto.setImageResource(R.drawable.avatar);
        } else {
            Glide.with(mContext).load(user.getmImage()).into(viewHolder.userPhoto);
        }

        if (mUser.get(position).getOnline()){
            viewHolder.onlineView.setVisibility(View.VISIBLE);
        }else
            viewHolder.onlineView.setVisibility(View.INVISIBLE);

        viewHolder.tvLastChat.setText(mUser.get(position).getLastChat());
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
        return mUser .size();
    }



    public static class ViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener,View.OnLongClickListener  {

        public TextView tvName;
        public TextView tvTime;
        public TextView tvLastChat;
        public ImageView userPhoto;
        public boolean online = false;
        private final View onlineView;
        public CheckBox checked;
        private com.uiresource.appchat.adapter.ChatAdapter.ViewHolder.ClickListener listener;
        //private final View selectedOverlay;


        public ViewHolder(View itemLayoutView, com.uiresource.appchat.adapter.ChatAdapter.ViewHolder.ClickListener listener) {
            super(itemLayoutView);

            this.listener = listener;

            tvName = (TextView) itemLayoutView.findViewById(R.id.tv_user_name);
            //selectedOverlay = (View) itemView.findViewById(R.id.selected_overlay);
            tvTime = (TextView) itemLayoutView.findViewById(R.id.tv_time);
            tvLastChat = (TextView) itemLayoutView.findViewById(R.id.tv_last_chat);
            userPhoto = (ImageView) itemLayoutView.findViewById(R.id.iv_user_photo);
            onlineView = (View) itemLayoutView.findViewById(R.id.online_indicator);
            checked = (CheckBox) itemLayoutView.findViewById(R.id.chk_list);

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
