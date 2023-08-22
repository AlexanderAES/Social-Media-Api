package com.socialmediaapi.repository;

import com.socialmediaapi.model.Post;
import com.socialmediaapi.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    Optional<Post> getPostById(Long postId);

    Optional<Post> findByIdAndUser(Long id, User currentUser);

    void deletePostById(Long postId);


    @Query("SELECT new com.socialmediaapi.dto.PostDTO(p.id, p.title, p.text, p.filename,p.user.id, p.timeCreate) " +
            "FROM Post p " +
            "JOIN User u ON p.user.id = u.id " +
            "JOIN Subscription s ON u.id = s.user.id " +
            "WHERE s.subscriber.id = :userId")
    Page<Post> getPostsAllAuthorsWhichSubscription(long userId, Pageable pageable);

}

