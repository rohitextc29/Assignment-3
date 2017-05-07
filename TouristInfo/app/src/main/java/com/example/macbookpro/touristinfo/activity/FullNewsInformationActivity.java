package com.example.macbookpro.touristinfo.activity;

import android.Manifest;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.macbookpro.touristinfo.R;
import com.example.macbookpro.touristinfo.database.DatabaseHelper;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FullNewsInformationActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 0x11;
    private TextView titleTextVew,descriptionTextView;
    private ImageView newsImageView;
    private Button readmoreButton;
    private String title,url,imageurl,description,strTitle;
    private DatabaseHelper mdbhelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_news_information1);
        titleTextVew = (TextView)findViewById(R.id.titleTextVew);
        descriptionTextView = (TextView)findViewById(R.id.descriptionTextView);
        newsImageView = (ImageView)findViewById(R.id.newsImageView);
        readmoreButton = (Button)findViewById(R.id.readmoreButton);



        Bundle bundle = getIntent().getExtras();
        title = bundle.getString("title");
        url = bundle.getString("url");
        imageurl = bundle.getString("imageurl");
        description = bundle.getString("description");
        strTitle = bundle.getString("strTitle");

        titleTextVew.setText(title);
        descriptionTextView.setText(description);
        Picasso.with(FullNewsInformationActivity.this)
                .load(imageurl)
                .into(newsImageView);

        readmoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FullNewsInformationActivity.this,WebViewFullNewsInformationActivity.class);
                intent.putExtra("title",title);
                intent.putExtra("url",url);
                startActivity(intent);
            }
        });

        getSupportActionBar().setTitle(strTitle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_local_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //finish();
                onBackPressed();
                break;
            case R.id.action_save_local:
                checkforExternalPermissions();
                break;
        }
        return true;
    }

    void checkforExternalPermissions(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_CODE);
            }else{
                saveNewsLocally();
            }
        }else{
            saveNewsLocally();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // save file
                try {
                    saveNewsLocally();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                Toast.makeText(getApplicationContext(), "PERMISSION_DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void saveNewsLocally(){
        mdbhelper=new DatabaseHelper(getApplicationContext());
        Picasso.with(this).load(imageurl).into(picassoImageTarget(getApplicationContext(), "imageDir", title.replaceAll(" ","_").trim()+".jpeg"));
    }

    private Target picassoImageTarget(Context context, final String imageDir, final String imageName) {
        Log.d("picassoImageTarget", " picassoImageTarget");
        ContextWrapper cw = new ContextWrapper(context);
        final File directory = cw.getDir(imageDir, Context.MODE_PRIVATE); // path to /data/data/yourapp/app_imageDir
       // final File directory = cw.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        return new Target() {
            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final File myImageFile = new File(directory, imageName); // Create image file
                        FileOutputStream fos = null;
                        try {
                            fos = new FileOutputStream(myImageFile);
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                fos.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        Log.i("image", "image saved to >>>" + myImageFile.getAbsolutePath());
                        mdbhelper.newsLocalDatabase(title,url,description,myImageFile.getAbsolutePath());

                      /*  Intent intent = new Intent(FullNewsInformationActivity.this,TestImageActivity.class);
                        intent.putExtra("imagepath",myImageFile.getAbsolutePath());
                        startActivity(intent);*/
                    }
                }).start();
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
            }
            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                if (placeHolderDrawable != null) {
                }
            }
        };
    }

}
