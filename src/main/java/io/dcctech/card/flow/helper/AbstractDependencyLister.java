/*
 * A DCCTech © 2022 - 2023 All Rights Reserved. This copyright notice is the exclusive property of DCCTech and
 * is hereby granted to users for use of DCCTech's intellectual property. Any reproduction, modification, distribution,
 * or other use of DCCTech's intellectual property without prior written consent is strictly prohibited.
 */

package io.dcctech.card.flow.helper;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.hateoas.Identifiable;

import java.util.Collection;

public abstract class AbstractDependencyLister<D extends Identifiable<String>> implements DependencyFinder {

    @Override
    public void findDependantDocuments(MongoTemplate mongoTemplate, DocumentCollection dependencyStore, Collection<? extends Identifiable<String>> documents) {

        addDependenciesToCollection(mongoTemplate, dependencyStore, (Collection<D>) documents);

        dependencyStore.addAllAsProcessed(documents);
    }

    public abstract void addDependenciesToCollection(MongoTemplate mongoTemplate, DocumentCollection dependencyStore, Collection<? extends D> documents);
}
