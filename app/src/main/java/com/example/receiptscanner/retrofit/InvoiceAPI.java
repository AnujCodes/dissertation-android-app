package com.example.receiptscanner.retrofit;

import com.example.receiptscanner.dto.InvoiceDto;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface InvoiceAPI {
    @POST("vision_v2")
    Call<InvoiceDto> postInvoice();
}
