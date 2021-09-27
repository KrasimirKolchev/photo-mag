package com.krasimirkolchev.photomag.services;

import com.krasimirkolchev.photomag.models.serviceModels.VoucherServiceModel;

import javax.mail.MessagingException;

public interface DiscountService {
    VoucherServiceModel createVoucher(VoucherServiceModel voucherServiceModel) throws MessagingException;

    boolean isValidVoucher(String voucher);

    VoucherServiceModel getVoucher(String voucherName);

    void useVoucher(String username, VoucherServiceModel voucherServiceModel);
}
