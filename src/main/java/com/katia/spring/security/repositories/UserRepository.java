package com.katia.spring.security.repositories;

import com.katia.spring.security.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);
    boolean existsByEmailAndIdNot(String email, Long id);

    User findByEmail(String email);
    @Nullable
    Optional<User> findById(Long id);


}
