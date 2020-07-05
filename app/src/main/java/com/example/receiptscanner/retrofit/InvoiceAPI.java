package com.example.receiptscanner.retrofit;

import com.example.receiptscanner.dto.InvoiceDto;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface InvoiceAPI {
    @Multipart
    @POST("image/parse")
    Call<InvoiceDto> postInvoiceImage(@Part MultipartBody.Part image);

    @POST("report")
    Call<InvoiceDto> postReport(@Body InvoiceDto invoiceDto);
}
