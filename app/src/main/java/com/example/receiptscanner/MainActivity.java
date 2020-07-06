package com.example.receiptscanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.receiptscanner.dto.InvoiceDto;
import com.example.receiptscanner.retrofit.InvoiceAPI;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private Button btnCapture;
    private ImageView imgCapture;
    private static final int Image_Capture_Code = 1;
    private static final int PICK_FILE_REQUEST = 2;
    private InvoiceAPI invoiceAPI;
    static final String BASE_URL = "https://dissertation-project.cfapps.us10.hana.ondemand.com/";
    static final String filename = "image.jpeg";
    static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    File file;
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createInvoiceAPI();

        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        }
        btnCapture =(Button)findViewById(R.id.btnTakePicture);
        imgCapture = (ImageView) findViewById(R.id.capturedImage);
        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, "New Picture");
                values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
                imageUri = getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, Image_Capture_Code);

//                Intent cInt = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(cInt,Image_Capture_Code);
            }
        });


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == Image_Capture_Code) {
                try {
                    Bitmap thumbnail = MediaStore.Images.Media.getBitmap(
                            getContentResolver(), imageUri);
                    imgCapture.setImageBitmap(thumbnail);
                    String imageurl = getRealPathFromURI(imageUri);
                    file = getFileFromBitmap(thumbnail);
                }catch (Exception e) {
                    e.printStackTrace();
                }

//                Bitmap bp = (Bitmap) data.getExtras().get("data");
//                file=getFileFromBitmap(bp);
//                imgCapture.setImageBitmap(bp);
            }
            else if(requestCode == PICK_FILE_REQUEST){
                if(data == null){
                    //no data present
                    return;
                }
                try {
                Uri selectedFileUri = data.getData();
                Bitmap thumbnail = MediaStore.Images.Media.getBitmap(
                        getContentResolver(), selectedFileUri);
                imgCapture.setImageBitmap(thumbnail);
                String imageurl = getRealPathFromURI(selectedFileUri);
                file = getFileFromBitmap(thumbnail);

                }catch (Exception e) {
                    e.printStackTrace();
                }

            }
        } else if (resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
        }
    }

    private void createInvoiceAPI(){
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        invoiceAPI = retrofit.create(InvoiceAPI.class);
    }

    Callback<InvoiceDto> postInvoiceCallback = new Callback<InvoiceDto>(){

        @Override
        public void onResponse(Call<InvoiceDto> call, Response<InvoiceDto> response) {
            if(response.isSuccessful()) {
                InvoiceDto invoiceDto = response.body();
                System.out.println("Image successfully scanned");
                System.out.println("Scanned invoice details received");
                System.out.println(invoiceDto.toString());

                Intent myIntent = new Intent(getBaseContext(),   ReceiptActivity.class);
                myIntent.putExtra("invoice",invoiceDto);
                startActivity(myIntent);
            } else {
                System.out.println("Image scan failed");
                System.out.println(response.toString());
                Toast.makeText(getApplicationContext(),"Image scan failed",Toast.LENGTH_SHORT).show();

            }

        }

        @Override
        public void onFailure(Call<InvoiceDto> call, Throwable t) {
            System.out.println("Image scan failed");
            t.printStackTrace();
        }
    };

    //On button click of upload button
    public void onConfirmButtonClick(View view){
        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("imageFile", file.getName(), reqFile);
        invoiceAPI.postInvoiceImage(body).enqueue(postInvoiceCallback);
    }

    public void onUploadFileButtonClick(View view){
        Intent intent = new Intent();
        //sets the select file to all types of files
        intent.setType("*/*");
        //allows to select data and return it
        intent.setAction(Intent.ACTION_GET_CONTENT);
        //starts new activity to select file and return data
        startActivityForResult(Intent.createChooser(intent,"Choose File to Upload.."),PICK_FILE_REQUEST);
    }

    private File getFileFromBitmap(Bitmap bitmap){
        //Convert bitmap to byte array
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bos);
        byte[] bitmapdata = bos.toByteArray();

        //write the bytes in file
        FileOutputStream fos = null;
        File file = null;
        try {
            file = new File(this.getCacheDir(), filename);
            file.createNewFile();
            fos = new FileOutputStream(file);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
}