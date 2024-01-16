package com.example.webview;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class SelectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        Button galleryBtn = findViewById(R.id.galleryBtn);
        galleryBtn.setOnClickListener(view -> {
            Intent i = new Intent(SelectActivity.this, GalleryActivity.class);
            startActivity(i);
        });

        Button cameraBtn = findViewById(R.id.cameraBtn);
        cameraBtn.setOnClickListener(view -> {
            Intent i = new Intent(SelectActivity.this, CameraActivity.class);
            startActivity(i);
        });
    }
}