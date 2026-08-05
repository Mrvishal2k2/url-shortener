package com.shortener.repository;

import com.shortener.model.Url;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShortenerRepo extends JpaRepository<Url, Long> {
    Optional<Url> findByShortId(String shortId);
    boolean existsByShortId(String shortId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update Url u set u.clickCount = u.clickCount + 1 where u.shortId = :shortId")
    void incrementClickCount(@Param("shortId") String shortId);
}
