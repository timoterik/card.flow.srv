/*
 * Copyright © 2022-2023, DCCTech, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package io.dcctech.card.flow.security;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.Link;

import java.util.Map;
import java.util.TreeMap;

public class AuthenticatedSessionInfo {

    private final String sessionId;
    private final Map<String, SimpleLink> links = new TreeMap<>();

    public AuthenticatedSessionInfo(String sessionId, Link... links) {
        this.sessionId = sessionId;
        for (Link link : links) {
            this.links.put(link.getRel(), new SimpleLink(link));
        }
    }

    @JsonProperty("_links")
    public Map<String, SimpleLink> getLinks() {
        return links;
    }

    public String getSessionId() {
        return sessionId;
    }

}
