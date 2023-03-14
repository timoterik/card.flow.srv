/*
 * Copyright © 2022-2023, DCCTech, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package io.dcctech.card.flow.helper;

import io.dcctech.card.flow.document.Board;
import io.dcctech.card.flow.document.CardList;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Collection;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

public class BoardDependencyLister extends AbstractDependencyLister<Board> implements DependencyFinder {

    @Override
    public void addDependenciesToCollection(MongoTemplate mongoTemplate, DocumentCollection dependencyStore, Collection<? extends Board> boards) {

        mongoTemplate.find(query(where("board").in(boards)), CardList.class)
                .forEach(dependencyStore::add);
    }
}
