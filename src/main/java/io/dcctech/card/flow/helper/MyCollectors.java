/*
 * A DCCTech © 2022 - 2023 All Rights Reserved. This copyright notice is the exclusive property of DCCTech and
 * is hereby granted to users for use of DCCTech's intellectual property. Any reproduction, modification, distribution,
 * or other use of DCCTech's intellectual property without prior written consent is strictly prohibited.
 */

package io.dcctech.card.flow.helper;

import org.springframework.hateoas.Identifiable;

import java.util.stream.Collector;

public abstract class MyCollectors {
    public static Collector<Identifiable<String>, DocumentCollection, DocumentCollection> toDocumentCollection(DocumentCollection collection) {
        return Collector.of(() -> collection, DocumentCollection::add, DocumentCollection::addAll);
    }

    public static Collector<Identifiable<String>, DocumentCollection, DocumentCollection> toDocumentCollection() {
        return Collector.of(DocumentCollection::new, DocumentCollection::add, DocumentCollection::addAll);
    }
}
