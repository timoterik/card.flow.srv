/*
 * Copyright © 2022-2023, DCCTech, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
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
