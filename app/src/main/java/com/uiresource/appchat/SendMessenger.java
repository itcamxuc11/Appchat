package com.uiresource.appchat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.uiresource.appchat.Model.User;
import com.uiresource.appchat.Model.ChatData;
import com.uiresource.appchat.adapter.MessengerAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class SendMessenger extends BaseActivity {

    private RecyclerView mRecyclerView;
    private MessengerAdapter mAdapter;
    private EditText text;
    private Button send;

    CircleImageView user_photo;//menu_userphoto

    List<ChatData> mChat;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    Intent intent;

    ValueEventListener seenListener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sendmessenger);
        //lấy Id user
        intent=getIntent();//dữ liệu từ Content Adapter
        final String userId=intent.getStringExtra("userId");
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        reference=FirebaseDatabase.getInstance().getReference("Users").child(userId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                setupToolbarWithUpNav(R.id.toolbar, user.getUsername(), R.drawable.ic_action_back);
                setData(firebaseUser.getUid(),userId);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mChat=new ArrayList<>();

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));





        text = (EditText) findViewById(R.id.et_message);
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRecyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRecyclerView.smoothScrollToPosition(mRecyclerView.getAdapter().getItemCount() - 1);
                    }
                }, 500);
            }
        });
        send = (Button) findViewById(R.id.bt_send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!text.getText().equals("")){
                    SendMessengerAbout(firebaseUser.getUid(),userId,text.getText().toString());

                    text.setText("");
                }
                else {
                    Toast.makeText(getApplicationContext(), "Bạn không thể để trống tin nhắn.", Toast.LENGTH_SHORT).show();
                    text.setText("");
                }
            }
        });
        seenMessage(userId);


    }
    private  void seenMessage(final String userId){
        reference=FirebaseDatabase.getInstance().getReference("Chats");
        seenListener=reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    ChatData chatData=snapshot.getValue(ChatData.class);
                    if(chatData.getReceiver().equals(firebaseUser.getUid())&&chatData.getSender().equals(userId)){
                        HashMap<String,Object> hashMap=new HashMap<>();
                        hashMap.put("isseen",true);
                        snapshot.getRef().updateChildren(hashMap);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void SendMessengerAbout(String sender, final String receiver, String message){

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference();
        HashMap<String,Object>hashMap=new HashMap<>();
        //Lấy thời gian thực
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        String time = df.format(c.getTime());
        hashMap.put("sender",sender);
        hashMap.put("receiver",receiver);
        hashMap.put("message",message);
        hashMap.put("time",time);
        hashMap.put("isseen",false);
        reference.child("Chats").push().setValue(hashMap);

        //Change some important code
        final DatabaseReference chatRef=FirebaseDatabase.getInstance().getReference("Chatlist")
                .child(firebaseUser.getUid())
                .child(receiver);
        chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()){
                    chatRef.child("id").setValue(receiver);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void setData(final String myId, final String userId){


        reference=FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Chat gui tin nhắn.. type bên MessengerAdapter
                mChat.clear();
                List<ChatData> data = new ArrayList<>();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    ChatData chat=snapshot.getValue(ChatData.class);
                    ChatData item = new ChatData();

                    if(chat.getReceiver().equals(myId)&&chat.getSender().equals(userId)||
                            chat.getReceiver().equals(userId)&&chat.getSender().equals(myId)){
                        item.setText(chat.getMessage());
                        item.setTime(chat.getTime());

                        item.setIsseen(chat.getIsseen());
                        if(chat.getSender().equals(myId)){
                            item.setType("2");
                            // item.setIsseen(false);

                        }
                        else {

                            // if(chat.getReceiver().equals(userId)) {
                            // item.setIsseen(true);
                            item.setType("1");


                        }
                        data.add(item);
                    }



                }
                if(!data.isEmpty()){
                    mAdapter = new MessengerAdapter(getApplicationContext(),data);
                    mRecyclerView.setAdapter(mAdapter);
                    mRecyclerView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mRecyclerView.smoothScrollToPosition(mRecyclerView.getAdapter().getItemCount() - 1);
                        }
                    }, 1000);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        // return data;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_userphoto, menu);
        return true;
    }
    //
    private void status(String status){

        reference=FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        HashMap<String,Object> hashMap= new HashMap<>();
        hashMap.put("status",status);
        reference.updateChildren(hashMap);
    }


    @Override
    public void onResume() {
        super.onResume();
        status("online");
    }

    @Override
    public void onPause() {
        super.onPause();
        reference.removeEventListener(seenListener);
        status("offline");
    }
}
