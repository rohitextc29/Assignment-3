package com.example.macbookpro.touristinfo;

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

/**
 * Created by macbookpro on 12/04/17.
 */

public class SettingActivity extends AppCompatActivity{

    RadioGroup font_setting,background_setting;
    RadioButton selected_font,selected_background;

    RadioButton small,medium,large,light,dark;

    CheckBox checkBox;
    Button save;

    DatabaseHelper mdbhelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_activity);
        mdbhelper=new DatabaseHelper(getApplicationContext());
        SettingItem item = mdbhelper.getSaveSettingData();
        font_setting=(RadioGroup)findViewById(R.id.font_setting);
        background_setting=(RadioGroup)findViewById(R.id.background_setting);
        checkBox=(CheckBox)findViewById(R.id.checkBox);

        small=(RadioButton)findViewById(R.id.small);
        medium=(RadioButton)findViewById(R.id.medium);
        large=(RadioButton)findViewById(R.id.large);
        light=(RadioButton)findViewById(R.id.light);
        dark=(RadioButton)findViewById(R.id.dark);

        if(item.getFont_size().equals(small.getText().toString())){
            small.setChecked(true);
        }else if(item.getFont_size().equals(medium.getText().toString())){
            medium.setChecked(true);
        }else if(item.getFont_size().equals(large.getText().toString())){
            large.setChecked(true);
        }

        if(item.getShow_image().equals("true")){
            checkBox.setChecked(true);
        }else{
            checkBox.setChecked(false);
        }

        if(item.getBackground().equals(light.getText().toString())){
            light.setChecked(true);
        }else if(item.getBackground().equals(dark.getText().toString())){
            dark.setChecked(true);
        }

        save=(Button)findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedfontId=font_setting.getCheckedRadioButtonId();
                selected_font=(RadioButton)findViewById(selectedfontId);
                int selectedbackgroundId=background_setting.getCheckedRadioButtonId();
                selected_background=(RadioButton)findViewById(selectedbackgroundId);
                Toast.makeText(SettingActivity.this,"font-"+selected_font.getText(),Toast.LENGTH_SHORT).show();
                Toast.makeText(SettingActivity.this,"background-"+selected_background.getText(),Toast.LENGTH_SHORT).show();
                Toast.makeText(SettingActivity.this,"show image-"+checkBox.isChecked(),Toast.LENGTH_SHORT).show();
                SettingItem newItem = new SettingItem();
                newItem.setFont_size(selected_font.getText().toString());
                newItem.setBackground(selected_background.getText().toString());
                newItem.setShow_image(checkBox.isChecked()+"");
                System.out.println("Setting font_size "+selected_font.getText().toString());
                System.out.println("Setting background "+selected_background.getText().toString());
                System.out.println("Setting checkBox "+checkBox.isChecked());
                mdbhelper.saveSettingInDatabase(newItem);
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
