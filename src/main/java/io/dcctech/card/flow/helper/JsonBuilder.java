/*
 * A DCCTech © 2022 - 2023 All Rights Reserved. This copyright notice is the exclusive property of DCCTech and
 * is hereby granted to users for use of DCCTech's intellectual property. Any reproduction, modification, distribution,
 * or other use of DCCTech's intellectual property without prior written consent is strictly prohibited.
 */

package io.dcctech.card.flow.helper;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.Closeable;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

public class JsonBuilder implements Closeable {
    private final Writer out;
    private final JsonGenerator generator;

    public JsonBuilder(Writer out) throws IOException {
        this.out = out;
        final JsonFactory jsonFactory = new JsonFactory();
        this.generator = jsonFactory.createGenerator(out);

    }

    public JsonBuilder() throws IOException {
        this(new StringWriter());
    }

    public JsonBuilder field(String fieldName, String value) throws IOException {
        generator.writeStringField(fieldName, value);
        return this;
    }

    public JsonBuilder field(String fieldName, Object value) throws IOException {
        generator.writeObjectField(fieldName, value);
        return this;
    }

    public JsonBuilder field(String fieldName, long value) throws IOException {
        generator.writeNumberField(fieldName, value);
        return this;
    }

    public JsonBuilder field(String fieldName, double value) throws IOException {
        generator.writeNumberField(fieldName, value);
        return this;
    }

    public JsonBuilder arrayFieldStart(String fieldName) throws IOException {
        generator.writeArrayFieldStart(fieldName);
        return this;
    }

    public JsonBuilder arrayEnd() throws IOException {
        generator.writeEndArray();
        return this;
    }

    public JsonBuilder startObject() throws IOException {
        generator.writeStartObject();
        return this;
    }

    public JsonBuilder endObject() throws IOException {
        generator.writeEndObject();
        return this;
    }

    public String build() throws IOException {
        generator.close();
        return out.toString();
    }

    @Override
    public void close() throws IOException {
        generator.close();
    }

}
