package com.personalblog.article;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    Article findByIdAndOwner(Long id, String owner);
    boolean existsByIdAndOwner(Long id, String owner);
    Page<Article> findByOwner(String owner, Pageable pageable);
    boolean existsByOwner(String username);
}
