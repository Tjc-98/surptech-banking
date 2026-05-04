package org.surptech.customerprofile.exception;

import java.io.Serial;

public class CustomerProfileNotFoundException extends Exception {

    @Serial
    private static final long serialVersionUID = 1001001001001L;

    public CustomerProfileNotFoundException(String message) {
        super(message);
    }

    @Override
    public String toString() {
        return "CustomerProfileNotFoundException: " + getMessage();
    }

}
