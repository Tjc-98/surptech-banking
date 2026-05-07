package org.surptech.customerprofile.service;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.surptech.customerprofile.repository.CustomerProfileRepository;

@Getter
@Service
public class ApplicationServices {

    @Autowired
    private CustomerProfileRepository customerProfileRepository;
}
