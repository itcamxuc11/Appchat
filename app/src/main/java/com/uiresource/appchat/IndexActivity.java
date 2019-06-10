package com.uiresource.appchat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class IndexActivity extends BaseActivity {

    Button btnLogin,btnRegister;
    FirebaseUser firebaseUser;

    @Override
    protected void onStart() {
        super.onStart();
        //Lấy User đăng nhập
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        //Nếu tồn tài thì load sang Main không cần Login
        if(firebaseUser!=null){
            Intent intent=new Intent(IndexActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        setupToolbar(R.id.toolbar, "Messenger");


        btnLogin=(Button)findViewById(R.id.btnLogin);
        btnRegister=(Button) findViewById(R.id.btnRegiter);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(IndexActivity.this,LoginMessenger.class));

            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(IndexActivity.this,RegisterMessenger.class));
            }
        });

    }
}
