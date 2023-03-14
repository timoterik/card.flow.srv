/*
 * Copyright © 2022-2023, DCCTech, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
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
