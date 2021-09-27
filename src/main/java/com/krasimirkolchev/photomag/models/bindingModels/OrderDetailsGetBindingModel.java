package com.krasimirkolchev.photomag.models.bindingModels;

public class OrderDetailsGetBindingModel {
    private String addressId;
    private String voucherName;

    public OrderDetailsGetBindingModel() {
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getVoucherName() {
        return voucherName;
    }

    public void setVoucherName(String voucherName) {
        this.voucherName = voucherName;
    }
}
