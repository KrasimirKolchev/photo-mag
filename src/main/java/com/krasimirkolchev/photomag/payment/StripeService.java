package com.krasimirkolchev.photomag.payment;

import com.stripe.exception.*;
import com.stripe.model.Charge;

public interface StripeService {

    Charge charge(ChargeRequest chargeRequest)
            throws CardException, APIException, AuthenticationException, InvalidRequestException, APIConnectionException;
}
