/*
 * A DCCTech © 2022 - 2023 All Rights Reserved. This copyright notice is the exclusive property of DCCTech and
 * is hereby granted to users for use of DCCTech's intellectual property. Any reproduction, modification, distribution,
 * or other use of DCCTech's intellectual property without prior written consent is strictly prohibited.
 */

package io.dcctech.card.flow.security;

import org.springframework.hateoas.Link;

public class SimpleLink {
    private final String href;

    public SimpleLink(String href) {
        this.href = href;
    }

    public SimpleLink(Link link) {
        this.href = link.getHref();
    }

    public String getHref() {
        return href;
    }
}
