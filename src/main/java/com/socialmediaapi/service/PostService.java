package com.socialmediaapi.service;

import com.socialmediaapi.dto.PostDTO;
import com.socialmediaapi.model.Post;
import com.socialmediaapi.payload.request.PostRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.security.Principal;
import java.util.List;

public interface PostService {

    PostDTO createNewPost(PostRequest postRequest, String filename, Principal principal);

    void deletePostById(Long postId, Principal principal);

    PostDTO readPostById(Long postId);

    List<PostDTO> getAllPostByUserId(Long userId);

    PostDTO updatePost(Long id, PostRequest postRequest, String fileName, Principal principal);

    Page<Post> getPosts(Principal principal, int pageNo, int pageSize, String sortBy, String sortDir);
}
