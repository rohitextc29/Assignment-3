package com.example.macbookpro.touristinfo.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.macbookpro.touristinfo.R;
import com.example.macbookpro.touristinfo.bean.SettingItem;
import com.example.macbookpro.touristinfo.database.DatabaseHelper;

/**
 * Created by macbookpro on 12/04/17.
 */

public class SettingActivity extends AppCompatActivity{

    RadioGroup fontsetting,backgroundsetting;
    RadioButton selectedfont,selectedbackground;

    RadioButton small,medium,large,light,dark;

    CheckBox checkBox;
    Button save;

    DatabaseHelper mdbhelper;
    private SharedPreferences settingPreference;
    private String mypreference = "setting";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_activity);
        mdbhelper=new DatabaseHelper(getApplicationContext());

        small=(RadioButton)findViewById(R.id.small);
        medium=(RadioButton)findViewById(R.id.medium);
        large=(RadioButton)findViewById(R.id.large);
        light=(RadioButton)findViewById(R.id.light);
        dark=(RadioButton)findViewById(R.id.dark);

        settingPreference = getSharedPreferences(mypreference,0);
        String fontsize=settingPreference.getString("fontsize","Medium");
        String showimage=settingPreference.getString("showimage","true");
        String background=settingPreference.getString("background","Light");

        if(fontsize.equalsIgnoreCase(small.getText().toString())){
            small.setChecked(true);
        }else if(fontsize.equalsIgnoreCase(medium.getText().toString())){
            medium.setChecked(true);
        }else if(fontsize.equalsIgnoreCase(large.getText().toString())){
            large.setChecked(true);
        }

        if(showimage.equalsIgnoreCase("true")){
            checkBox.setChecked(true);
        }else{
            checkBox.setChecked(false);
        }

        if(background.equalsIgnoreCase(light.getText().toString())){
            light.setChecked(true);
        }else if(background.equalsIgnoreCase(dark.getText().toString())){
            dark.setChecked(true);
        }

        save=(Button)findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedfontId=fontsetting.getCheckedRadioButtonId();
                selectedfont=(RadioButton)findViewById(selectedfontId);
                int selectedbackgroundId=backgroundsetting.getCheckedRadioButtonId();
                selectedbackground=(RadioButton)findViewById(selectedbackgroundId);
                Toast.makeText(SettingActivity.this,"font-"+selectedfont.getText(),Toast.LENGTH_SHORT).show();
                Toast.makeText(SettingActivity.this,"background-"+selectedbackground.getText(),Toast.LENGTH_SHORT).show();
                Toast.makeText(SettingActivity.this,"show image-"+checkBox.isChecked(),Toast.LENGTH_SHORT).show();
                /*SettingItem newItem = new SettingItem();
                newItem.setFontsize(selectedfont.getText().toString());
                newItem.setBackground(selectedbackground.getText().toString());
                newItem.setShowimage(checkBox.isChecked()+"");
                System.out.println("Setting font_size "+selectedfont.getText().toString());
                System.out.println("Setting background "+selectedbackground.getText().toString());
                System.out.println("Setting checkBox "+checkBox.isChecked());
                mdbhelper.saveSettingInDatabase(newItem);*/

                settingPreference.edit().putString("fontsize",selectedfont.getText().toString()).apply();
                settingPreference.edit().putString("showimage",checkBox.isChecked()+"").apply();
                settingPreference.edit().putString("background",selectedbackground.getText().toString()).apply();

                finish();
            }
        });

        getSupportActionBar().setTitle("Setting");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //finish();
                onBackPressed();
                break;
        }
        return true;
    }

}
