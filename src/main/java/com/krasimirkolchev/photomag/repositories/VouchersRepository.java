package com.krasimirkolchev.photomag.repositories;

import com.krasimirkolchev.photomag.models.entities.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VouchersRepository extends JpaRepository<Voucher, String> {
    boolean existsByVoucherName(String name);

    Voucher getVoucherByVoucherName(String name);

    List<Voucher> findAllByActiveIsTrue();
}
