package org.surptech.customerprofile.service;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.surptech.customerprofile.repository.CustomerProfileRepository;

@Data
@Service
public class ApplicationServices {

    @Autowired
    CustomerProfileRepository customerProfileRepository;
}
