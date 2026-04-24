package com.bananapi.bananapi.repository;

import com.bananapi.bananapi.domain.Mock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface MockRepository extends JpaRepository<Mock, Long> {

    Optional<Mock> findByUrl(String url);

    List<Mock> getMocksByUserUsername(String userUsername);
}
