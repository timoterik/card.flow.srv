/*
 * A DCCTech © 2022 - 2023 All Rights Reserved. This copyright notice is the exclusive property of DCCTech and
 * is hereby granted to users for use of DCCTech's intellectual property. Any reproduction, modification, distribution,
 * or other use of DCCTech's intellectual property without prior written consent is strictly prohibited.
 */

package io.dcctech.card.flow.test;


import io.dcctech.card.flow.document.User;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextImpl;

import java.util.ArrayList;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

public class SecurityTestHelper {

    public static Authentication createAuthenticationFor(User user) {
        final TestingAuthenticationToken authentication = new TestingAuthenticationToken(user, null, new ArrayList<>(user.getAuthorities()));
        authentication.setAuthenticated(true);
        return authentication;
    }

    public static Authentication anonymousAuthentication() {
        final TestingAuthenticationToken authentication = new TestingAuthenticationToken(null, null);
        authentication.setAuthenticated(false);
        return authentication;
    }

    public static MockHttpSession createSession(Authentication authentication) {
        final MockHttpSession session = new MockHttpSession();
        session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, new SecurityContextImpl(authentication));
        return session;
    }

    public static MockHttpSession anonymousSession() {
        return createSession(anonymousAuthentication());
    }
}
