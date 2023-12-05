package com.example.myapplication_call_cam_tel;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {
    EditText telnum;
    Button btn,btnc,btnnext;
    ImageView img;
    String strImage;
    Uri uriMyImage;
    File photoFile = null;
    //
    private static final String URI_INSTANCE_STATE_KEY = "saved_uri";
    private Uri mImageCaptureUri;
    // Let’s start how can we achieve capturing an image from the camera and getting the file path and load an image in an ImageView
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //
        telnum = (EditText) findViewById(R.id.editText);
        img=(ImageView)findViewById(R.id.imageView);
        btn = (Button) findViewById(R.id.button);
        btnc=(Button)findViewById(R.id.button2);
        btnnext=(Button)findViewById(R.id.button3);
        //
        btnc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    //
                    requestPermissions(new String[]{Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},2);
                    //
                    return;
                }

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                ////type 1 使用intent return data
                //startActivityForResult(intent,11);

                ////type2 use MediaStore 存在相簿
                // ContentValues value = new ContentValues();
                // value.put(MIME_TYPE, "image/jpeg");
                // uriMyImage= getContentResolver().insert(EXTERNAL_CONTENT_URI,value);
                //  intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, uriMyImage);
                // intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);//授予临时权限
                // startActivityForResult(intent,11);

                //// type3 setup filename
                //// Construct temporary image path and name to save the taken
                //// photo
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    // Error occurred while creating the File
                    ex.printStackTrace();
                }
                Log.d("xxxx",photoFile.getAbsolutePath().toString());
                if (photoFile != null) {
                    Uri photoURI = FileProvider.getUriForFile(MainActivity.this,
                            //BuildConfig.APPLICATION_ID,
                            getPackageName(),
                            //BuildConfig.LIBRARY_PACKAGE_NAME,//
                            photoFile);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(intent,11);
                }
             ////
            }
        });
        //
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //
                String telcall = telnum.getText().toString();
                Uri tel = Uri.parse("tel:" + telcall);
                Intent it = new Intent(Intent.ACTION_CALL, tel);
                if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                  //
                       requestPermissions(new String[]{Manifest.permission.CALL_PHONE},1);
                    //
                    return;
                }
                startActivity(it);
                //
            }
        });
        //
        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent it =new Intent(MainActivity.this,MainActivity2.class);
                it.putExtra("imagePath", mImageCaptureUri.toString());
                startActivity(it);
            }
        });
        //
    } //end on create
//

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //

        //
    }
//

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
      //  if (resultCode == RESULT_OK)  {
            //把相片抓出來show在screen
          //  Log.d("xxx",strImage);

        ////use typ1 intent data
        //  if (data != null && data.getExtras() != null)
        //      dshowimg(data);

        ////use type 2 uri
        //decodeuri(uriMyImage);

        ////use type3 filename
           decodeFile(photoFile);

       // }


    }

//
//type 3 filename
public void decodeFile(File filePath) {


    Bitmap bitmap = null;
    mImageCaptureUri=FileProvider.getUriForFile(this,getPackageName(),photoFile);
    try {
        bitmap = Media.getBitmap(this.getContentResolver(), mImageCaptureUri);
    } catch (IOException e) {
        e.printStackTrace();
    }
    img.setImageBitmap(bitmap);

}
//type2 uri
public void decodeuri(Uri filePath) {

    Bitmap bitmap = null;
    mImageCaptureUri=filePath;
    try {

         bitmap = Media.getBitmap(this.getContentResolver(), filePath);
    } catch (FileNotFoundException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }


    img.setImageBitmap(bitmap);

}
    //type 1 intent data
 public void dshowimg(Intent data)
 {
     Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
     //mImageCaptureUri=Uri.parse(data.getExtras().get("data").toString());
     img.setImageBitmap(imageBitmap);

 }
 //
 private File createImageFile() throws IOException {
     // Create an image file name
     DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
     dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Taipei"));
     String imageFileName= "M_"+dateFormat.format(new Date());
     Log.d("xxx",imageFileName);
     File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
     Log.d("xxx",storageDir.getAbsolutePath().toString());
     File image = File.createTempFile(
             imageFileName,  /* prefix */
             ".jpg",         /* suffix */
             storageDir      /* directory */
     );
     return image;
 }
    //
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save the image capture uri before the activity goes into background
        outState.putParcelable(URI_INSTANCE_STATE_KEY, mImageCaptureUri);
    }
    //
}
