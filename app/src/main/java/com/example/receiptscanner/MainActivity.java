package com.example.receiptscanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.receiptscanner.dto.InvoiceDto;
import com.example.receiptscanner.retrofit.InvoiceAPI;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayOutputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private Button btnCapture;
    private ImageView imgCapture;
    private static final int Image_Capture_Code = 1;
    private InvoiceAPI invoiceAPI;
    static final String BASE_URL = "https://dissertation-project.cfapps.us10.hana.ondemand.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createInvoiceAPI();
        btnCapture =(Button)findViewById(R.id.btnTakePicture);
        imgCapture = (ImageView) findViewById(R.id.capturedImage);
        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cInt = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cInt,Image_Capture_Code);
            }
        });


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Image_Capture_Code) {
            if (resultCode == RESULT_OK) {
                Bitmap bp = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                imgCapture.setImageBitmap(bp);
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            }
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
                System.out.println(invoiceDto.getInvoiceId());

                Intent myIntent = new Intent(getBaseContext(),   ReceiptActivity.class);
                myIntent.putExtra("invoice",invoiceDto);
                startActivity(myIntent);
            } else {
                System.out.println(response.errorBody());
            }

        }

        @Override
        public void onFailure(Call<InvoiceDto> call, Throwable t) {
            t.printStackTrace();
        }
    };

    public void onButtonClick(View view){
        invoiceAPI.postInvoice().enqueue(postInvoiceCallback);
    }
}