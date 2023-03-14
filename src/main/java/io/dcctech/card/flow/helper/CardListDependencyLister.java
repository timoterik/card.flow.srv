/*
 * Copyright © 2022-2023, DCCTech, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package io.dcctech.card.flow.helper;

import io.dcctech.card.flow.document.Card;
import io.dcctech.card.flow.document.CardList;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Collection;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

public class CardListDependencyLister extends AbstractDependencyLister<CardList> implements DependencyFinder {

    @Override
    public void addDependenciesToCollection(MongoTemplate mongoTemplate, DocumentCollection dependencyStore, Collection<? extends CardList> cardLists) {

        mongoTemplate.find(query(where("cardList").in(cardLists)), Card.class)
                .forEach(dependencyStore::add);
    }
}
