/*
 * A DCCTech © 2022 - 2023 All Rights Reserved. This copyright notice is the exclusive property of DCCTech and
 * is hereby granted to users for use of DCCTech's intellectual property. Any reproduction, modification, distribution,
 * or other use of DCCTech's intellectual property without prior written consent is strictly prohibited.
 */

package io.dcctech.card.flow.controller;

import io.dcctech.card.flow.document.Image;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.domain.Sort;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.mapping.context.PersistentEntities;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.repository.support.Repositories;
import org.springframework.data.repository.support.RepositoryInvoker;
import org.springframework.data.repository.support.RepositoryInvokerFactory;
import org.springframework.data.rest.core.mapping.ResourceMappings;
import org.springframework.data.rest.core.mapping.ResourceMetadata;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.data.rest.webmvc.CustomRepositoryEntityController;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RootResourceInformation;
import org.springframework.data.rest.webmvc.support.DefaultedPageable;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.hateoas.Resources;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

@RestController
@BasePathAwareController
@RequiredArgsConstructor
public class ImageCollectionController implements InitializingBean {

    private @NotNull MongoTemplate template;
    private @NotNull RepositoryEntityLinks repositoryEntityLinks;
    private @NotNull CustomRepositoryEntityController entityController;
    private @NotNull ResourceMappings resourceMappings;
    private @NotNull Repositories repositories;
    private @NotNull RepositoryInvokerFactory invokerFactory;
    private @NotNull PersistentEntities persistentEntities;

    private ResourceMetadata imageMetadata;
    private PersistentEntity<?, ?> imageEntity;
    private RepositoryInvoker imageRepositoryInvoker;
    private RootResourceInformation resourceInformation;

    @GetMapping("/images")
    public Resources<?> getAllImages(DefaultedPageable defaultedPageable, Sort sort, PersistentEntityResourceAssembler resourceAssembler) throws HttpRequestMethodNotSupportedException {
        return entityController.getCollectionResource(resourceInformation, defaultedPageable, sort, resourceAssembler);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        imageMetadata = resourceMappings.getMetadataFor(Image.class);
        imageEntity = repositories.getPersistentEntity(Image.class);
        imageRepositoryInvoker = invokerFactory.getInvokerFor(Image.class);
        resourceInformation = new RootResourceInformation(imageMetadata, imageEntity, imageRepositoryInvoker);
    }
}
