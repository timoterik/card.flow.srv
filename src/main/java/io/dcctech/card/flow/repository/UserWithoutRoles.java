/*
 * Copyright © 2022-2023, DCCTech, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
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
