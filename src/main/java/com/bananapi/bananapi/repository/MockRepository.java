package com.bananapi.bananapi.repository;

import com.bananapi.bananapi.domain.Mock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MockRepository extends JpaRepository<Mock, Long> {
}
