package com.uiresource.appchat;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterMessenger extends BaseActivity {

    EditText username,email,password;
    Button btnRegiter;
    FirebaseAuth mAuth;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_messenger);

        //implement tu BaseActivity
        setupToolbar(R.id.toolbar, "Messenger");


        username=(EditText)findViewById(R.id.et_username);
        email=(EditText)findViewById(R.id.et_email);
        password= (EditText) findViewById(R.id.et_password);

        btnRegiter= (Button) findViewById(R.id.btnRegiter);


        //
        mAuth=FirebaseAuth.getInstance();
        //
        btnRegiter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txtUsername=username.getText().toString();
                String txtEmail=email.getText().toString();
                String txtPassword=password.getText().toString();
                if(TextUtils.isEmpty(txtUsername)||TextUtils.isEmpty(txtEmail)||TextUtils.isEmpty(txtPassword)){
                    Toast.makeText(RegisterMessenger.this,"All fileds are required",Toast.LENGTH_SHORT).show();
                }else if(txtPassword.length()<6){
                    Toast.makeText(RegisterMessenger.this,"Password must be at least 6 characters",Toast.LENGTH_SHORT).show();
                }else {
                    registerAccount(txtUsername,txtEmail,txtPassword);
                }
            }
        });
    }



    private void registerAccount(final String username, String email, String pasword){
        mAuth.createUserWithEmailAndPassword(email,pasword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser firebaseUser=mAuth.getCurrentUser();
                            String userId=firebaseUser.getUid();

                            reference= FirebaseDatabase.getInstance().getReference("Users").child(userId);

                            HashMap<String,String> hashMap=new HashMap<>();
                            hashMap.put("id",userId);
                            hashMap.put("username",username);
                            hashMap.put("imageURL","default");
                            hashMap.put("status","offline");
                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Intent intent=new Intent(RegisterMessenger.this, LoginMessenger.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });

                        }else{
                            Toast.makeText(RegisterMessenger.this,"You can't Register Account with Email or Password",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
