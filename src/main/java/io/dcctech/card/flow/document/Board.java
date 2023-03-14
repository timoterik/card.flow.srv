/*
 * Copyright © 2022-2023, DCCTech, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package io.dcctech.card.flow.document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.hateoas.Identifiable;

import java.util.List;

@Document
public class Board implements Identifiable<String> {
    @Id
    private String id;
    private String name;
    private boolean archived;
    @Version
    private String version;

    private List<BoardMembership> memberships;

    @JsonIgnore
    public String getId() {
        return id;
    }

    public boolean getArchived() {
        return archived;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<BoardMembership> getMemberships() {
        return memberships;
    }

    public void setMemberships(List<BoardMembership> memberships) {
        this.memberships = memberships;
    }
}
