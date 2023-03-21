/*
 * A DCCTech © 2022 - 2023 All Rights Reserved. This copyright notice is the exclusive property of DCCTech and
 * is hereby granted to users for use of DCCTech's intellectual property. Any reproduction, modification, distribution,
 * or other use of DCCTech's intellectual property without prior written consent is strictly prohibited.
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
