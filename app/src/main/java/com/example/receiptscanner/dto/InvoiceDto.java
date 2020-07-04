package com.example.receiptscanner.dto;

import java.io.Serializable;

public class InvoiceDto implements Serializable {
    private String invoiceId;
    private String invoiceDate;
    private String vendor;
    private String totalAmount;
    private String category;
    private boolean reimbursable;

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isReimbursable() {
        return reimbursable;
    }

    public void setReimbursable(boolean reimbursable) {
        this.reimbursable = reimbursable;
    }

    public InvoiceDto(String invoiceId, String invoiceDate, String vendor, String totalAmount, String category,
                      boolean reimbursable) {
        super();
        this.invoiceId = invoiceId;
        this.invoiceDate = invoiceDate;
        this.vendor = vendor;
        this.totalAmount = totalAmount;
        this.category = category;
        this.reimbursable = reimbursable;
    }

    public InvoiceDto() {
        super();
    }

}