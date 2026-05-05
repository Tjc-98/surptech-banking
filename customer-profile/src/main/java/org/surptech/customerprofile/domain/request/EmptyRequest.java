package org.surptech.customerprofile.domain.request;

import lombok.Builder;

@Builder(builderClassName = "Builder", toBuilder = true)
public class EmptyRequest {

    @Override
    public String toString() {
        return "EmptyRequest()";
    }

}
