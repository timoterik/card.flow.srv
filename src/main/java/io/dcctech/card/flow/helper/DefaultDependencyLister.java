/*
 * A DCCTech © 2022 - 2023 All Rights Reserved. This copyright notice is the exclusive property of DCCTech and
 * is hereby granted to users for use of DCCTech's intellectual property. Any reproduction, modification, distribution,
 * or other use of DCCTech's intellectual property without prior written consent is strictly prohibited.
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
