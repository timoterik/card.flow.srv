/*
 * Copyright © 2022-2023, DCCTech, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package io.dcctech.card.flow.document;

import org.springframework.data.mongodb.core.mapping.DBRef;

public class BoardMembership {

    @DBRef
    private User user;

    private BoardRole role;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BoardRole getRole() {
        return role;
    }

    public void setRole(BoardRole role) {
        this.role = role;
    }
}
