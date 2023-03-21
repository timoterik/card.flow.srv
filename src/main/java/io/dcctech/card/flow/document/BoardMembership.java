/*
 * A DCCTech © 2022 - 2023 All Rights Reserved. This copyright notice is the exclusive property of DCCTech and
 * is hereby granted to users for use of DCCTech's intellectual property. Any reproduction, modification, distribution,
 * or other use of DCCTech's intellectual property without prior written consent is strictly prohibited.
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
