package com.example.zhiyue.smartfitnesscenter;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    private EditText etxFontSize;

    private TextView txtPreview;

    private Button btnSaveSetting;

    private SharedPreferences prefs;

    private RadioGroup rdgBMRformula;
    private RadioButton rdbBMRformula;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //get preferences instance
        prefs = getSharedPreferences("BmrSettings", MODE_PRIVATE);

        //get view refs

        etxFontSize = (EditText)findViewById(R.id.etxFontSize);
        txtPreview = (TextView)findViewById(R.id.txtPreview);

        btnSaveSetting = (Button) findViewById(R.id.btnSaveSetting);

        rdgBMRformula =  (RadioGroup) findViewById(R.id.rdgBMRformula);

        etxFontSize.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s)
            {
                int size = 0;

                try
                {
                    size = Integer.parseInt(String.valueOf(s));
                }
                catch(NumberFormatException ex)
                {
                    size = 1;
                }

                size = size < 1 ? 1 : size;

                txtPreview.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
            }
        });

        btnSaveSetting.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                savePrefs();
            }
        });
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        //check if shared prefs exist
        if(prefs.contains("FontSize"))
        {
            etxFontSize.setText(String.valueOf(prefs.getInt("FontSize", 24)));
        }

        if(prefs.contains("Formula"))
        {
            String sFormula = String.valueOf(prefs.getString("Formula", getString(R.string.newFormula)));
            if(((RadioButton) findViewById(R.id.rdbOrg)).getText().equals(sFormula)) {
                ((RadioButton) findViewById(R.id.rdbOrg)).setChecked(true);
            }else if(((RadioButton) findViewById(R.id.rdbHB)).getText().equals(sFormula)) {
                ((RadioButton) findViewById(R.id.rdbHB)).setChecked(true);
            }else{
                ((RadioButton) findViewById(R.id.rdbNew)).setChecked(true);
            }
        }
        else
        {
            ((RadioButton) findViewById(R.id.rdbNew)).setChecked(true);
        }
    }

    @Override
    public void onBackPressed()
    {
        savePrefs();

        super.onBackPressed();
    }

    private void savePrefs()
    {
        //store EditText fields
        String  fontSize = etxFontSize.getText().toString().trim();
        //formula field
        int selectedId = rdgBMRformula.getCheckedRadioButtonId();
        rdbBMRformula = (RadioButton) findViewById(selectedId);
        String sBMRformula = rdbBMRformula.getText().toString().trim();

        //determine if values are empty
        if(fontSize.length() > 0 && sBMRformula.length() > 0)
        {
            //store integer values
            int size = Integer.parseInt(fontSize);

            //get prefs editor
            SharedPreferences.Editor editor = prefs.edit();

            //store values
            editor.putInt("FontSize", (size < 1 ? 1 : size));
            editor.putString("Formula", sBMRformula);

            //write to xml file and note success/failure
            if(editor.commit())
            {
                //display success message
                Toast.makeText(this, "Settings Saved", Toast.LENGTH_SHORT).show();
            }
            else
            {
                //display failure message
                Toast.makeText(this, "An error occurred while saving", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
