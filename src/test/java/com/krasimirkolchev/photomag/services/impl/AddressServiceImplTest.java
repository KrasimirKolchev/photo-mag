package com.krasimirkolchev.photomag.services.impl;

import com.krasimirkolchev.photomag.error.AddressNotFoundException;
import com.krasimirkolchev.photomag.models.entities.Address;
import com.krasimirkolchev.photomag.models.serviceModels.AddressServiceModel;
import com.krasimirkolchev.photomag.repositories.AddressRepository;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.Optional;

@SpringBootTest
class AddressServiceImplTest {
    private AddressRepository addressRepository;
    private AddressServiceImpl addressService;
    private ModelMapper modelMapper;
    private AddressServiceModel address;

    @BeforeEach
    void setUp() {
        ModelMapper mapper = Mockito.mock(ModelMapper.class);
        this.addressRepository = Mockito.mock(AddressRepository.class);
        this.modelMapper = Mockito.mock(ModelMapper.class);

        this.addressService = new AddressServiceImpl(this.addressRepository, this.modelMapper);


        this.address = new AddressServiceModel(){{
            setId("1");
            setCountry("Bulgaria");
            setCity("Razlog");
            setStreet("aaaaaaaaaaaaaaaa aaaaaaaaaaa");
        }};
        Mockito.when(this.addressRepository.findById(any()))
                .thenReturn(Optional.of(new Address(){{
                    setId("1");
                    setCountry("Bulgaria");
                    setCity("Razlog");
                    setStreet("aaaaaaaaaaaaaaaa aaaaaaaaaaa");
                }}));

        Mockito.when(mapper.map(any(AddressServiceModel.class), eq(Address.class)))
                .thenAnswer(invocationOnMock ->
                        mapper.map(invocationOnMock.getArguments()[0], Address.class));
        Mockito.when(mapper.map(any(Address.class), eq(AddressServiceModel.class)))
                .thenAnswer(invocationOnMock ->
                        mapper.map(invocationOnMock.getArguments()[0], AddressServiceModel.class));
    }

    @Test
    void createAddressWhenValidShouldReturnCreatedAddress() {
        Mockito.when(addressRepository.save(any())).thenReturn(any());
        Mockito.when(addressService.createAddress(any())).thenReturn(address);

        AddressServiceModel exp = addressService.createAddress(address);

        assertEquals(exp.getCountry(), address.getCountry());
        assertEquals(exp.getCity(), address.getCity());
        assertEquals(exp.getStreet(), address.getStreet());
    }

    @Test
    void getAddressByIdShouldReturnAddressWhenValid() {
        Mockito.when(this.addressService.getAddressById(any())).thenReturn(this.address);
        AddressServiceModel actual = addressService.getAddressById("1");

        Assert.assertEquals(actual.getId(), address.getId());
        Assert.assertEquals(actual.getCountry(), address.getCountry());
        Assert.assertEquals(actual.getCity(), address.getCity());
        Assert.assertEquals(actual.getStreet(), address.getStreet());
    }

    @Test
    void getAddressByIdShouldThrowWhenInvalid() {
        when(this.addressRepository.findById(any()))
                .thenReturn(Optional.empty());

        assertThrows(
                AddressNotFoundException.class,
                () -> this.addressService.getAddressById("invalid"));
    }

}