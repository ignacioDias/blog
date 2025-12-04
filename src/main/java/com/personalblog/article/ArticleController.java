package com.personalblog.article;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@CrossOrigin(origins = "http://localhost:8080")
public class ArticleController {

    private final ArticleRepository articleRepository;

    public ArticleController(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @GetMapping("/article/{requestedId}")
    public ResponseEntity<Article> findById(@PathVariable Long requestedId) {
        var article = articleRepository.findById(requestedId);
        if(article.isPresent()) {
            return ResponseEntity.ok(article.get());
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/home")
    public ResponseEntity<List<Article>> findAll() {
        return ResponseEntity.ok(articleRepository.findAll());
    }

    @PutMapping("/edit/{requestedId}")
    public ResponseEntity<Void> editById(@PathVariable Long requestedId, @RequestBody Article requestedBody, Principal principal) {
        if(requestedBody.getContent() == null || requestedBody.getContent().isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        var articleFromDB = articleRepository.findById(requestedId);
        if(!articleFromDB.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        var newArticle = articleFromDB.get();
        var ownerOfArticleFromDB = articleFromDB.get().getOwner();
        if(!ownerOfArticleFromDB.equals(principal.getName())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        newArticle.setContent(requestedBody.getContent());
        newArticle.setTitle(requestedBody.getTitle());
        articleRepository.save(newArticle);
        return ResponseEntity.noContent().build();
    }
    @PostMapping("/new") 
    public ResponseEntity<Void> createArticle(@RequestBody Article requestBody, UriComponentsBuilder ucb, Principal principal) {
        if(requestBody.getTitle() == null || requestBody.getTitle().isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        if(requestBody.getContent() == null || requestBody.getContent().isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        var newArticle = new Article(null, LocalDateTime.now(),  principal.getName(), requestBody.getTitle(), requestBody.getContent());
        var saved = articleRepository.save(newArticle);
        var location = ucb.path("/article/{id}").buildAndExpand(saved.getId()).toUri();
        return ResponseEntity.created(location).build();
    }
    @GetMapping("/admin")
    public ResponseEntity<Page<Article>> getAllOwnedArticles(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "createdAt") String sortBy, @RequestParam(defaultValue = "DESC") String sortDirection, Principal principal) {
        Sort.Direction direction = sortDirection.equalsIgnoreCase("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        return ResponseEntity.ok(articleRepository.findByOwner(principal.getName(), pageable));
    }

    @DeleteMapping("/delete/{requestedId}")
    public ResponseEntity<Void> deleteArticle(@PathVariable Long requestedId, Principal principal) {
        if(!articleRepository.existsByIdAndOwner(requestedId, principal.getName())) {
            return ResponseEntity.notFound().build();
        }
        articleRepository.deleteById(requestedId);
        return ResponseEntity.noContent().build();
    }
}
