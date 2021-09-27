package com.krasimirkolchev.photomag.services.impl;

import com.krasimirkolchev.photomag.models.entities.Voucher;
import com.krasimirkolchev.photomag.models.serviceModels.*;
import com.krasimirkolchev.photomag.repositories.VouchersRepository;
import com.krasimirkolchev.photomag.services.CartItemService;
import com.krasimirkolchev.photomag.services.DiscountService;
import com.krasimirkolchev.photomag.services.EmailService;
import com.krasimirkolchev.photomag.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DiscountServiceImpl implements DiscountService {
    private final VouchersRepository vouchersRepository;
    private final UserService userService;
    private final CartItemService cartItemService;
    private final EmailService emailService;
    private final ModelMapper modelMapper;

    @Autowired
    public DiscountServiceImpl(VouchersRepository vouchersRepository, UserService userService,
                               CartItemService cartItemService, EmailService emailService, ModelMapper modelMapper) {
        this.vouchersRepository = vouchersRepository;
        this.userService = userService;
        this.cartItemService = cartItemService;
        this.emailService = emailService;
        this.modelMapper = modelMapper;
    }

    @Override
    public VoucherServiceModel createVoucher(VoucherServiceModel voucherServiceModel) throws MessagingException {
        Voucher voucher = this.vouchersRepository.save(this.modelMapper.map(voucherServiceModel, Voucher.class));
        this.emailService.sendVouchersByEmail(this.modelMapper.map(voucher, VoucherServiceModel.class));
        return this.modelMapper.map(voucher, VoucherServiceModel.class);
    }

    @Override
    public boolean isValidVoucher(String voucher) {
        VoucherServiceModel voucherServiceModel = this.modelMapper
                .map(this.vouchersRepository.getVoucherByVoucherName(voucher), VoucherServiceModel.class);

        return voucherServiceModel.getStartDate().isBefore(LocalDateTime.now())
                && voucherServiceModel.getEndDate().isAfter(LocalDateTime.now());
    }

    @Override
    public VoucherServiceModel getVoucher(String voucherName) {
        return this.modelMapper.map(this.vouchersRepository
                .getVoucherByVoucherName(voucherName), VoucherServiceModel.class);
    }

    @Override
    public void useVoucher(String username, VoucherServiceModel voucherServiceModel) {
        UserServiceModel userServiceModel = this.modelMapper
                .map(this.userService.getUserByUsername(username), UserServiceModel.class);

        List<CartItemServiceModel> items = userServiceModel.getShoppingCart().getItems()
                .stream()
                .filter(i -> {
                    List<String> brands = voucherServiceModel.getBrands().stream()
                            .map(BrandServiceModel::getName)
                            .collect(Collectors.toList());
                    List<String> categories = voucherServiceModel.getCategories().stream()
                            .map(ProductCategoryServiceModel::getName)
                            .collect(Collectors.toList());

                    return brands.contains(i.getItem().getBrand().getName())
                            && categories.contains(i.getItem().getProductCategory().getName());
                })
                .collect(Collectors.toList());

        items.forEach(i -> {
            Double price = i.getItem().getPrice();
            Double newPrice = price - (price * (voucherServiceModel.getDiscountPercentage()) / 100.0);
            i.getItem().setPrice(newPrice);
            this.cartItemService.saveItem(i);
        });


    }

    @Async
    @Scheduled(cron = "0 0 1 * * *")
    void removeOldVouchers() {
        List<Voucher> vouchers = this.vouchersRepository.findAllByActiveIsTrue();

        vouchers.forEach(v -> {
           if (v.getEndDate().isAfter(LocalDateTime.now())) {
               v.setActive(false);
               this.vouchersRepository.save(v);
           }
        });
    }
}
