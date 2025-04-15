package com.example.questions_5;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class MainActivity extends Activity {

    Button btnCapture, btnChooseFolder;
    GridView gridView;
    String currentPhotoPath;
    final int REQUEST_IMAGE_CAPTURE = 1;
    final int REQUEST_FOLDER_PICK = 2;
    File photoFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnCapture = findViewById(R.id.btnCapture);
        btnChooseFolder = findViewById(R.id.btnChooseFolder);
        gridView = findViewById(R.id.gridView);

        checkPermissions();

        btnCapture.setOnClickListener(v -> dispatchTakePictureIntent());

        btnChooseFolder.setOnClickListener(v -> openFolderPicker());
    }

    void checkPermissions() {
        String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        List<String> list = new ArrayList<>();
        for (String p : permissions) {
            if (ContextCompat.checkSelfPermission(this, p) != PackageManager.PERMISSION_GRANTED) {
                list.add(p);
            }
        }
        if (!list.isEmpty()) {
            ActivityCompat.requestPermissions(this, list.toArray(new String[0]), 100);
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        getApplicationContext().getPackageName() + ".provider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp;
        File storageDir = new File(Environment.getExternalStorageDirectory(), "MyPhotos");

        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }

        File image = new File(storageDir, imageFileName + ".jpg");
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void openFolderPicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        startActivityForResult(intent, REQUEST_FOLDER_PICK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_FOLDER_PICK && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            File folder = new File(FileUtil.getFullPathFromTreeUri(uri, this));
            loadImagesFromFolder(folder);
        }
    }

    private void loadImagesFromFolder(File folder) {
        File[] files = folder.listFiles();
        List<Bitmap> bitmaps = new ArrayList<>();

        if (files != null) {
            List<String> imagePaths = new ArrayList<>();
            for (File file : files) {
                if (file.getName().endsWith(".jpg")) {
                    imagePaths.add(file.getAbsolutePath());
                }
            }

            gridView.setAdapter(new ImageAdapter(this, imagePaths));
        }
    }
}
