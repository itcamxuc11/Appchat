package com.uiresource.appchat;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.uiresource.appchat.Model.Contact;
import com.uiresource.appchat.adapter.ContactAdapter;
import com.uiresource.appchat.Model.User;

import java.util.ArrayList;
import java.util.List;


public class FragmentContacts extends Fragment implements ContactAdapter.ViewHolder.ClickListener{
    private RecyclerView mRecyclerView;
    private ContactAdapter mAdapter;
    private List<Contact> mUser;
    private  final String nameHigh="default";
    EditText et_search_user;


    public FragmentContacts(){
        setHasOptionsMenu(true);
    }
    public void onCreate(Bundle a){
        super.onCreate(a);
        setHasOptionsMenu(true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts, null, false);

        getActivity().supportInvalidateOptionsMenu();
        ((MainActivity)getActivity()).changeTitle(R.id.toolbar, "Contacts");


        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mUser=new ArrayList<>();
        setData();
        //Tim kiem User
        et_search_user=(EditText)view.findViewById(R.id.et_seach_user);
        et_search_user.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                searchUser(charSequence.toString().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        return view;
    }

    //Ham tìm kiếm User
     private void searchUser(String nameUser){
        final FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
         Query query=FirebaseDatabase.getInstance().getReference("Users").orderByChild("username")
                 .startAt(nameUser)
                 .endAt(nameUser+"\uf8ff");
         query.addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 mUser.clear();
                 for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                     User user=snapshot.getValue(User.class);
                     assert user!=null;
                     assert  firebaseUser!=null;

                     if(!user.getId().equals(firebaseUser.getUid())){

                         Contact contact = new
                                 Contact();
                         contact.setName(user.getUsername());
                         contact.setImage(user.getImageURL());

                         contact.setUserId(user.getId());
                         mUser.add(contact);
                     }

                 }
                 mAdapter = new ContactAdapter(mUser,getContext());
                 mRecyclerView.setAdapter (mAdapter);
             }

             @Override
             public void onCancelled(@NonNull DatabaseError databaseError) {

             }
         });
     }
    //Lấy danh sách tất cả user trong database
    public void setData(){

            final FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
            DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users");

            reference.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(et_search_user.getText().toString().equals("")){
                        mUser.clear();
                        for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                            User user=snapshot.getValue(User.class);
                            assert user!=null;
                            assert  firebaseUser!=null;

                            if(!user.getId().equals(firebaseUser.getUid())){

                                Contact contact = new
                                        Contact();
                                contact.setName(user.getUsername());
                                contact.setImage(user.getImageURL());

                                contact.setUserId(user.getId());
                                mUser.add(contact);
                            }

                        }
                        mAdapter = new ContactAdapter(mUser,getContext());
                        mRecyclerView.setAdapter (mAdapter);

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }




    @Override
    public void onItemClicked (int position) {


    }

    @Override
    public boolean onItemLongClicked (int position) {
        toggleSelection(position);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    private void toggleSelection(int position) {
        mAdapter.toggleSelection (position);
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_add, menu);
    }
}
