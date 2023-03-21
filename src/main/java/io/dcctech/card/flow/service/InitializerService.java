/*
 * A DCCTech © 2022 - 2023 All Rights Reserved. This copyright notice is the exclusive property of DCCTech and
 * is hereby granted to users for use of DCCTech's intellectual property. Any reproduction, modification, distribution,
 * or other use of DCCTech's intellectual property without prior written consent is strictly prohibited.
 */

package io.dcctech.card.flow.service;

import io.dcctech.card.flow.document.*;
import io.dcctech.card.flow.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.FastByteArrayOutputStream;

import javax.validation.constraints.NotNull;
import java.io.InputStream;
import java.util.EnumSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InitializerService implements InitializingBean {

    private @NotNull MongoTemplate mongoTemplate;

    private @NotNull PasswordEncoder passwordEncoder;

    private @NotNull UserRepository userRepository;

    private @NotNull MongoHelper mongoHelper;

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("InitializerService is starting...");

        User ted = findUserByUsername("ted");

        if (ted != null) {
            mongoHelper.cascadeDelete(ted);
        }

        User admin = findUserByUsername("admin");

        if (admin != null) {
            mongoHelper.cascadeDelete(admin);
        }

        mongoTemplate.remove(new Query(), User.class);
        mongoTemplate.remove(new Query(), Image.class);
        mongoTemplate.remove(new Query(), Board.class);

        if (!userExists("admin")) {
            System.out.println("Creating admin user...");
            admin = new User("admin", "admin", EnumSet.of(UserRole.ADMIN, UserRole.USER));
            admin.setFirstName("Joe");
            admin.setLastName("Administrator");
            admin.setPassword(passwordEncoder.encode(admin.getPassword()));
            mongoTemplate.save(admin);
        }

        if (!userExists("ted")) {
            ted = new User("ted", "hello", EnumSet.of(UserRole.USER));
            ted.setFirstName("Ted");
            ted.setLastName("Mosby");
            Image image = new Image();
            image.setContentType("image/jpeg");

            byte[] buffer = new byte[8192];
            int bytesRead;

            try (final InputStream in = getClass().getClassLoader().getResourceAsStream("Color logo - no background.jpg");
                 final FastByteArrayOutputStream out = new FastByteArrayOutputStream()) {
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
                image.setContent(out.toByteArray());
            }

            mongoTemplate.save(image);

            ted.setImage(image);
            ted.setPassword(passwordEncoder.encode(ted.getPassword()));
            mongoTemplate.save(ted);

            final BoardMembership m1 = new BoardMembership();
            m1.setUser(ted);
            m1.setRole(BoardRole.ADMIN);

            final Board b1 = new Board();
            b1.setName("1. Board");

            final Board b2 = new Board();
            b2.setName("2. Board");

            final Board b3 = new Board();
            b3.setName("3. Board");

            final Board b4 = new Board();
            b4.setName("4. Board");

            b1.setMemberships(List.of(m1));
            b2.setMemberships(List.of(m1));
            b3.setMemberships(List.of(m1));
            b4.setMemberships(List.of(m1));

            mongoTemplate.save(b1);
            mongoTemplate.save(b2);
            mongoTemplate.save(b3);
            mongoTemplate.save(b4);

            final CardList cardList1 = new CardList();

            cardList1.setName("first list");
            cardList1.setBoard(b1);
            mongoTemplate.save(cardList1);

            final CardList cardList2 = new CardList();
            cardList2.setName("second list");
            cardList2.setBoard(b1);
            mongoTemplate.save(cardList2);

            final CardList cardList3 = new CardList();
            cardList3.setName("third list");
            cardList3.setBoard(b1);
            mongoTemplate.save(cardList3);

            final Card card1 = new Card();
            card1.setTitle("first card");
            card1.setCardList(cardList1);
            card1.setPos(65536);
            mongoTemplate.save(card1);

            final Card card2 = new Card();
            card2.setTitle("second card");
            card2.setCardList(cardList1);
            card2.setPos(card1.getPos() + 65536);
            mongoTemplate.save(card2);

            final Card card3 = new Card();
            card3.setTitle("third card");
            card3.setCardList(cardList2);
            card3.setPos(65536);
            mongoTemplate.save(card3);

            final Card card4 = new Card();
            card4.setTitle("fourth card");
            card4.setCardList(cardList3);
            card4.setPos(65536);
            mongoTemplate.save(card4);


        }
    }

    private User findUserByUsername(String username) {
        return mongoTemplate.findOne(whereUsernameIs(username), User.class);
    }

    private boolean userExists(String username) {
        return mongoTemplate.exists(whereUsernameIs(username), User.class);
    }

    private Query whereUsernameIs(String username) {
        return Query.query(Criteria.where("username").is(username));
    }
}