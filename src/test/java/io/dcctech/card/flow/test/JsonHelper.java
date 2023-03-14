/*
 * Copyright © 2022-2023, DCCTech, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package io.dcctech.card.flow.test;

import com.jayway.jsonpath.JsonPath;
import org.springframework.test.web.servlet.ResultActions;

public class JsonHelper {
    public static <T> T extractJsonPathValue(Object jsonObject, String jsonPath) throws Exception {
        if (jsonObject instanceof ResultActions) {
            ResultActions resultActions = (ResultActions) jsonObject;
            final JsonPathValueCapturer valueCapturer = new JsonPathValueCapturer(jsonPath);
            resultActions.andDo(valueCapturer);
            return valueCapturer.getValue();
        } else {
            final JsonPath path = JsonPath.compile(jsonPath);
            return path.read(jsonObject);
        }
    }
}
