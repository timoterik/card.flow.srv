/*
 * A DCCTech © 2022 - 2023 All Rights Reserved. This copyright notice is the exclusive property of DCCTech and
 * is hereby granted to users for use of DCCTech's intellectual property. Any reproduction, modification, distribution,
 * or other use of DCCTech's intellectual property without prior written consent is strictly prohibited.
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
