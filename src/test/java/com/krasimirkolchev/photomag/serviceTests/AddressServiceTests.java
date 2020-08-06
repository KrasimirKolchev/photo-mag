package com.krasimirkolchev.photomag.serviceTests;

import com.krasimirkolchev.photomag.models.entities.Address;
import com.krasimirkolchev.photomag.models.serviceModels.AddressServiceModel;
import com.krasimirkolchev.photomag.repositories.AddressRepository;
import com.krasimirkolchev.photomag.services.AddressService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class AddressServiceTests {

    private final AddressService addressService;
    private final AddressRepository addressRepository;

    public AddressServiceTests(AddressService addressService, AddressRepository addressRepository) {
        this.addressService = addressService;
        this.addressRepository = addressRepository;
    }

    @Test
    public void createAddressShouldSaveInDb() {
        AddressServiceModel address = new AddressServiceModel();
        address.setCountry("Bulgaria");
        address.setCity("Razlog");
        address.setStreet("sdasdasdasdasdasdasdas");


        Assert.assertEquals(this.addressService.createAddress(address), address);
    }

}
