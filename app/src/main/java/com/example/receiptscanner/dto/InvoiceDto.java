package com.example.receiptscanner.dto;

import java.io.Serializable;

public class InvoiceDto implements Serializable {
    private String invoiceId;
    private String invoiceDate;
    private String vendor;
    private String totalAmount;
    private String category;
    private String currency;
    private String employeeID;
    private boolean reimbursable;

    public String getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(String employeeID) {
        this.employeeID = employeeID;
    }

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

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public InvoiceDto() {
        super();
    }

    public InvoiceDto(String invoiceId, String invoiceDate, String vendor, String totalAmount, String category, String currency, String employeeID, boolean reimbursable) {
        this.invoiceId = invoiceId;
        this.invoiceDate = invoiceDate;
        this.vendor = vendor;
        this.totalAmount = totalAmount;
        this.category = category;
        this.currency = currency;
        this.employeeID = employeeID;
        this.reimbursable = reimbursable;
    }

    @Override
    public String toString() {
        return "InvoiceDto{" +
                "invoiceId='" + invoiceId + '\'' +
                ", invoiceDate='" + invoiceDate + '\'' +
                ", vendor='" + vendor + '\'' +
                ", totalAmount='" + totalAmount + '\'' +
                ", category='" + category + '\'' +
                ", currency='" + currency + '\'' +
                ", employeeID='" + employeeID + '\'' +
                ", reimbursable=" + reimbursable +
                '}';
    }

}