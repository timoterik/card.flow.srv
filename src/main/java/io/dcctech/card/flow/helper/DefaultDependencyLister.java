/*
 * Copyright © 2022-2023, DCCTech, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package io.dcctech.card.flow.helper;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.hateoas.Identifiable;

import java.util.Collection;

public class DefaultDependencyLister<D extends Identifiable<String>> extends AbstractDependencyLister<D> {
    @Override
    public void addDependenciesToCollection(MongoTemplate mongoTemplate, DocumentCollection dependencyStore, Collection documents) {
    }
}
