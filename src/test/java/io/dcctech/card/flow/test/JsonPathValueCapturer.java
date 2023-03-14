/*
 * Copyright © 2022-2023, DCCTech, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package io.dcctech.card.flow.test;

import com.jayway.jsonpath.JsonPath;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultHandler;

public class JsonPathValueCapturer implements ResultHandler {

    private final JsonPath jsonPath;
    private Object value;

    public JsonPathValueCapturer(String jsonPath) {
        this.jsonPath = JsonPath.compile(jsonPath);
    }

    @Override
    public void handle(MvcResult result) throws Exception {
        final String contentAsString = result.getResponse().getContentAsString();
        value = jsonPath.read(contentAsString);
    }

    public <E> E getValue() {
        return (E) value;
    }
}
