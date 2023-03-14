/*
 * Copyright © 2022-2023, DCCTech, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package io.dcctech.card.flow.test;

public interface FunctionWithExceptions<T, R> {
    R apply(T value) throws Exception;
}
