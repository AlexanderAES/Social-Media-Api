package com.socialmediaapi.controller;

import com.socialmediaapi.dto.PostDTO;
import com.socialmediaapi.model.Post;
import com.socialmediaapi.payload.request.PostRequest;
import com.socialmediaapi.payload.response.MessageResponse;
import com.socialmediaapi.service.ImageService;
import com.socialmediaapi.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@SecurityRequirement(name = "JWT")
@RestController
@RequestMapping(value = "api/v1/posts")
@Tag(name = "Post-controller", description = "контроллер для управления постами")
public class PostController {

    private final PostService postService;
    private final ImageService imageService;

    @Autowired
    public PostController(PostService postService, ImageService imageService) {
        this.postService = postService;
        this.imageService = imageService;
    }

    @Operation(summary = "Добавление нового поста",
            description = "Позволяет создавать новые посты, указывать текст, заголовок и прикреплять изображения.")
    @PostMapping
    public ResponseEntity<PostDTO> createPost(@RequestParam(value = "file", required = false) MultipartFile file,
                                              @RequestParam("title") String title,
                                              @RequestParam("text") String text,
                                              Principal principal) throws IOException {
        String saveFilename = imageService.saveFile(file);
        PostRequest postRequest = new PostRequest();
        postRequest.setTitle(title);
        postRequest.setText(text);

        PostDTO postDTO = postService.createNewPost(postRequest, saveFilename, principal);
        return ResponseEntity.status(HttpStatus.CREATED).body(postDTO);
    }

    @Operation(summary = "Чтение поста пользователя",
            description = "Позволяет прочитать пользователю пост другого пользователя")
    @GetMapping(value = "/{id}")
    public ResponseEntity<PostDTO> readPost(
            @Parameter(description = "Идентификатор поста, который необходимо прочитать")
            @PathVariable("id") Long postId) {
        PostDTO postDTO = postService.readPostById(postId);
        return ResponseEntity.status(HttpStatus.OK).body(postDTO);
    }

    @Operation(summary = "Измение поста",
            description = "Позволяет изменить свой собственный пост, написанный пользователем")
    @PutMapping("/{id}")
    public ResponseEntity<PostDTO> updatePost(
            @Parameter(description = "Идентификатор поста, который необходимо отредактировать")
            @PathVariable Long id,
            @Parameter(description = "Файл изображения к посту")
            @RequestParam(value = "file", required = false) MultipartFile file,
            @Parameter(description = "Заголовок поста")
            @RequestParam("title") String title,
            @Parameter(description = "Текст поста")
            @RequestParam("text") String text,
            Principal principal) throws IOException {
        String saveFilename = imageService.saveFile(file);
        PostRequest postRequest = new PostRequest();
        postRequest.setTitle(title);
        postRequest.setText(text);

        PostDTO updatedPostDTO = postService.updatePost(id, postRequest, saveFilename, principal);
        return ResponseEntity.ok(updatedPostDTO);
    }

    @Operation(summary = "Удаление поста",
            description = "Позволяет пользователю удалить свой собственный пост")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> deletePost(
            @Parameter(description = "Идентификатор поста, который необходимо удалить")
            @PathVariable("id") final Long postId, Principal principal) {
        postService.deletePostById(postId, principal);
        return ResponseEntity.ok(new MessageResponse("Пост был удален"));
    }

    @Operation(summary = "Получение всех постов",
            description = "Позволяет пользователю просмотреть посты другого пользователя")
    @GetMapping(value = "/all/{userId}")
    public ResponseEntity<List<PostDTO>> getAllPosts(
            @Parameter(description = "Идентификатор пользователя, чьи посты надо посмотреть")
            @PathVariable("userId") final Long userId) {
        List<PostDTO> allPosts = postService.getAllPostByUserId(userId);
        return allPosts.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(allPosts);
    }


    @Operation(summary = "Лента активности пользователя",
            description = "Позволяет получить последние посты от других пользователей, на которых он подписан")
    @GetMapping
    public Page<Post> getActivityFeedPosts(Principal principal,
                                           @Parameter(description = "Номер старницы")
                                           @RequestParam(value = "pageNo", required = false, defaultValue = "0") Integer pageNumber,
                                           @Parameter(description = "Количесто страниц")
                                           @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                           @Parameter(description = "Сортировка по времени создания")
                                           @RequestParam(value = "sortBy", required = false, defaultValue = "timeCreate") String sortBy,
                                           @Parameter(description = "Направление сортировки")
                                           @RequestParam(value = "sortDir", required = false, defaultValue = "asc") String sortDir) {
        Page<Post> posts = postService.getPosts(principal, pageNumber, pageSize, sortBy, sortDir);
        return posts;
    }

}
