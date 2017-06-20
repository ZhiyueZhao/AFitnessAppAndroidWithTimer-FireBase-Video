package com.example.zhiyue.smartfitnesscenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BmrActivity extends MenuActivity {

    private TextView txvGreetBmr, txvBMR;
    private Button btnWorkOut;

    private FirebaseAuth mAuth;

    private DatabaseReference databaseReference;
    private DatabaseReference userProfileRef;

    private FirebaseUser currentUser;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private NavigationView navigation_main_menu;

    private String sFormula = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmr);

        txvGreetBmr = (TextView) findViewById(R.id.txvGreetBmr);
        txvBMR = (TextView) findViewById(R.id.txvBMR);

        //check if shared prefs exist
        if(prefs.contains("FontSize"))
        {
            int size = prefs.getInt("FontSize", 20);

            size = size < 1 ? 20 : size;

            txvBMR.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        }
        sFormula = getString(R.string.newFormula);
        if(prefs.contains("Formula"))
        {
            sFormula = prefs.getString("Formula", getString(R.string.newFormula));
        }

        mDrawerLayout = (DrawerLayout)findViewById(R.id.activity_bmr);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);

        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigation_main_menu = (NavigationView) findViewById(R.id.navigation_bmr_menu);

        navigation_main_menu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.nav_tutorial:
                        mDrawerLayout.closeDrawers();
                        finish();
                        startActivity(new Intent(BmrActivity.this, TutorialActivity.class));
                        break;
                    case R.id.nav_profile:
                        finish();
                        startActivity(new Intent(BmrActivity.this, ProfileActivity.class));
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.nav_timer:
                        finish();
                        startActivity(new Intent(BmrActivity.this, WorkOutActivity.class));
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.nav_logout:
                        mAuth.signOut();
                        finish();
                        startActivity(new Intent(BmrActivity.this, LoginActivity.class));
                        mDrawerLayout.closeDrawers();
                        break;
                }
                return true;
            }
        });

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
                if(dataSnapshot.child("name").getValue()!= null && dataSnapshot.child("age").getValue()!= null &&
                        dataSnapshot.child("height").getValue()!= null && dataSnapshot.child("weight").getValue()!= null &&
                        dataSnapshot.child("gender").getValue()!= null)
                {
                    double BMR = 0;
                    String sFormulaToShow = "";
                    if(sFormula.equals(getString(R.string.newFormula))) {
                        BMR = (10 * Double.parseDouble(dataSnapshot.child("weight").getValue().toString())) +
                                (6.25 * Double.parseDouble(dataSnapshot.child("height").getValue().toString())) -
                                (5 * Double.parseDouble(dataSnapshot.child("age").getValue().toString()));

                        if ("Male".equals(dataSnapshot.child("gender").getValue().toString())) {
                            BMR += 5;
                        } else {
                            BMR -= 161;
                        }

                        sFormulaToShow = "1990 Formula";
                    }
                    else if(sFormula.equals(getString(R.string.orgFormula)))
                    {
                        if ("Male".equals(dataSnapshot.child("gender").getValue().toString())) {
                            BMR = 66.5 + ( 13.75 * Double.parseDouble(dataSnapshot.child("weight").getValue().toString())) +
                                    ( 5.003 * Double.parseDouble(dataSnapshot.child("height").getValue().toString())) -
                                    ( 6.755 * Double.parseDouble(dataSnapshot.child("age").getValue().toString()));
                        } else {
                            BMR = 655.1 + ( 9.563 * Double.parseDouble(dataSnapshot.child("weight").getValue().toString())) +
                                    ( 1.850 * Double.parseDouble(dataSnapshot.child("height").getValue().toString())) -
                                    ( 4.676 * Double.parseDouble(dataSnapshot.child("age").getValue().toString()));
                        }

                        sFormulaToShow = "Original Formula";
                    }
                    else
                    {
                        if ("Male".equals(dataSnapshot.child("gender").getValue().toString())) {
                            BMR = 88.362 + ( 13.397  * Double.parseDouble(dataSnapshot.child("weight").getValue().toString())) +
                                    ( 4.799 * Double.parseDouble(dataSnapshot.child("height").getValue().toString())) -
                                    ( 5.677 * Double.parseDouble(dataSnapshot.child("age").getValue().toString()));
                        } else {
                            BMR = 447.593 + ( 9.247 * Double.parseDouble(dataSnapshot.child("weight").getValue().toString())) +
                                    ( 3.098 * Double.parseDouble(dataSnapshot.child("height").getValue().toString())) -
                                    ( 4.330 * Double.parseDouble(dataSnapshot.child("age").getValue().toString()));
                        }

                        sFormulaToShow = "1984 Formula";
                    }

                    txvGreetBmr.setText("Hello " + dataSnapshot.child("name").getValue().toString() + "!");
                    txvBMR.setText("In " + sFormulaToShow + ", your basal metabolic rate(BMR) is " + BMR +
                            " kilocalories. Which means your Daily kilocalories needed is "
                            + BMR * 1.2 + " without doing any exercise." );
                }else{
                    finish();
                    startActivity(new Intent(BmrActivity.this, ProfileActivity.class));
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        btnWorkOut = (Button)findViewById(R.id.btnWorkOut);
        btnWorkOut.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(BmrActivity.this, WorkOutActivity.class));
            }
        });
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
