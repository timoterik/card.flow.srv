/*
 * Copyright © 2022-2023, DCCTech, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package io.dcctech.card.flow.repository.handler;

import io.dcctech.card.flow.document.User;
import io.dcctech.card.flow.document.UserRole;
import io.dcctech.card.flow.service.SecurityService;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@RepositoryEventHandler
@Component
public class UserEventHandler {

    final static SimpleGrantedAuthority ROLE_ADMIN = new SimpleGrantedAuthority("ROLE_ADMIN");

    private final MongoTemplate mongoTemplate;

    private final PasswordEncoder passwordEncoder;

    private final SecurityService securityService;

    public UserEventHandler(MongoTemplate mongoTemplate, PasswordEncoder passwordEncoder, SecurityService securityService) {
        this.mongoTemplate = mongoTemplate;
        this.passwordEncoder = passwordEncoder;
        this.securityService = securityService;
    }

    void encodePassword(User user) {
        final String password = user.getPassword();
        if (password != null && !user.isPasswordEncoded()) {
            final String encodedPassword = passwordEncoder.encode(password);
            user.setPassword(encodedPassword);
        }
    }


    @HandleBeforeSave
    public void handleUserSave(User user) {
        if (!securityService.isAdmin()) {
            if (securityService.getUsername().equals(user.getUsername())) {
                user.getRoles().remove(UserRole.ADMIN);
            } else {
                throw new AccessDeniedException("Not allowed for " + securityService.getUsername() + " to modify other user's data, as this user is not an ADMIN.");
            }
        }

        encodePassword(user);
    }


    @HandleBeforeCreate
    public void handleUserCreate(User user) {
        if (!securityService.isAdmin()) {
            user.getRoles().remove(UserRole.ADMIN);
        }

        encodePassword(user);
    }
}
