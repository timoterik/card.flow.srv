/*
 * A DCCTech © 2022 - 2023 All Rights Reserved. This copyright notice is the exclusive property of DCCTech and
 * is hereby granted to users for use of DCCTech's intellectual property. Any reproduction, modification, distribution,
 * or other use of DCCTech's intellectual property without prior written consent is strictly prohibited.
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
