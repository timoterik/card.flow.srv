/*
 * Copyright © 2022-2023, DCCTech, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package io.dcctech.card.flow.document;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.AccessType;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.hateoas.Identifiable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.EnumSet;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.springframework.data.annotation.AccessType.Type.PROPERTY;


@Document
public class User implements UserDetails, Identifiable<String> {

    private final static Pattern ENCODED_PASSWORD_PATTERN = Pattern.compile("\\{([\\w\\-]+)\\}.+");

    @Id
    private String id;

    @Indexed(unique = true)
    @TextIndexed
    private String username;

    @TextIndexed
    private String firstName;

    @TextIndexed
    private String lastName;

    private String password;

    @Version
    private String version;

    private Set<UserRole> roles;

    @DBRef
    private Image image;

    public User() {
    }

    @JsonCreator
    public User(@JsonProperty("username") String username,
                @JsonProperty("password") String password,
                @JsonProperty("roles") Set<UserRole> roles) {
        this.username = username;
        this.password = password;
        adjustRoles(roles);
    }

    private void adjustRoles(@JsonProperty("roles") Set<UserRole> roles) {
        this.roles = EnumSet.of(UserRole.USER);
        if (roles != null) {
            this.roles.addAll(roles);
        }
    }

    @JsonIgnore
    public String getId() {
        return id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return true;
    }

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles()
                .stream()
                .map(role -> "ROLE_" + role.name())
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @JsonIgnore
    public boolean isPasswordEncoded() {
        if (password == null || password.isEmpty()) {
            return false;
        }
        return ENCODED_PASSWORD_PATTERN.matcher(password).matches();
    }

    @AccessType(PROPERTY)
    public Set<UserRole> getRoles() {
        return roles == null ? EnumSet.of(UserRole.USER) : roles;
    }

    public void setRoles(Set<UserRole> roles) {
        adjustRoles(roles);
    }


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", roles=" + roles +
                '}';
    }
}
