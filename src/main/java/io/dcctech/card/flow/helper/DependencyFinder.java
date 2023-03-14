/*
 * Copyright © 2022-2023, DCCTech, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package io.dcctech.card.flow.helper;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.hateoas.Identifiable;

import java.util.Collection;

public interface DependencyFinder {

    void findDependantDocuments(MongoTemplate mongoTemplate, DocumentCollection dependencyStore, Collection<? extends Identifiable<String>> documents);
}
