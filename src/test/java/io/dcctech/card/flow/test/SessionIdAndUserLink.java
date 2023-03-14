/*
 * Copyright © 2022-2023, DCCTech, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package io.dcctech.card.flow.test;

public class SessionIdAndUserLink {
    private final String sessionId;
    private final String userHref;

    public SessionIdAndUserLink(String sessionId, String userHref) {
        this.sessionId = sessionId;
        this.userHref = userHref;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getUserHref() {
        return userHref;
    }
}
