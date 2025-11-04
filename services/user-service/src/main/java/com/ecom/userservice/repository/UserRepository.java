package com.ecom.userservice.repository;

import com.ecom.userservice.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByUsername(String username);

    Optional<Users> findByEmail(String mail);

    Users getById(Long id);
    Users getByEmail(String email);
    

}