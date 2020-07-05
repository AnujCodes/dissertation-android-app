package com.example.receiptscanner;

import android.content.Intent;
import android.os.Bundle;

import com.example.receiptscanner.dto.InvoiceDto;
import com.example.receiptscanner.retrofit.InvoiceAPI;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ReceiptActivity extends AppCompatActivity {
    private InvoiceDto invoiceDto;
    private EditText editTextInvoiceId;
    private EditText editTextDate;
    private EditText editTextVendorName;
    private EditText editTextTotalAmount;
    private EditText editTextCurrency;
    private EditText editTextEmployeeId;
    private CheckBox checkBoxReimbursable;
    private Spinner spinnerCategory;
    private InvoiceAPI invoiceAPI;

    static final String BASE_URL = "https://dissertation-project.cfapps.us10.hana.ondemand.com/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        createInvoiceAPI();

        editTextInvoiceId=(EditText)findViewById(R.id.editTextInvoiceId);
        editTextDate=(EditText)findViewById(R.id.editTextDate);
        editTextVendorName=(EditText)findViewById(R.id.editTextVendorName);
        editTextTotalAmount =(EditText)findViewById(R.id.editTextTotalAmount);
        editTextCurrency =(EditText)findViewById(R.id.editTextCurrency);
        editTextEmployeeId =(EditText)findViewById(R.id.editTextEmployeeId);
        checkBoxReimbursable =(CheckBox) findViewById(R.id.checkBoxReimbursable);
        editTextEmployeeId =(EditText)findViewById(R.id.editTextEmployeeId);
        spinnerCategory = (Spinner) findViewById(R.id.spinnerCategory);

        Intent intent = getIntent();
        invoiceDto=(InvoiceDto)intent.getSerializableExtra("invoice");
        populateFormData(invoiceDto);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.categories_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);
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

    private void populateFormData(InvoiceDto invoiceDto){
        editTextInvoiceId.setText(invoiceDto.getInvoiceId());
        editTextDate.setText(invoiceDto.getInvoiceDate());
        editTextVendorName.setText(invoiceDto.getVendor());
        editTextTotalAmount.setText(invoiceDto.getTotalAmount());
        editTextCurrency.setText(invoiceDto.getCurrency());
    }

    public void onSaveButtonClicked(View view){
        InvoiceDto invoiceDto = populateInvoiceDTOFromFormData();
        invoiceAPI.postReport(invoiceDto).enqueue(postInvoiceCallback);
    }

    Callback<InvoiceDto> postInvoiceCallback = new Callback<InvoiceDto>(){

        @Override
        public void onResponse(Call<InvoiceDto> call, Response<InvoiceDto> response) {
            if(response.isSuccessful()) {
                InvoiceDto invoiceDto = response.body();
                System.out.println("Report saved successfully");
                System.out.println(invoiceDto.toString());

                Toast.makeText(getApplicationContext(),"Report saved successfully",Toast.LENGTH_SHORT).show();

                Intent myIntent = new Intent(getBaseContext(),   MainActivity.class);
                startActivity(myIntent);
            } else {
                System.out.println("Report save failed");
                System.out.println(response.errorBody());
                Toast.makeText(getApplicationContext(),"Report save failed",Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        public void onFailure(Call<InvoiceDto> call, Throwable t) {
            System.out.println("Report save failed");
            Toast.makeText(getApplicationContext(),"Report save failed",Toast.LENGTH_SHORT).show();
            t.printStackTrace();
        }
    };

    private InvoiceDto populateInvoiceDTOFromFormData(){
        InvoiceDto invoiceDto = new InvoiceDto();
        invoiceDto.setCategory(spinnerCategory.getSelectedItem().toString());
        invoiceDto.setCurrency(editTextCurrency.getText().toString());
        invoiceDto.setEmployeeID(editTextEmployeeId.getText().toString());
        invoiceDto.setInvoiceDate(editTextDate.getText().toString());
        invoiceDto.setInvoiceId(editTextInvoiceId.getText().toString());
        invoiceDto.setReimbursable(checkBoxReimbursable.isChecked());
        invoiceDto.setTotalAmount(editTextTotalAmount.getText().toString());
        invoiceDto.setVendor(editTextVendorName.getText().toString());

        return invoiceDto;

    }
}
