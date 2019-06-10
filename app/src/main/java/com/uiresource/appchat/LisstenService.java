package com.uiresource.appchat;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.uiresource.appchat.Model.ChatData;
import com.uiresource.appchat.Model.User;


public class LisstenService extends Service {


    String sender;
    public LisstenService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId){
        final MyNotification myNotification = new MyNotification(this);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference ch = database.getReference("Chats");


        Query query = ch.orderByKey().limitToLast(1);
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                    ChatData chat = dataSnapshot.getValue(ChatData.class);
                    if(chat.getReceiver().equals(firebaseUser.getUid()))
                    {
                        getSender(chat.getSender());
                        if(sender!=null)
                        {
                            myNotification.ShowNotification(sender, chat.getMessage());
                        }
                    }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return START_STICKY;
    }

    private void getSender(final String key)
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference us = database.getReference("Users");
        us.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    User user = snapshot.getValue(User.class);
                    if(user.getId().equals(key))
                    {
                        sender = user.getUsername();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
