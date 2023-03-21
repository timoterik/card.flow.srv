/*
 * A DCCTech © 2022 - 2023 All Rights Reserved. This copyright notice is the exclusive property of DCCTech and
 * is hereby granted to users for use of DCCTech's intellectual property. Any reproduction, modification, distribution,
 * or other use of DCCTech's intellectual property without prior written consent is strictly prohibited.
 */

package io.dcctech.card.flow.repository;

import io.dcctech.card.flow.document.Image;
import io.dcctech.card.flow.document.User;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "UserWithoutRoles", types = User.class)
public interface UserWithoutRoles {
    String getUsername();

    String getFirstName();

    String getLastName();

    Image getImage();
}
