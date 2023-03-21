/*
 * A DCCTech © 2022 - 2023 All Rights Reserved. This copyright notice is the exclusive property of DCCTech and
 * is hereby granted to users for use of DCCTech's intellectual property. Any reproduction, modification, distribution,
 * or other use of DCCTech's intellectual property without prior written consent is strictly prohibited.
 */

package io.dcctech.card.flow.repository;

import io.dcctech.card.flow.document.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(excerptProjection = UserWithoutRoles.class)
public interface UserRepository extends MongoRepository<User, String> {
    List<User> findByUsername(@Param("username") String username);

    @Query("{'$text': {'$search' : ?0, '$caseSensitive': false}}")
    List<User> fullText(@Param("query") String query);
}
