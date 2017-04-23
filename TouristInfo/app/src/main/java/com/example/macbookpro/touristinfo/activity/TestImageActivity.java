package com.example.macbookpro.touristinfo.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.macbookpro.touristinfo.R;

import java.io.File;

public class TestImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_image);
        ImageView testImageView = (ImageView)findViewById(R.id.testImageView);
        Bundle bundle = getIntent().getExtras();
        String src  = bundle.getString("imagepath");
        File imgFile = new  File(src);

        if(imgFile.exists()){

            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

            testImageView.setImageBitmap(myBitmap);

        }
    }
}
