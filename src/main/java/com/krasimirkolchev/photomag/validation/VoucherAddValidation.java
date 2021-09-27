package com.krasimirkolchev.photomag.validation;

import com.krasimirkolchev.photomag.models.bindingModels.VoucherAddBindingModel;
import com.krasimirkolchev.photomag.repositories.VouchersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.krasimirkolchev.photomag.common.CommonMessages.*;
import static com.krasimirkolchev.photomag.common.CommonMessages.VOUCHER_DATE_BEFORE;

@Component
public class VoucherAddValidation implements Validator {
    private final VouchersRepository vouchersRepository;

    @Autowired
    public VoucherAddValidation(VouchersRepository vouchersRepository) {
        this.vouchersRepository = vouchersRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return VoucherAddBindingModel.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        VoucherAddBindingModel voucherAddBindingModel = (VoucherAddBindingModel) target;

        LocalDateTime from = LocalDateTime
                .parse(voucherAddBindingModel.getStartDate() + "T00:00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        LocalDateTime to = LocalDateTime
                .parse(voucherAddBindingModel.getEndDate() + "T23:59:59", DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        if (voucherAddBindingModel.getVoucherName().length() < 4 || voucherAddBindingModel.getVoucherName().length() > 20) {
            errors.rejectValue("voucherName", VOUCHER_NAME_LENGTH, VOUCHER_NAME_LENGTH);
        }
        if (this.vouchersRepository.existsByVoucherName(voucherAddBindingModel.getVoucherName())) {
            errors.rejectValue("voucherName", VOUCHER_EXISTS, VOUCHER_EXISTS);
        }
        if (voucherAddBindingModel.getStartDate().isBlank()) {
            errors.rejectValue("startDate", VOUCHER_SELECT_DATE, VOUCHER_SELECT_DATE);
        }
        if (voucherAddBindingModel.getEndDate().isBlank()) {
            errors.rejectValue("endDate", VOUCHER_SELECT_DATE, VOUCHER_SELECT_DATE);
        }
        if (from.isBefore(LocalDateTime.now())) {
            errors.rejectValue("startDate", VOUCHER_DATE_PAST, VOUCHER_DATE_PAST);
        }
        if (to.isBefore(from)) {
            errors.rejectValue("endDate", VOUCHER_DATE_BEFORE, VOUCHER_DATE_BEFORE);
        }
//        if (to.isAfter(LocalDateTime.now().plusDays(1L))) {
//            errors.rejectValue("endDate", VOUCHER_DATE_AFTER, VOUCHER_DATE_AFTER);
//        }
        if (voucherAddBindingModel.getDiscountPercentage() < 1 || voucherAddBindingModel.getDiscountPercentage() > 99) {
            errors.rejectValue("discountPercentage", VOUCHER_PERCENTS, VOUCHER_PERCENTS);
        }
        if (voucherAddBindingModel.getCategories().isEmpty()) {
            errors.rejectValue("categories", VOUCHER_CATEGORY_ERR, VOUCHER_CATEGORY_ERR);
        }
        if (voucherAddBindingModel.getBrands().isEmpty()) {
            errors.rejectValue("brands", VOUCHER_BRAND_ERR, VOUCHER_BRAND_ERR);
        }
    }
}
