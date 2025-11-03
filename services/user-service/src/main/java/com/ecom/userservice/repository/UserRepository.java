package com.ecom.userservice.repository;

import com.ecom.userservice.domain.User;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends CrudRepository<User, String> {
    Optional<User> findByUsername(String username);
    
    @Query("""
        SELECT * FROM users 
        WHERE ROLES LIKE '%' || :role || '%' 
           OR ROLES = :role
           OR :role = ANY(roles)
        """)
    Set<User> findByRole(String role);
    
    boolean existsByEmail(String email);
}