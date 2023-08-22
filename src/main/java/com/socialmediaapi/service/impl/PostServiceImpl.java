package com.socialmediaapi.service.impl;

import com.socialmediaapi.dto.PostDTO;
import com.socialmediaapi.exceptions.PostNotFoundException;
import com.socialmediaapi.model.Post;
import com.socialmediaapi.model.User;
import com.socialmediaapi.payload.request.PostRequest;
import com.socialmediaapi.repository.PostRepository;
import com.socialmediaapi.repository.UserRepository;
import com.socialmediaapi.service.PostService;
import com.socialmediaapi.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.lang.reflect.Type;
import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class PostServiceImpl implements PostService {

    /**
     * listType представляет тип данных List<PostDTO>.
     * Этот код используется для получения типа данных List<PostDTO>,
     * который используется при сериализации и десериализации объектов типа List<PostDTO>.
     */
    private static final Type listType = new TypeToken<List<PostDTO>>() {
    }.getType();

    private final UserService userDetailsService;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public PostServiceImpl(UserService userDetailsService,
                           PostRepository postRepository,
                           UserRepository userRepository,
                           ModelMapper modelMapper) {
        this.userDetailsService = userDetailsService;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    /**
     * Метод позволяет создать новый пост.
     *
     * @param postRequest информация поста.
     * @param filename    имя файла передаваемого с постом.
     * @param principal   аутентифицированный пользователь, создатель поста.
     * @return PostDTO.
     */
    @Override
    public PostDTO createNewPost(PostRequest postRequest, String filename, Principal principal) {
        User currentUser = userDetailsService.getCurrentUser(principal);
        Optional<User> findUser = userRepository.findUserById(currentUser.getId());
        if (findUser.isPresent()) {
            Post post = modelMapper.map(postRequest, Post.class);
            User author = new User();
            author.setId(currentUser.getId());
            post.setUser(author);
            post.setDateTime();
            post.setFilename(filename);
            Post savedPost = postRepository.save(post);
            log.info("New post has been created by user with id {}", post.getUser().getId());
            PostDTO postDTO = modelMapper.map(savedPost, PostDTO.class);
            postDTO.setAuthorId(currentUser.getId());
            return postDTO;
        }
        return new PostDTO();
    }

    /**
     * Метод позволяет найти пользователя по его идентификатору.
     *
     * @param userId идентификатор пользователя.
     * @return User.
     */
    public User findUserById(Long userId) {
        log.info("Find user by id {}", userId);
        return userDetailsService.loadUserByUserId(userId);
    }

    /**
     * Метод позволяет провести верификацию поста и пользователя
     * пытающегося выпонить действие с постом.
     *
     * @param postId    идентфикатор поста.
     * @param principal аутентифицированный пользователь, пытающийся выполнить действие с постом.
     * @return true если пост принадлежит пользователю, иначе false.
     */
    boolean userPostVerify(Long postId, Principal principal) {
        User ownerPostUser = getUserByPostId(postId);
        User currentUser = userDetailsService.getCurrentUser(principal);
        return ownerPostUser.equals(currentUser);

    }

    /**
     * Метод позволяет получить пользователя по идентфикатору поста.
     *
     * @param postId идентифкатор поста.
     * @return User
     */
    private User getUserByPostId(Long postId) {
        log.info("Get user by post id {}", postId);
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post with id " + postId + " not found"));
        Long userId = post.getUser().getId();
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + userId + " not found"));
    }

    /**
     * Метод позвоялет удалить пост написанный пользователем,
     * при условии что пользователь является автором поста.
     *
     * @param postId    идентфикатор поста.
     * @param principal аутентифицированный пользователь, пытающийся выполнить удаление поста.
     */
    @Transactional
    @Override
    public void deletePostById(Long postId, Principal principal) {
        if (userPostVerify(postId, principal)) {
            postRepository.deletePostById(postId);
            log.info("Deleted post by id {}", postId);
        }
    }

    /**
     * Метод позволяет прочитать пост по идентифкатору поста.
     *
     * @param postId идентфикатор поста.
     * @return PostDTO c информацией о посте.
     */
    @Override
    public PostDTO readPostById(Long postId) {
        log.info("Read post by id {}", postId);
        Post readPost = postRepository.getPostById(postId).orElseThrow(() ->
                new PostNotFoundException("Post with id: " + postId + " not found"));
        PostDTO postDTO = modelMapper.map(readPost, PostDTO.class);
        postDTO.setAuthorId(readPost.getUser().getId());
        return postDTO;
    }

    /**
     * Метод позволяет получить список всех постов написанных пользователем с идентфикатором userId.
     *
     * @param userId идентификатор пользователя, чьи посты пользователь будет читать.
     * @return List<PostDTO>.
     */
    @Override
    public List<PostDTO> getAllPostByUserId(Long userId) {
        User foundUser = findUserById(userId);
        if (foundUser == null) {
            throw new NotFoundException("Пользователь с id не найден:" + userId);
        }

        if (foundUser.getPosts() == null || foundUser.getPosts().isEmpty()) {
            return Collections.emptyList();
        }

        return modelMapper.map(foundUser.getPosts(), listType);
    }

    /**
     * Метод позволяет получить пост по его идентификатору.
     *
     * @param id          идентификатор пользователя.
     * @param currentUser текущий пользователь, выполняющий действие с постом.
     * @return Post.
     */
    private Post getPostByIdAndUser(Long id, User currentUser) {
        return postRepository.findByIdAndUser(id, currentUser)
                .orElseThrow(() -> new PostNotFoundException("Post not found with id : " + id));
    }

    /**
     * Метод позволяет преобразовать объект PostRequest в объект Post, используется в методе
     * updatePost.
     *
     * @param postRequest сущность запроса поста, с отправляемой информацией.
     * @param post        сущность поста.
     */
    private void mapAndSavePost(PostRequest postRequest, Post post) {
        modelMapper.map(postRequest, post);
        postRepository.save(post);
        log.info("Updated post with id {}", post.getId());
    }


    /**
     * Метод позволяет изменить пост, созданный пользователем.
     *
     * @param id          идентификатор поста.
     * @param postRequest сущность запроса поста, с отправляемой информацией.
     * @param fileName    имя файла, отправляемого вместе с постом.
     * @param principal   аутентифицированный пользователь, пытающийся выполнить изменение поста.
     * @return postDTO.
     */
    @Override
    public PostDTO updatePost(Long id, PostRequest postRequest, String fileName, Principal principal) {
        User currentUser = userDetailsService.getCurrentUser(principal);
        Post post = getPostByIdAndUser(id, currentUser);
        post.setTitle(postRequest.getTitle());
        post.setText(postRequest.getText());
        post.setFilename(fileName);
        PostDTO postDTO = modelMapper.map(post, PostDTO.class);
        postDTO.setAuthorId(post.getUser().getId());
        mapAndSavePost(postRequest, post);
        return postDTO;
    }


    /**
     * Метод позволяет получить последние посты от других пользователей,
     * на которых он подписан(лента активности пользователя).
     *
     * @param principal аутентифицированный пользователь.
     * @param pageNo    номер страницы.
     * @param pageSize  количество страниц.
     * @param sortBy    сортировка.
     * @param sortDir   направление сортировки.
     * @return Page<Post>.
     */
    @Override
    public Page<Post> getPosts(Principal principal, int pageNo, int pageSize,
                               String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        User user = userDetailsService.getCurrentUser(principal);
        return postRepository.getPostsAllAuthorsWhichSubscription(user.getId(), pageable);
    }

}
