package com.bananapi.bananapi.repository;

import com.bananapi.bananapi.domain.Mock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MockRepository extends JpaRepository<Mock, Long> {
    Mock findByUrl(String url);

    List<Mock> getMocksByUserUsername(String userUsername);
}
