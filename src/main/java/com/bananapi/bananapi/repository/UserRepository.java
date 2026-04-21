package com.bananapi.bananapi.repository;

import com.bananapi.bananapi.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsUserByUsername(String username);

    boolean existsUserByEmail(String email);

    User findUserByUsername(String username);

    void deleteByUsername(String username);
}
