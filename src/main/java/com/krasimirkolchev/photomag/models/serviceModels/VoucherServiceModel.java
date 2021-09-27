package com.krasimirkolchev.photomag.models.serviceModels;

import java.time.LocalDateTime;
import java.util.List;

public class VoucherServiceModel extends BaseServiceModel {
    private String voucherName;
    private List<ProductCategoryServiceModel> categories;
    private List<BrandServiceModel> brands;
    private Integer discountPercentage;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private boolean isActive;

    public VoucherServiceModel() {
    }

    public String getVoucherName() {
        return voucherName;
    }

    public void setVoucherName(String voucherName) {
        this.voucherName = voucherName;
    }

    public List<ProductCategoryServiceModel> getCategories() {
        return categories;
    }

    public void setCategories(List<ProductCategoryServiceModel> categories) {
        this.categories = categories;
    }

    public List<BrandServiceModel> getBrands() {
        return brands;
    }

    public void setBrands(List<BrandServiceModel> brands) {
        this.brands = brands;
    }

    public Integer getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(Integer discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
