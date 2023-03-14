/*
 * Copyright © 2022-2023, DCCTech, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
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
