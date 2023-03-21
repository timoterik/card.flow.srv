/*
 * A DCCTech © 2022 - 2023 All Rights Reserved. This copyright notice is the exclusive property of DCCTech and
 * is hereby granted to users for use of DCCTech's intellectual property. Any reproduction, modification, distribution,
 * or other use of DCCTech's intellectual property without prior written consent is strictly prohibited.
 */

package io.dcctech.card.flow.helper;

import io.dcctech.card.flow.document.Board;
import io.dcctech.card.flow.document.User;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Collection;
import java.util.Objects;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

public class UserDependencyLister extends AbstractDependencyLister<User> implements DependencyFinder {

    @Override
    public void addDependenciesToCollection(MongoTemplate mongoTemplate, DocumentCollection dependencyStore, Collection<? extends User> users) {
        users.stream()
                .map(User::getImage)
                .filter(Objects::nonNull)
                .collect(MyCollectors.toDocumentCollection(dependencyStore));

        mongoTemplate.find(query(where("memberships.user").in(users)), Board.class)
                .forEach(dependencyStore::add);
    }
}
