/*
 * Copyright © 2022-2023, DCCTech, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package io.dcctech.card.flow.controller;

import io.dcctech.card.flow.document.Image;
import io.dcctech.card.flow.document.User;
import lombok.RequiredArgsConstructor;
import org.imgscalr.Scalr;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.rest.webmvc.support.ETag;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.validation.constraints.NotNull;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;

@RestController
@RequiredArgsConstructor
public class ImageController {

    private @NotNull MongoTemplate template;
    private @NotNull RepositoryEntityLinks repositoryEntityLinks;


    @GetMapping(value = "/images/{id}", produces = "image/*")
    public ResponseEntity<byte[]> getImage(@PathVariable("id") String id) {
        final Image image = template.findById(id, Image.class);
        if (image != null) {
            return ResponseEntity
                    .ok()
                    .contentType(MediaType.parseMediaType(image.getContentType()))
                    .body(image.getContent());

        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping(value = "/users/{userId}/image", produces = "image/*")
    public ResponseEntity<byte[]> getUserImage(@PathVariable("userId") String userId) {
        final User user = template.findById(userId, User.class);
        final Image image = user.getImage();
        if (image != null) {
            return ResponseEntity
                    .ok()
                    .contentType(MediaType.parseMediaType(image.getContentType()))
                    .body(image.getContent());

        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/images")
    public ResponseEntity<Resource<?>> createImage(@RequestParam("content") MultipartFile file) throws IOException {
        final BufferedImage origImage = ImageIO.read(file.getInputStream());
        final BufferedImage resized = Scalr.resize(origImage, Scalr.Method.ULTRA_QUALITY, Scalr.Mode.AUTOMATIC, 200);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(resized, "png", out);

        final Image image = new Image();
        image.setContentType("image/png");
        image.setContent(out.toByteArray());
        template.insert(image);
        final Link selfLink = repositoryEntityLinks.linkToSingleResource(Image.class, image.getId()).withSelfRel();
        final Link imageLink = repositoryEntityLinks.linkToSingleResource(Image.class, image.getId());
        final URI selfURI = URI.create(selfLink.getHref());
        final ETag eTag = ETag.from(image.getVersion());

        return ResponseEntity
                .created(selfURI)
                .eTag(eTag.toString())
                .body(new Resource(image, selfLink, imageLink));
    }

}
