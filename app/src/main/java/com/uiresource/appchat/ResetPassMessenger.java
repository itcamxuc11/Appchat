package com.uiresource.appchat;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPassMessenger extends BaseActivity {
    EditText et_email;
    Button reset;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_reset_pass_messenger);
        setupToolbar(R.id.toolbar, "Reset Password");
        et_email=(EditText)findViewById(R.id.et_email);
        reset=(Button)findViewById(R.id.btnReset);
        firebaseAuth=FirebaseAuth.getInstance();
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=et_email.getText().toString();
                if(email.equals("")){
                    Toast.makeText(ResetPassMessenger.this, "Vui lòng nhập email để reset mật khẩu", Toast.LENGTH_SHORT).show();
                }else {
                    firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(ResetPassMessenger.this,"Vui lòng Check Email của bạn",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(ResetPassMessenger.this,LoginMessenger.class));
                            }
                            else {
                                String err=task.getException().getMessage();
                                Toast.makeText(ResetPassMessenger.this,err,Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}
