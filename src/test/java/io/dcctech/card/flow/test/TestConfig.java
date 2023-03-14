/*
 * Copyright © 2022-2023, DCCTech, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package io.dcctech.card.flow.test;

import io.dcctech.card.flow.controller.ImageController;
import io.dcctech.card.flow.repository.UserRepository;
import io.dcctech.card.flow.repository.handler.UserEventHandler;
import io.dcctech.card.flow.security.SecurityConfig;
import io.dcctech.card.flow.service.InitializerService;
import io.dcctech.card.flow.service.SecurityService;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.data.rest.RepositoryRestMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.repository.support.Repositories;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.CustomRepositoryEntityController;
import org.springframework.data.rest.webmvc.HttpHeadersPreparer;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableAutoConfiguration
@ImportAutoConfiguration(classes = {
        MongoRepositoriesAutoConfiguration.class,
        RepositoryRestMvcAutoConfiguration.class})
@ComponentScan(basePackageClasses = {
        InitializerService.class,
        SecurityConfig.class,
        UserEventHandler.class,
        ImageController.class})
@EnableMongoRepositories(basePackageClasses = UserRepository.class)
@EnableWebMvc
public class TestConfig {

    @Bean
    SpringDataConfigurer springDataConfigurer() {
        return new SpringDataConfigurer();
    }

    @Bean
    TestHelper testHelper(MockMvc mockMvc, MongoTemplate mongoTemplate) {
        return new TestHelper(mockMvc, mongoTemplate);
    }

    @Bean
    public CustomRepositoryEntityController customRepositoryEntityController(Repositories repositories, RepositoryRestConfiguration config, RepositoryEntityLinks entityLinks, PagedResourcesAssembler<Object> assembler, HttpHeadersPreparer headersPreparer) {
        return new CustomRepositoryEntityController(repositories, config, entityLinks, assembler, headersPreparer);
    }

    @Bean
    public SecurityService securityService() {
        return new SecurityService();
    }

    @Bean
    public PasswordEncoder delegatingPasswordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
