/*
 * Copyright © 2022-2023, DCCTech, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
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
