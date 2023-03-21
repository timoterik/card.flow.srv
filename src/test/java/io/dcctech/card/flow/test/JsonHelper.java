/*
 * A DCCTech © 2022 - 2023 All Rights Reserved. This copyright notice is the exclusive property of DCCTech and
 * is hereby granted to users for use of DCCTech's intellectual property. Any reproduction, modification, distribution,
 * or other use of DCCTech's intellectual property without prior written consent is strictly prohibited.
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
