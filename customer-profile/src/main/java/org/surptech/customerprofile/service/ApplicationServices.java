package org.surptech.customerprofile.service;

import org.surptech.customerprofile.procedure.BaseProcedure;
import org.surptech.customerprofile.repository.CustomerProfileRepository;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Data
@Service
public class ApplicationServices {

    @Autowired
    CustomerProfileRepository customerProfileRepository;

    @PostConstruct
    public void postConstruct() {
        BaseProcedure.setApplicationServices(this);
    }
}
