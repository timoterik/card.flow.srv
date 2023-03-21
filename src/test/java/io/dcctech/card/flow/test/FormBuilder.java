/*
 * A DCCTech © 2022 - 2023 All Rights Reserved. This copyright notice is the exclusive property of DCCTech and
 * is hereby granted to users for use of DCCTech's intellectual property. Any reproduction, modification, distribution,
 * or other use of DCCTech's intellectual property without prior written consent is strictly prohibited.
 */

package io.dcctech.card.flow.test;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class FormBuilder {
    private final MultiValueMap<String, String> valueMap = new LinkedMultiValueMap<>();

    private static String urlEncode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

    public FormBuilder property(String name, String value) {
        valueMap.add(name, value);
        return this;
    }

    public FormBuilder property(String name, Object value) {
        property(name, value != null ? value.toString() : null);
        return this;
    }

    public String build() {
        return valueMap.entrySet().stream()
                .flatMap((entry) -> entry.getValue().stream()
                        .map(value -> urlEncode(entry.getKey()) + "=" + urlEncode(value))
                ).collect(Collectors.joining("&"));
    }
}
