/*
 * Copyright © 2022-2023, DCCTech, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package org.springframework.data.rest.webmvc;

import org.springframework.data.repository.support.Repositories;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.data.web.PagedResourcesAssembler;

public class CustomRepositoryEntityController extends RepositoryEntityController {
    public CustomRepositoryEntityController(Repositories repositories, RepositoryRestConfiguration config, RepositoryEntityLinks entityLinks, PagedResourcesAssembler<Object> assembler, HttpHeadersPreparer headersPreparer) {
        super(repositories, config, entityLinks, assembler, headersPreparer);
    }
}
