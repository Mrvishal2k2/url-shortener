package com.shortener.repository;

import com.shortener.model.Url;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShortenerRepo extends JpaRepository<Url, Long> {
    Optional<Url> findByShortId(String shortId);
    boolean existsByShortId(String shortId);
}
