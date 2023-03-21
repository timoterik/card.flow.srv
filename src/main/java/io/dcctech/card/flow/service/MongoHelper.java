/*
 * A DCCTech © 2022 - 2023 All Rights Reserved. This copyright notice is the exclusive property of DCCTech and
 * is hereby granted to users for use of DCCTech's intellectual property. Any reproduction, modification, distribution,
 * or other use of DCCTech's intellectual property without prior written consent is strictly prohibited.
 */

package io.dcctech.card.flow.service;

import io.dcctech.card.flow.document.*;
import io.dcctech.card.flow.helper.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.hateoas.Identifiable;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MongoHelper implements InitializingBean {
    private @NotNull MongoTemplate mongoTemplate;

    private final Map<Class, DependencyFinder> dependencyListerMap = new HashMap<>();

    public DocumentCollection findDependencies(Identifiable<String> document) {
        final DocumentCollection dependencies = new DocumentCollection();
        dependencies.add(document);
        while (dependencies.hasUnprocessedDocuments()) {
            dependencies.getUnprocessedTypeStream()
                    .forEach(type -> findDependenciesOfType(type, dependencies));
        }
        return dependencies;
    }

    private <D extends Identifiable<String>> void findDependenciesOfType(Class<D> type, DocumentCollection documentCollection) {
        final DependencyFinder dependencyFinder = dependencyListerMap.get(type);
        final List<D> documents = documentCollection.getUnprocessedDocumentStreamOfType(type).collect(Collectors.toList());
        dependencyFinder.findDependantDocuments(mongoTemplate, documentCollection, documents);
    }

    public <D extends Identifiable<String>> void cascadeDelete(D document) {
        final DocumentCollection dependencies = findDependencies(document);
        dependencies.getTypeStream()
                .flatMap(dependencies::getDocumentStreamOfType)
                .forEach(mongoTemplate::remove);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        dependencyListerMap.put(User.class, new UserDependencyLister());
        dependencyListerMap.put(Image.class, new DefaultDependencyLister<>());
        dependencyListerMap.put(Board.class, new BoardDependencyLister());
        dependencyListerMap.put(CardList.class, new CardListDependencyLister());
        dependencyListerMap.put(Card.class, new DefaultDependencyLister<>());
    }
}
