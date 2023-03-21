/*
 * A DCCTech © 2022 - 2023 All Rights Reserved. This copyright notice is the exclusive property of DCCTech and
 * is hereby granted to users for use of DCCTech's intellectual property. Any reproduction, modification, distribution,
 * or other use of DCCTech's intellectual property without prior written consent is strictly prohibited.
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
