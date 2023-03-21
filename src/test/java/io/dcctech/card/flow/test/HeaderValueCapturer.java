/*
 * A DCCTech © 2022 - 2023 All Rights Reserved. This copyright notice is the exclusive property of DCCTech and
 * is hereby granted to users for use of DCCTech's intellectual property. Any reproduction, modification, distribution,
 * or other use of DCCTech's intellectual property without prior written consent is strictly prohibited.
 */

package io.dcctech.card.flow.test;

import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultHandler;

public class HeaderValueCapturer implements ResultHandler {

    private final String headerName;
    private Object value;

    public HeaderValueCapturer(String headerName) {
        this.headerName = headerName;
    }

    @Override
    public void handle(MvcResult result) throws Exception {
        value = result.getResponse().getHeader(headerName);
    }

    public <E> E getValue() {
        return (E) value;
    }
}
