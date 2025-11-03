package com.ecom.userservice.repository;

import com.ecom.userservice.domain.Role;
import com.ecom.userservice.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@DataJdbcTest
@Sql("/schema-h2.sql") // Will use H2 + application.yml from test resources
class UserRepositoryTest {

    @Autowired
    private UserRepository repository;
   String id = UUID.randomUUID().toString();

    @Test
    void shouldFindByRole() {
        User admin = User.builder()
                .id(id)
                .username("admin")
                .email("admin@ecom.com")
                .passwordHash("xxx")
                .roles(Set.of(Role.ADMIN))
                .build();

        repository.save(admin); // ← ID auto-generated

        Set<User> result = repository.findByRole(Role.ADMIN);

        assertThat(result).hasSize(1);
        assertThat(result.iterator().next().getUsername()).isEqualTo("admin");
    }
}