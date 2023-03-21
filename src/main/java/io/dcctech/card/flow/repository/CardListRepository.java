/*
 * A DCCTech © 2022 - 2023 All Rights Reserved. This copyright notice is the exclusive property of DCCTech and
 * is hereby granted to users for use of DCCTech's intellectual property. Any reproduction, modification, distribution,
 * or other use of DCCTech's intellectual property without prior written consent is strictly prohibited.
 */

package io.dcctech.card.flow.repository;

import io.dcctech.card.flow.document.Board;
import io.dcctech.card.flow.document.CardList;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface CardListRepository extends MongoRepository<CardList, String> {
    List<CardList> findByBoard(@Param("board") Board board);

    List<CardList> findByArchivedFalseAndBoard(@Param("board") Board board);
}
