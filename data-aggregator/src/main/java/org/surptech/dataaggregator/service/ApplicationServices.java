package org.surptech.dataaggregator.service;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Getter
@Service
public class ApplicationServices {

    @Autowired
    @Qualifier("customerProfileRestClient")
    private RestClient customerProfileRestClient;

    @Autowired
    @Qualifier("creditProfileRestClient")
    private RestClient creditProfileRestClient;

    @Autowired
    @Qualifier("authServerRestClient")
    private RestClient authServerRestClient;
}
