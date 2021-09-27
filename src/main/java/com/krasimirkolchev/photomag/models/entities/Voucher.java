package com.krasimirkolchev.photomag.models.entities;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "vouchers")
public class Voucher extends BaseEntity {
    private String voucherName;
    private List<ProductCategory> categories;
    private List<Brand> brands;
    private Integer discountPercentage;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private boolean isActive;

    public Voucher() {

    }

    @Column(name = "voucher_name", unique = true, nullable = false)
    public String getVoucherName() {
        return voucherName;
    }

    public void setVoucherName(String voucherName) {
        this.voucherName = voucherName;
    }

    @ManyToMany
    public List<ProductCategory> getCategories() {
        return categories;
    }

    public void setCategories(List<ProductCategory> categories) {
        this.categories = categories;
    }

    @ManyToMany
    public List<Brand> getBrands() {
        return brands;
    }

    public void setBrands(List<Brand> brands) {
        this.brands = brands;
    }

    @Column(name = "discount_percentage", nullable = false)
    public Integer getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(Integer discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    @Column(name = "start_date", nullable = false)
    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    @Column(name = "end_date", nullable = false)
    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime durationDays) {
        this.endDate = durationDays;
    }

    @Column(name = "is_active", columnDefinition = "boolean default true")
    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
