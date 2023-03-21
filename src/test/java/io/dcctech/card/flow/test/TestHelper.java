/*
 * A DCCTech © 2022 - 2023 All Rights Reserved. This copyright notice is the exclusive property of DCCTech and
 * is hereby granted to users for use of DCCTech's intellectual property. Any reproduction, modification, distribution,
 * or other use of DCCTech's intellectual property without prior written consent is strictly prohibited.
 */

package io.dcctech.card.flow.test;

import io.dcctech.card.flow.document.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;

import javax.validation.constraints.NotNull;
import java.io.InputStream;

import static io.dcctech.card.flow.app.CardFlowAppTest.APPLICATION_HAL_JSON_UTF_8;
import static io.dcctech.card.flow.test.SecurityTestHelper.createAuthenticationFor;
import static io.dcctech.card.flow.test.SecurityTestHelper.createSession;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RequiredArgsConstructor
public class TestHelper {

    private @NotNull MockMvc mvc;
    private @NotNull MongoTemplate monTemplate;

    public TestHelper(MockMvc mockMvc, MongoTemplate mongoTemplate) {
        mvc = mockMvc;
        monTemplate = mongoTemplate;

    }

    public ResultActions login(String username, String password) throws Exception {
        return this.mvc.perform(
                        post("/login")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .content(new FormBuilder()
                                        .property("username", username)
                                        .property("password", password)
                                        .build()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("$.sessionId").value(not(nullValue())));
    }

    public ResultActions loginWithBela() throws Exception {
        return login("bela", "hello");
    }

    public ResultActions loginWithAdmin() throws Exception {
        return login("admin", "admin");
    }


    public ResultActions findBoardsOfUser(MockHttpSession session, String userHref) throws Exception {
        final ResultActions resultActions = this.mvc.perform(
                        get("/boards/search/findByMembershipsUserAndArchivedIsFalse?user={user}", userHref)
                                .session(session))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json;charset=UTF-8"))
                .andExpect(jsonPath("$._embedded.boards").value(not(nullValue())));
        return resultActions;
    }

    public ResultActions findCardsOfCardList(MockHttpSession session, String cardListHref) throws Exception {
        final ResultActions resultActions = this.mvc.perform(
                get("/cards/search/findByCardListAndArchivedIsFalseOrderByPos?cardList={cardList}", cardListHref)
                        .session(session));
        return resultActions;
    }

    public ResultActions httpGet(MockHttpSession session, String url) throws Exception {
        final ResultActions resultActions = this.mvc.perform(
                        get(url)
                                .session(session))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json;charset=UTF-8"));
        return resultActions;
    }

    public ResultMatcher statusIsOkAndContentTypeIsHalJson() {
        return mvcResult -> {
            status().isOk().match(mvcResult);
            content().contentType("application/hal+json;charset=UTF-8").match(mvcResult);
        };
    }

    public ResultActions createUser(MockHttpSession session, String userJson) throws Exception {
        return httpPost(session, "/users", userJson);
    }

    public ResultActions createBoard(MockHttpSession session, String boardJson) throws Exception {
        return httpPost(session, "/boards", boardJson);
    }

    //-------------------------------------
    //cardlists begin
    public ResultActions createCardList(MockHttpSession session, String cardListJson) throws Exception {
        return httpPost(session, "/cardLists", cardListJson);
    }

    public ResultActions findCardListsByBoard(MockHttpSession session, String board) throws Exception {
        return this.mvc.perform(
                        get("/cardLists/search/findByBoard")
                                .session(session)
                                .param("board", board))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_HAL_JSON_UTF_8))
                .andExpect(jsonPath("$._embedded.cardLists").exists())
                .andExpect(jsonPath("$._embedded.cardLists").isArray());
    }

    public ResultActions createCard(MockHttpSession session, String cardJson) throws Exception {
        return httpPost(session, "/cards", cardJson);
    }

    public ResultActions httpPost(MockHttpSession session, String url, String json) throws Exception {
        return this.mvc.perform(
                post(url)
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(json));
    }

    public ResultActions httpPut(MockHttpSession session, String url, String json) throws Exception {
        return this.mvc.perform(
                put(url)
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(json));
    }

    public ResultActions httpPatch(MockHttpSession session, String url, String json) throws Exception {
        return this.mvc.perform(
                patch(url)
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(json));
    }

    public ResultActions httpDelete(MockHttpSession session, String url) throws Exception {
        return this.mvc.perform(
                delete(url)
                        .session(session));
    }

    public ResultActions findActiveCardListsByBoard(MockHttpSession session, String board) throws Exception {
        return this.mvc.perform(
                        get("/cardLists/search/findByArchivedFalseAndBoard")
                                .session(session)
                                .param("board", board))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_HAL_JSON_UTF_8))
                .andExpect(jsonPath("$._embedded.cardLists").exists())
                .andExpect(jsonPath("$._embedded.cardLists").isArray());
    }

    public TestBuilder.TestStepWithSession httpPost(String url, String json) throws Exception {
        return session -> this.mvc.perform(
                post(url)
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(json));
    }


    public ResultActions uploadImage(MockHttpSession session, String imagePath) throws Exception {
        return uploadImage(session, TestHelper.class.getClassLoader().getResourceAsStream(imagePath));
    }

    public ResultActions uploadImage(MockHttpSession session, InputStream image) throws Exception {
        return this.mvc.perform(
                multipart("/images")
                        .file(new MockMultipartFile("content", image))
                        .session(session));
    }

    public MockHttpSession sessionFor(String username) {
        final User user = monTemplate.findOne(
                query(where("username").is(username)), User.class);
        return createSession(createAuthenticationFor(user));
    }


}
