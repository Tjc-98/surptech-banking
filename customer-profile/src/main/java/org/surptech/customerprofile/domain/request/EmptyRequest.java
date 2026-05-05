package org.surptech.customerprofile.domain;

import lombok.Builder;

@Builder(builderClassName = "Builder", toBuilder = true)
public class EmptyRequest {

    @Override
    public String toString() {
        return "EmptyRequest()";
    }

}
