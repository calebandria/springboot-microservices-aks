package com.ecom.userservice.repository;

import com.ecom.userservice.domain.Role;
import com.ecom.userservice.domain.Users;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@Sql("/schema-h2.sql") // Will use H2 + application.yml from test resources
class UserRepositoryTest {

    @Autowired
    private UserRepository repository;

    @Test
    void shouldFindByRole() {
        Users admin = Users.builder()
                .username("admin")
                .email("admin@ecom.com")
                .passwordHash("xxx")
                .roles(Set.of(Role.ADMIN))
                .build();

        repository.save(admin); // ← ID auto-generated

        Optional<Users> result = repository.findByUsername("admin");

        assertThat(result).isNotEmpty();

        assertThat(result.get().getEmail()).isEqualTo("admin@ecom.com");
    }
}