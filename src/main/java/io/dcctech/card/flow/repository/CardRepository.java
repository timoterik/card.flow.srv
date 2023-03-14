/*
 * Copyright © 2022-2023, DCCTech, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package io.dcctech.card.flow.repository;

import io.dcctech.card.flow.document.Card;
import io.dcctech.card.flow.document.CardList;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface CardRepository extends MongoRepository<Card, String> {

    List<Card> findByCardListAndArchivedIsFalseOrderByPos(@Param("cardList") CardList cardList);
}