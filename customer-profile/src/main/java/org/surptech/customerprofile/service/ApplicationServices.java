package org.surptech.customerprofile.service;

import org.surptech.customerprofile.repository.CustomerProfileRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Data
@Service
public class ApplicationServices {

    @Autowired
    CustomerProfileRepository customerProfileRepository;
}
