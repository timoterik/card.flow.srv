/*
 * A DCCTech © 2022 - 2023 All Rights Reserved. This copyright notice is the exclusive property of DCCTech and
 * is hereby granted to users for use of DCCTech's intellectual property. Any reproduction, modification, distribution,
 * or other use of DCCTech's intellectual property without prior written consent is strictly prohibited.
 */

package io.dcctech.card.flow.helper;

import org.springframework.hateoas.Identifiable;

import java.util.*;
import java.util.stream.Stream;

public class DocumentCollection {
    private final static Comparator<Identifiable<String>> ID_COMPARATOR =
            Comparator.comparing(Identifiable::getId);
    private final Map<Class, Set<Identifiable<String>>> documentListByType = new HashMap<>();
    private final Map<Class, Set<Identifiable<String>>> unprocessedDocumentListByType = new HashMap<>();
    private int unprocessedDocumentCount = 0;

    public <D extends Identifiable<String>> boolean add(D document) {
        return add((Class<D>) document.getClass(), document, false);
    }

    public <D extends Identifiable<String>> boolean addAsProcessed(D document) {
        return add((Class<D>) document.getClass(), document, true);
    }

    public <D extends Identifiable<String>> boolean add(Class<D> type, D document, boolean processed) {
        Set<Identifiable<String>> unprocessedObjects = unprocessedDocumentListByType.get(type);
        if (unprocessedObjects == null) {
            unprocessedObjects = new TreeSet<>(ID_COMPARATOR);
            unprocessedDocumentListByType.put(type, unprocessedObjects);
        }
        Set<Identifiable<String>> objects = documentListByType.get(type);
        if (objects == null) {
            objects = new TreeSet<>(ID_COMPARATOR);
            documentListByType.put(type, objects);
        }
        if (!processed) {
            if (!objects.contains(document) && unprocessedObjects.add(document)) {
                unprocessedDocumentCount++;
                return true;
            }
            return false;
        } else {
            if (unprocessedObjects.remove(document)) {
                unprocessedDocumentCount--;
            }
            return objects.add(document);
        }
    }

    public void addAll(Iterable<? extends Identifiable<String>> documents) {
        documents.forEach(this::add);
    }

    public void addAllAsProcessed(Iterable<? extends Identifiable<String>> documents) {
        documents.forEach(this::addAsProcessed);
    }

    public DocumentCollection addAll(DocumentCollection other) {
        other.getUnprocessedTypeStream()
                .flatMap(other::getUnprocessedDocumentStreamOfType)
                .forEach(doc -> this.add((Identifiable<String>) doc));
        return this;
    }

    public Stream<Class> getUnprocessedTypeStream() {
        return new HashSet<>(unprocessedDocumentListByType.entrySet()).stream()
                .filter(entry -> !entry.getValue().isEmpty())
                .map(entry -> entry.getKey());
    }

    public Stream<Class> getTypeStream() {
        return new HashSet<>(documentListByType.entrySet()).stream()
                .filter(entry -> !entry.getValue().isEmpty())
                .map(entry -> entry.getKey());
    }

    public <D extends Identifiable<String>> Stream<D> getUnprocessedDocumentStreamOfType(Class<D> type) {
        return toStream(unprocessedDocumentListByType.get(type));
    }

    public <D extends Identifiable<String>> Stream<D> getDocumentStreamOfType(Class<D> type) {
        return toStream(documentListByType.get(type));
    }

    private <D extends Identifiable<String>> Stream<D> toStream(Set<Identifiable<String>> documents) {
        if (documents == null) {
            return Stream.empty();
        } else {
            Set<D> setCopy = new TreeSet<>(ID_COMPARATOR);
            setCopy.addAll((Collection<? extends D>) documents);
            return setCopy.stream();
        }
    }

    public boolean hasUnprocessedDocuments() {
        return unprocessedDocumentCount > 0;
    }


}
