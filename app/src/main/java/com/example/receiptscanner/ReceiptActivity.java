package com.example.receiptscanner;

import android.content.Intent;
import android.os.Bundle;

import com.example.receiptscanner.dto.InvoiceDto;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class ReceiptActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private InvoiceDto invoiceDto;
    private EditText editTextInvoiceId;
    private EditText editTextDate;
    private EditText editTextVendorName;
    private EditText editTextQuantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        editTextInvoiceId=(EditText)findViewById(R.id.editTextInvoiceId);
        editTextDate=(EditText)findViewById(R.id.editTextDate);
        editTextVendorName=(EditText)findViewById(R.id.editTextVendorName);
        editTextQuantity=(EditText)findViewById(R.id.editTextQuantity);

        Intent intent = getIntent();
        invoiceDto=(InvoiceDto)intent.getSerializableExtra("invoice");
        populateFormData(invoiceDto);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.categories_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void populateFormData(InvoiceDto invoiceDto){
        editTextInvoiceId.setText(invoiceDto.getInvoiceId());
        editTextDate.setText(invoiceDto.getInvoiceDate());
        editTextVendorName.setText(invoiceDto.getVendor());
        editTextQuantity.setText(invoiceDto.getTotalAmount());
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
