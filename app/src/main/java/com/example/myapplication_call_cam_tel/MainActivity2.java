package com.example.myapplication_call_cam_tel;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore.Images.Media;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;

public class MainActivity2 extends AppCompatActivity {
    EditText telnum;
    Button btn,btnc,btnnext;
    ImageView img;
    String strImage;
    Uri uriMyImage;
    File photoFile = null;
    private static final String URI_INSTANCE_STATE_KEY = "saved_uri";

    private Uri mImageCaptureUri;
    // Let’s start how can we achieve capturing an image from the camera and getting the file path and load an image in an ImageView
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        //

        img=(ImageView)findViewById(R.id.imageView2);
         //
        Intent intent = getIntent();
        String image_path= intent.getStringExtra("imagePath");
        Uri fileUri = Uri.parse(image_path);
        img.setImageURI(fileUri);
        //

        //
        btn = (Button) findViewById(R.id.button22);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //select picture Gallery
                try {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent,"請點選欲觀看之相片"),2);
                }
                catch (Exception e) {
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                }

            }
        });

    } //end on create
//
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    // TODO Auto-generated method stub
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == RESULT_OK)  {
        //把相片抓出來show在screen
        get_pic(data);
    }
}
//
void get_pic(Intent data)
{
    Uri selectedImageUri = data.getData();
    String filePath = null;

    String wholeID = DocumentsContract.getDocumentId(selectedImageUri);

    String id = wholeID.split(":")[1];

    String[] column = { Media.DATA };

    // where id is equal to
    String sel = Media._ID + "=?";

    Cursor cursor = getContentResolver().query(Media.EXTERNAL_CONTENT_URI,
            column, sel, new String[]{ id }, null);

    int columnIndex = cursor.getColumnIndex(column[0]);

    if (cursor.moveToFirst()) {
        filePath = cursor.getString(columnIndex);
    }
    cursor.close();
    if (filePath != null) {
        decodeFile(filePath);
    } else {
        // bitmap = null;
    }

  }
  //
  public void decodeFile(String filePath) {
      Bitmap bitmap = null;
      bitmap = BitmapFactory.decodeFile(filePath);

      img.setImageBitmap(bitmap);

  }
    //
}