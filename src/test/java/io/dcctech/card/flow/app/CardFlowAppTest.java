/*
 * Copyright © 2022-2023, DCCTech, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package io.dcctech.card.flow.app;

import io.dcctech.card.flow.helper.JsonBuilder;
import io.dcctech.card.flow.test.SecurityTestHelper;
import io.dcctech.card.flow.test.TestBuilder;
import io.dcctech.card.flow.test.TestConfig;
import io.dcctech.card.flow.test.TestHelper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {TestConfig.class})
@AutoConfigureMockMvc
public class CardFlowAppTest {

    public static final String APPLICATION_HAL_JSON_UTF_8 = "application/hal+json;charset=UTF-8";
    public final static String USERNAME = "u001";
    public final static String PASSWORD = "u001pass";
    public static final String BOARD_ONE = "Board One";
    public static final String BOARD_TWO = "Board Two";
    public static final String CARDLIST_ONE = "CardList One";
    public static final String CARDLIST_TWO = "CardList Two";
    public static final String CARD_ONE = "Card One";
    public static final String CARD_TWO = "Card Two";
    public static final String CARDLIST_THREE = "CardList Three";
    @Autowired
    private TestHelper helper;
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Login succeeds with bela/hello")
    public void loginWithBela() throws Exception {

        new TestBuilder()
                .perform(helper::loginWithBela)
                .andExpectValueAtJsonPath("$.sessionId", notNullValue())
                .andExpectValueAtJsonPath("$._links.user.href", notNullValue())
                .andExpectValueAtJsonPath("$._links.user.href", startsWith("http://localhost/users/"));
    }

    @Nested
    @DisplayName("Logging in with admin/admin")
    class LoginAdmin {
        String adminLink;

        @BeforeEach
        void beforeEach() throws Exception {
            adminLink = new TestBuilder()
                    .perform(helper::loginWithAdmin)
                    .andExpectValueAtJsonPath("$.sessionId", notNullValue())
                    .andMapWithJsonPath("$._links.user.href", String.class)
                    .getResult();
        }

        @Test
        @DisplayName("Succeeds")
        void succeeds() {
        }

        @Test
        @DisplayName("User 'admin' has the role 'ADMIN'")
        void hasAdminRole() throws Exception {
            new TestBuilder()
                    .useSession(helper.sessionFor("admin"))
                    .useParam(adminLink)
                    .thenPerformWithSessionAndParam(helper::httpGet)
                    .andExpectValueAtJsonPath("$.roles", hasItem("ADMIN"));
        }
    }

    @Nested
    @DisplayName("Upload profile image")
    class UploadProfileImage {

        String imageLink;

        @BeforeEach
        void beforeEach() throws Exception {
            imageLink = new TestBuilder()
                    .useSession(SecurityTestHelper.anonymousSession())
                    .useParam("Color logo - no background.jpg")
                    .thenPerformWithSessionAndParam(helper::uploadImage)
                    .andMapWithJsonPath("$._links.self.href", String.class)
                    .getResult();
        }

        @AfterEach
        void afterEach() throws Exception {
            helper.httpDelete(helper.sessionFor("admin"), imageLink);
        }

        @Nested
        @DisplayName("Registering user " + USERNAME + " with password " + PASSWORD)
        class RegisterUser {


            String userLink;

            @BeforeEach
            void beforeEach() throws Exception {


                userLink = new TestBuilder()
                        .useSession(SecurityTestHelper.anonymousSession())
                        .useParam(new JsonBuilder()
                                .startObject()
                                .field("username", USERNAME)
                                .field("password", PASSWORD)
                                .field("image", imageLink)
                                .endObject()
                                .build())
                        .thenPerformWithSessionAndParam(helper::createUser)
                        .andMapWithJsonPath("$._links.self.href", String.class)
                        .getResult();
            }

            @Test
            @DisplayName("Authentication with " + USERNAME + "/" + PASSWORD + " is successful")
            void authSuccess() throws Exception {
                new TestBuilder()
                        .useSession(SecurityTestHelper.anonymousSession())
                        .perform(() -> helper.login(USERNAME, PASSWORD))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                        .andExpectValueAtJsonPath("$._links.user.href", is(userLink));

            }

            @Test
            @DisplayName("No boards for user " + USERNAME)
            void noBoards() throws Exception {
                new TestBuilder()
                        .useSession(helper.sessionFor(USERNAME))
                        .useParam(userLink)
                        .thenPerformWithSessionAndParam(helper::findBoardsOfUser)
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(APPLICATION_HAL_JSON_UTF_8))
                        .andExpectValueAtJsonPath("$._embedded.boards.length()", is(0));

            }

            @AfterEach
            void afterEach() throws Exception {
                helper.httpDelete(helper.sessionFor("admin"), userLink);
            }

            @Nested
            @DisplayName("Creating board '" + BOARD_ONE + "' and '" + BOARD_TWO + "'")
            class CreateBoards {


                String boardOneLink;
                String boardTwoLink;

                @BeforeEach
                void beforeEach() throws Exception {
                    boardOneLink = new TestBuilder()
                            .useSession(helper.sessionFor(USERNAME))
                            .useParam(new JsonBuilder()
                                    .startObject()
                                    .field("name", BOARD_ONE)
                                    .arrayFieldStart("memberships")
                                    .startObject()
                                    .field("role", "ADMIN")
                                    .field("user", userLink)
                                    .endObject()
                                    .arrayEnd()
                                    .endObject()
                                    .build())
                            .thenPerformWithSessionAndParam(helper::createBoard)
                            .andMapWithJsonPath("$._links.self.href", String.class)
                            .getResult();
                    boardTwoLink = new TestBuilder()
                            .useSession(helper.sessionFor(USERNAME))
                            .useParam(new JsonBuilder()
                                    .startObject()
                                    .field("name", BOARD_TWO)
                                    .arrayFieldStart("memberships")
                                    .startObject()
                                    .field("role", "ADMIN")
                                    .field("user", userLink)
                                    .endObject()
                                    .arrayEnd()
                                    .endObject()
                                    .build())
                            .thenPerformWithSessionAndParam(helper::createBoard)
                            .andMapWithJsonPath("$._links.self.href", String.class)
                            .getResult();
                }

                @Test
                @DisplayName("The two boards are listed")
                void listBoards() throws Exception {
                    new TestBuilder()
                            .useSession(helper.sessionFor(USERNAME))
                            .useParam(userLink)
                            .thenPerformWithSessionAndParam(helper::findBoardsOfUser)
                            .andMapWithJsonPath("$._embedded.boards.[*].name", List.class)
                            .andExpectValueAtJsonPath("$.length()", is(2))
                            .andExpectValueAtJsonPath("$", contains(BOARD_ONE, BOARD_TWO));
                }

                @AfterEach
                void afterEach() throws Exception {
                    new TestBuilder()
                            .useSession(helper.sessionFor("admin"))
                            .performWithSession(session -> helper.httpDelete(session, boardOneLink))
                            .thenPerformWithSession(session -> helper.httpDelete(session, boardTwoLink));
                }

                @Nested
                @DisplayName("Archive board '" + BOARD_ONE + "'")
                class ArchiveBoard {
                    @BeforeEach
                    void beforeEach() throws Exception {
                        boardOneLink = new TestBuilder()
                                .useSession(helper.sessionFor(USERNAME))
                                .useParam(new JsonBuilder()
                                        .startObject()
                                        .field("archived", true)
                                        .endObject()
                                        .build())
                                .thenPerformWithSessionAndParam((session, param)
                                        -> helper.httpPatch(session, boardOneLink, param))
                                .andMapWithJsonPath("$._links.self.href", String.class)
                                .getResult();
                    }

                    @Test
                    @DisplayName("The board list of user '" + USERNAME + "' should contain only '" + BOARD_TWO + "'")
                    void listBoards() throws Exception {
                        new TestBuilder()
                                .useSession(helper.sessionFor(USERNAME))
                                .useParam(userLink)
                                .thenPerformWithSessionAndParam(helper::findBoardsOfUser)
                                .andMapWithJsonPath("$._embedded.boards.[*].name", List.class)
                                .andExpectValueAtJsonPath("$.length()", is(1))
                                .andExpectValueAtJsonPath("$", contains(BOARD_TWO));
                    }

                    @AfterEach
                    void afterEach() throws Exception {
                        boardOneLink = new TestBuilder()
                                .useSession(helper.sessionFor(USERNAME))
                                .useParam(new JsonBuilder()
                                        .startObject()
                                        .field("archived", false)
                                        .endObject()
                                        .build())
                                .thenPerformWithSessionAndParam((session, param)
                                        -> helper.httpPatch(session, boardOneLink, param))
                                .andMapWithJsonPath("$._links.self.href", String.class)
                                .getResult();
                    }
                }

                @Nested
                @DisplayName("Creating CardLists: '" + CARDLIST_ONE + "' and '" + CARDLIST_TWO
                        + " in '" + BOARD_ONE + "' and '" + CARDLIST_THREE + "' in '" + BOARD_TWO + "'")
                class CreateCardLists {
                    private String cardListOneLink;
                    private String cardListTwoLink;
                    private String cardListThreeLink;

                    @BeforeEach
                    void beforeEach() throws Exception {
                        cardListOneLink = new TestBuilder()
                                .useSession(helper.sessionFor(USERNAME))
                                .useParam(new JsonBuilder()
                                        .startObject()
                                        .field("name", CARDLIST_ONE)
                                        .field("board", boardOneLink)
                                        .endObject()
                                        .build())
                                .thenPerformWithSessionAndParam(helper::createCardList)
                                .andMapWithJsonPath("$._links.self.href", String.class)
                                .getResult();

                        cardListTwoLink = new TestBuilder()
                                .useSession(helper.sessionFor(USERNAME))
                                .useParam(new JsonBuilder()
                                        .startObject()
                                        .field("name", CARDLIST_TWO)
                                        .field("board", boardOneLink)
                                        .endObject()
                                        .build())
                                .thenPerformWithSessionAndParam(helper::createCardList)
                                .andMapWithJsonPath("$._links.self.href", String.class)
                                .getResult();
                        cardListThreeLink = new TestBuilder()
                                .useSession(helper.sessionFor(USERNAME))
                                .useParam(new JsonBuilder()
                                        .startObject()
                                        .field("name", CARDLIST_THREE)
                                        .field("board", boardTwoLink)
                                        .endObject()
                                        .build())
                                .thenPerformWithSessionAndParam(helper::createCardList)
                                .andMapWithJsonPath("$._links.self.href", String.class)
                                .getResult();
                    }

                    @Test
                    @DisplayName("When listing active cardLists of board '" + BOARD_ONE + "', it should return '" + CARDLIST_ONE + "' and '" + CARDLIST_TWO + "'")
                    void getCardListsByBoard() throws Exception {
                        new TestBuilder()
                                .useSession(helper.sessionFor(USERNAME))
                                .useParam(boardOneLink)
                                .thenPerformWithSessionAndParam(helper::findCardListsByBoard)
                                .andMapWithJsonPath("$._embedded.cardLists[*]", List.class)
                                .andExpectValueAtJsonPath("$.length()", is(2))
                                .andExpectValueAtJsonPath("$[*].name", contains(CARDLIST_ONE, CARDLIST_TWO));
                    }

                    @AfterEach
                    void afterEach() throws Exception {
                        new TestBuilder()
                                .useSession(helper.sessionFor("admin"))
                                .performWithSession(session -> helper.httpDelete(session, cardListOneLink))
                                .thenPerformWithSession(session -> helper.httpDelete(session, cardListTwoLink))
                                .thenPerformWithSession(session -> helper.httpDelete(session, cardListThreeLink));
                    }

                    @Nested
                    @DisplayName("Archiving cardList '" + CARDLIST_TWO + "'")
                    class ArchiveCardLists {
                        @BeforeEach
                        void beforeEach() throws Exception {
                            String json = new JsonBuilder()
                                    .startObject()
                                    .field("archived", true)
                                    .endObject()
                                    .build();

                            new TestBuilder()
                                    .useSession(helper.sessionFor(USERNAME))
                                    .performWithSession(session -> helper.httpPut(session, cardListTwoLink, json));
                        }

                        @Test
                        @DisplayName("When listing active cardLists of board '" + BOARD_ONE + "', it should return only '" + CARDLIST_ONE + "'")
                        void getCardListsByBoard() throws Exception {
                            new TestBuilder()
                                    .useSession(helper.sessionFor(USERNAME))
                                    .useParam(boardOneLink)
                                    .thenPerformWithSessionAndParam(helper::findActiveCardListsByBoard)
                                    .andMapWithJsonPath("$._embedded.cardLists[*]", List.class)
                                    .andExpectValueAtJsonPath("$.length()", is(1))
                                    .andExpectValueAtJsonPath("$[*].name", contains(CARDLIST_ONE));
                        }

                        @AfterEach
                        void afterEach() throws Exception {
                            String json = new JsonBuilder()
                                    .startObject()
                                    .field("archived", false)
                                    .endObject()
                                    .build();

                            new TestBuilder()
                                    .useSession(helper.sessionFor(USERNAME))
                                    .performWithSession(session -> helper.httpPut(session, cardListTwoLink, json));
                        }

                    }

                    @Nested
                    @DisplayName("Creating card '" + CARD_ONE + "' and '" + CARD_TWO)
                    class CreateCards {

                        private String cardOneLink;
                        private String cardTwoLink;
                        private final int cardOnePos = 10;
                        private final int cardTwoPos = 20;

                        @BeforeEach
                        void beforeEach() throws Exception {
                            cardOneLink = new TestBuilder()
                                    .useSession(helper.sessionFor(USERNAME))
                                    .useParam(new JsonBuilder()
                                            .startObject()
                                            .field("title", CARD_ONE)
                                            .field("cardList", cardListOneLink)
                                            .field("pos", cardOnePos)
                                            .endObject()
                                            .build())
                                    .thenPerformWithSessionAndParam(helper::createCard)
                                    .andMapWithJsonPath("$._links.self.href", String.class)
                                    .getResult();
                            cardTwoLink = new TestBuilder()
                                    .useSession(helper.sessionFor(USERNAME))
                                    .useParam(new JsonBuilder()
                                            .startObject()
                                            .field("title", CARD_TWO)
                                            .field("cardList", cardListOneLink)
                                            .field("pos", cardTwoPos)
                                            .endObject()
                                            .build())
                                    .thenPerformWithSessionAndParam(helper::createCard)
                                    .andMapWithJsonPath("$._links.self.href", String.class)
                                    .getResult();
                        }

                        @Test
                        @DisplayName("The two cards are listed")
                        void listCards() throws Exception {
                            new TestBuilder()
                                    .useSession(helper.sessionFor(USERNAME))
                                    .useParam(cardListOneLink)
                                    .thenPerformWithSessionAndParam(helper::findCardsOfCardList)
                                    .andMapWithJsonPath("$._embedded.cards.[*].title", List.class)
                                    .andExpectValueAtJsonPath("$.length()", is(2))
                                    .andExpectValueAtJsonPath("$", contains(CARD_ONE, CARD_TWO));
                        }

                        @AfterEach
                        void afterEach() throws Exception {
                            new TestBuilder()
                                    .useSession(helper.sessionFor("admin"))
                                    .performWithSession(session -> helper.httpDelete(session, cardOneLink))
                                    .thenPerformWithSession(session -> helper.httpDelete(session, cardTwoLink));
                        }

                        @Nested
                        @DisplayName("Archive card " + CARD_ONE)
                        class ArchiveCard {
                            @BeforeEach
                            void beforeEach() throws Exception {
                                cardOneLink = new TestBuilder()
                                        .useSession(helper.sessionFor(USERNAME))
                                        .useParam(new JsonBuilder()
                                                .startObject()
                                                .field("archived", true)
                                                .endObject()
                                                .build())
                                        .thenPerformWithSessionAndParam((session, param)
                                                -> helper.httpPatch(session, cardOneLink, param))
                                        .andMapWithJsonPath("$._links.self.href", String.class)
                                        .getResult();
                            }

                            @Test
                            @DisplayName("The archived card is excluded from the list")
                            void listCards() throws Exception {
                                new TestBuilder()
                                        .useSession(helper.sessionFor(USERNAME))
                                        .useParam(cardListOneLink)
                                        .thenPerformWithSessionAndParam(helper::findCardsOfCardList)
                                        .andMapWithJsonPath("$._embedded.cards.[*].title", List.class)
                                        .andExpectValueAtJsonPath("$.length()", is(1))
                                        .andExpectValueAtJsonPath("$", contains(CARD_TWO));
                            }

                            @AfterEach
                            void afterEach() throws Exception {
                                cardOneLink = new TestBuilder()
                                        .useSession(helper.sessionFor(USERNAME))
                                        .useParam(new JsonBuilder()
                                                .startObject()
                                                .field("archived", false)
                                                .endObject()
                                                .build())
                                        .thenPerformWithSessionAndParam((session, param)
                                                -> helper.httpPatch(session, cardOneLink, param))
                                        .andMapWithJsonPath("$._links.self.href", String.class)
                                        .getResult();
                            }
                        }

                    }

                }
            }

        }

    }


}
