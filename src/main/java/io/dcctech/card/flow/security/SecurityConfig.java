/*
 * Copyright © 2022-2023, DCCTech, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package io.dcctech.card.flow.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dcctech.card.flow.document.User;
import io.dcctech.card.flow.helper.JsonBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.constraints.NotNull;
import java.io.PrintWriter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private @NotNull RepositoryEntityLinks repositoryEntityLinks;
    private @NotNull MongoBasedUserDetailsService mongoBasedUserDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth)
            throws Exception {
        auth.userDetailsService(mongoBasedUserDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint((request, response, authEx) -> {
                    response.setStatus(401);
                    response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
                    new JsonBuilder(response.getWriter())
                            .startObject()
                            .field("message", "Authentication session is required")
                            .endObject()
                            .close();

                })
                .and()
                .authorizeRequests()
                //.antMatchers("/users/**").authenticated()
                .mvcMatchers(HttpMethod.GET, "/users", "/users/{id}", "/users/{id}/search/{searchName}", "/users/{id}/{field}").authenticated()
                .mvcMatchers(HttpMethod.PUT, "/users/{id}", "/users/{id}/{field}").authenticated()
                .mvcMatchers(HttpMethod.PATCH, "/users/{id}", "/users/{id}/{field}").authenticated()
                .mvcMatchers(HttpMethod.GET, "/images", "/images/{id}", "/images/{id}/search/{searchName}", "/images/{id}/{field}").authenticated()
                .mvcMatchers(HttpMethod.PUT, "/images/{id}", "/images/{id}/{field}").authenticated()
                .mvcMatchers(HttpMethod.PATCH, "/images/{id}", "/images/{id}/{field}").authenticated()
                .antMatchers("/boards/**").authenticated()
                .antMatchers("/cardLists/**").authenticated()
                .antMatchers("/cards/**").authenticated()
                .mvcMatchers(HttpMethod.DELETE, "/users/{id}").hasRole("ADMIN")
                .and()
                .formLogin()
                .successHandler((request, response, authentication) -> {
                    response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
                    final PrintWriter out = response.getWriter();
                    final ObjectMapper objectMapper = new ObjectMapper();
                    final String sessionId = request.getSession().getId();

                    User user = (User) authentication.getPrincipal();
                    final String userHref = ServletUriComponentsBuilder.fromRequest(request)
                            .replacePath("/users/" + user.getId()).build().toString();

                    final AuthenticatedSessionInfo sessionInfo =
                            new AuthenticatedSessionInfo(sessionId,
                                    new Link(userHref).withRel("user"));

                    objectMapper.writeValue(out, sessionInfo);
                    out.close();

                })
                .failureHandler((request, response, exception) -> {
                    response.setStatus(401);
                    response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
                    new JsonBuilder(response.getWriter())
                            .startObject()
                            .field("message", exception.getMessage())
                            .endObject()
                            .close();
                })
                .and()
                .logout()
                .and()
                .cors();
    }

}
