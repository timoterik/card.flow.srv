/*
 * Copyright © 2022-2023, DCCTech, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package io.dcctech.card.flow.app;

import io.dcctech.card.flow.controller.ImageController;
import io.dcctech.card.flow.repository.UserRepository;
import io.dcctech.card.flow.repository.handler.UserEventHandler;
import io.dcctech.card.flow.security.SecurityConfig;
import io.dcctech.card.flow.service.InitializerService;
import io.dcctech.card.flow.service.SecurityService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.repository.support.Repositories;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.CustomRepositoryEntityController;
import org.springframework.data.rest.webmvc.HttpHeadersPreparer;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
@ComponentScan(basePackageClasses = {InitializerService.class, SecurityConfig.class, UserEventHandler.class, ImageController.class})
@EnableMongoRepositories(basePackageClasses = UserRepository.class)
@EnableWebMvc
public class RealConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        return (request) -> {
            final CorsConfiguration config = new CorsConfiguration();
            config.setAllowedMethods(Stream.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS", "HEAD").collect(Collectors.toList()));
            config.setAllowCredentials(true);
            config.setAllowedOrigins(Stream.of("*").collect(Collectors.toList()));
            config.setAllowedHeaders(Stream.of("Authorization", "Content-Type").collect(Collectors.toList()));
            return config;
        };
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