package com.ecom.userservice.domain;

import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table("USERS")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class User {
    @Id
    @Column("ID")
    private String id;

    @Column("USERNAME")
    private String username;

    @Column("EMAIL")
    private String email;

    @Column("PASSWORD_HASH")
    private String passwordHash;

    @Column("ROLES")
    @Builder.Default
    private Set<String> roles = Set.of(Role.USER);
}