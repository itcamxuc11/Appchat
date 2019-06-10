package com.uiresource.appchat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginMessenger extends BaseActivity {

    EditText username,email,password;
    TextView reset_pass;
    Button btnLogin;

    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setupToolbar(R.id.toolbar, "Messenger");

        auth=FirebaseAuth.getInstance();

        email=(EditText)findViewById(R.id.et_email);
        password= (EditText) findViewById(R.id.et_password);
        reset_pass=(TextView) findViewById(R.id.tv_info_confirm);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        reset_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginMessenger.this,ResetPassMessenger.class));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Xử lý click đăng nhập
                String txt_email=email.getText().toString();
                String txt_password=password.getText().toString();
                if(TextUtils.isEmpty(txt_email)||TextUtils.isEmpty(txt_password)){
                    Toast.makeText(LoginMessenger.this, "Nhập đầy đủ thông tin để đăng nhập.", Toast.LENGTH_SHORT).show();
                }
                else {
                    auth.signInWithEmailAndPassword(txt_email,txt_password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        Intent intent=new Intent(LoginMessenger.this,MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        Intent myIntent = new Intent(LoginMessenger.this, LisstenService.class);
                                        startService(myIntent);
                                        finish();
                                    }
                                    else {
                                        Toast.makeText(LoginMessenger.this,"Đăng nhập thất bại",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

    }
}
