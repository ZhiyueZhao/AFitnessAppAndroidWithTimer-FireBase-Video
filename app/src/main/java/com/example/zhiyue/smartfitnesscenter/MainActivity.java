package com.example.zhiyue.smartfitnesscenter;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private EditText etxEmail, etxPass, etxPassConfirm;
    private Button btnSignUp;
    private TextView txvSignIn;

    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private NavigationView navigation_main_menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDrawerLayout = (DrawerLayout)findViewById(R.id.activity_main);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);

        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigation_main_menu = (NavigationView) findViewById(R.id.navigation_main_menu);

        navigation_main_menu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.nav_tutorial:
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.nav_profile:
                        finish();
                        startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.nav_timer:
                        finish();
                        startActivity(new Intent(MainActivity.this, WorkOutActivity.class));
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.nav_logout:
                        mAuth.signOut();
                        finish();
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        mDrawerLayout.closeDrawers();
                        break;
                }
                return true;
            }
        });

        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() != null)
        {
            //profile activity
            finish();
            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
        }

        etxEmail = (EditText) findViewById(R.id.etxEmailSP);
        etxPass = (EditText) findViewById(R.id.etxPassSP);
        etxPassConfirm = (EditText) findViewById(R.id.etxPassConfirmSP);

        btnSignUp = (Button) findViewById(R.id.btnSignUp);
        txvSignIn = (TextView)findViewById(R.id.txvSignIn);

        progressDialog = new ProgressDialog(this);

        btnSignUp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startRegister();
            }
        });

        txvSignIn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });
    }

    private void startRegister(){
        String sEmail = etxEmail.getText().toString().trim();
        String sPass = etxPass.getText().toString().trim();
        String sPassConfirm = etxPassConfirm.getText().toString().trim();

        if(!sPass.equals(sPassConfirm))
        {
            Toast.makeText(MainActivity.this,"Different password input..", Toast.LENGTH_LONG).show();
        }
        else{
            if(!TextUtils.isEmpty(sEmail) && !TextUtils.isEmpty(sPass) && !TextUtils.isEmpty(sPassConfirm))
            {
                progressDialog.setMessage("Registering...");
                progressDialog.show();

                mAuth.createUserWithEmailAndPassword(sEmail, sPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if(task.isSuccessful()){
                            //redirect to some view
                            finish();
                            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        }else {
                            Toast.makeText(MainActivity.this,"Registration error..", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
