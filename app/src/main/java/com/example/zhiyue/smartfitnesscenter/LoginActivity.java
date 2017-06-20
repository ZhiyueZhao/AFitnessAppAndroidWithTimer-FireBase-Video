package com.example.zhiyue.smartfitnesscenter;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
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

public class LoginActivity extends AppCompatActivity {

    private EditText etxEmail, etxPass;
    private Button btnSignIn;
    private TextView txvSignUp;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() != null)
        {
            //profile activity
            finish();
            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
        }

        etxEmail = (EditText) findViewById(R.id.etxEmailSI);
        etxPass = (EditText) findViewById(R.id.etxPassSI);

        btnSignIn = (Button) findViewById(R.id.btnSignIn);

        txvSignUp = (TextView)findViewById(R.id.txvSignUp);

        EventHandler eventHandler = new EventHandler();
        btnSignIn.setOnClickListener(eventHandler);
        txvSignUp.setOnClickListener(eventHandler);

        progressDialog = new ProgressDialog(this);
    }

    class EventHandler implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            //determine which View was click
            switch(v.getId())
            {
                case R.id.btnSignIn:
                    userLogin();
                    break;
                case R.id.txvSignUp:
                    finish();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    break;
            }
        }
    }

    private void userLogin(){
        String sEmail = etxEmail.getText().toString().trim();
        String sPass = etxPass.getText().toString().trim();

        if(TextUtils.isEmpty(sEmail)){
            Toast.makeText(this,"Please enter your email", Toast.LENGTH_LONG).show();
        }

        if(TextUtils.isEmpty(sPass)){
            Toast.makeText(this,"Please enter your password", Toast.LENGTH_LONG).show();
        }

        progressDialog.setMessage("Logging in...");
        progressDialog.show();

        mAuth.signInWithEmailAndPassword(sEmail, sPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if(task.isSuccessful()){
                    //redirect to some view
                    finish();
                    startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                }
            }
        });
    }
}
