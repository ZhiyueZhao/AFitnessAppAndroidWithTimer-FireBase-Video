package com.example.zhiyue.smartfitnesscenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText etxName, etxAge, etxHeight, etxWeight, etxHeartRate;
    private TextView txvGreet;
    private Button btnSignOut, btnSave, btnBMR;
    private RadioGroup rdgGender;
    private RadioButton rdbGender;

    private FirebaseAuth mAuth;

    private DatabaseReference databaseReference;
    private DatabaseReference userProfileRef;

    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
        currentUser = mAuth.getCurrentUser();

        databaseReference = FirebaseDatabase.getInstance().getReference();

        userProfileRef = databaseReference.child(currentUser.getUid().toString());

        //add value change listener to studentIdRef
        userProfileRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("name").getValue()!= null)
                {
                    //get the student's name and program value
                    etxName.setText(dataSnapshot.child("name").getValue().toString());
                    etxAge.setText(dataSnapshot.child("age").getValue().toString());
                    etxHeartRate.setText(dataSnapshot.child("heartRate").getValue().toString());
                    etxHeight.setText(dataSnapshot.child("height").getValue().toString());
                    etxWeight.setText(dataSnapshot.child("weight").getValue().toString());

                    rdbGender = (RadioButton) findViewById(R.id.rdbFemale);
                    if(rdbGender.getText().equals(dataSnapshot.child("gender").getValue().toString())) {
                        rdbGender.setChecked(true);
                    }else {
                        ((RadioButton) findViewById(R.id.rdbMale)).setChecked(true);
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        txvGreet = (TextView) findViewById(R.id.txvGreet);
        btnSignOut = (Button) findViewById(R.id.btnSignOut);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnBMR = (Button) findViewById(R.id.btnBMR);

        etxName = (EditText) findViewById(R.id.etxName);
        etxAge = (EditText) findViewById(R.id.etxAge);
        etxHeight = (EditText) findViewById(R.id.etxHeight);
        etxWeight = (EditText) findViewById(R.id.etxWeight);
        etxHeartRate = (EditText) findViewById(R.id.etxHeartRate);

        rdgGender =  (RadioGroup) findViewById(R.id.rdgGender);

        txvGreet.setText("Welcome " + currentUser.getEmail());
        btnSignOut.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        btnBMR.setOnClickListener(this);
    }

    private void saveUserProfile()
    {
        String sName = etxName.getText().toString().trim();
        int iAge = Integer.parseInt(etxAge.getText().toString());
        int iHeartRate = Integer.parseInt(etxHeartRate.getText().toString());
        double dHeight = Double.parseDouble(etxHeight.getText().toString());
        double dWeight = Double.parseDouble(etxWeight.getText().toString());
        int selectedId = rdgGender.getCheckedRadioButtonId();
        rdbGender = (RadioButton) findViewById(selectedId);
        String sGender = rdbGender.getText().toString().trim();

        UserProfile userProfile = new UserProfile(sName, iAge, iHeartRate, dHeight, dWeight, sGender);

        databaseReference.child(currentUser.getUid()).setValue(userProfile);

        Toast.makeText(this,"Profile saved..", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View v) {

        switch(v.getId())
        {
            case R.id.btnSignOut:
                mAuth.signOut();
                finish();
                startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
                break;
            case R.id.btnSave:
                saveUserProfile();
                break;
            case R.id.btnBMR:
                finish();
                startActivity(new Intent(ProfileActivity.this, BmrActivity.class));
                break;
        }
    }
}
