/*
 * A DCCTech © 2022 - 2023 All Rights Reserved. This copyright notice is the exclusive property of DCCTech and
 * is hereby granted to users for use of DCCTech's intellectual property. Any reproduction, modification, distribution,
 * or other use of DCCTech's intellectual property without prior written consent is strictly prohibited.
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
