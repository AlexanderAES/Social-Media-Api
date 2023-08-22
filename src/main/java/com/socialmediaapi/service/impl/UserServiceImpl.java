package com.socialmediaapi.service.impl;

import com.socialmediaapi.model.FriendRequest;
import com.socialmediaapi.model.Role;
import com.socialmediaapi.model.Subscription;
import com.socialmediaapi.model.User;
import com.socialmediaapi.payload.RegistrationUserDto;
import com.socialmediaapi.repository.FriendRequestRepository;
import com.socialmediaapi.repository.SubscriptionRepository;
import com.socialmediaapi.repository.UserRepository;
import com.socialmediaapi.service.RoleService;
import com.socialmediaapi.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private PasswordEncoder passwordEncoder;
    private RoleService roleService;
    private UserRepository userRepository;
    private FriendRequestRepository friendRequestRepository;
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    public void setFriendRequestRepository(FriendRequestRepository friendRequestRepository) {
        this.friendRequestRepository = friendRequestRepository;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void setSubscriptionRepository(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    /**
     * Метод позволяет узнать существует ли указанный пользователь в БД,
     * по его username.
     *
     * @param username String имя пользователя.
     * @return UserDetails.
     * @throws UsernameNotFoundException если пользователь не найден.
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUsername(username).orElseThrow(() ->
                new UsernameNotFoundException(String.format("User not found with username '%s': ", username)));
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority(role.getName()))
                        .collect(Collectors.toList()));
    }

    /**
     * Метод позволяет создать нового пользователя.
     *
     * @param userDto информация о пользователе.
     */
    @Transactional
    public void createNewUser(RegistrationUserDto userDto) {
        Role role_user = roleService.getUserRole();
        User user = new User(userDto.getUsername(), userDto.getEmail(),
                passwordEncoder.encode(userDto.getPassword()), List.of(role_user));
        userRepository.save(user);
    }

    /**
     * Метод позволяет отклонить заявку на добавление в друзья.
     * Проверяет является ли текущий пользователь получателем заявки на дружбу.
     *
     * @param requestId идентфикатор запроса на дружбу.
     * @param principal аутентифицированный пользователь.
     * @return true, если успешно, иначе false.
     */
    @Transactional
    @Override
    public boolean rejectFriendRequest(Long requestId, Principal principal) {
        User currentUser = getCurrentUser(principal);

        Optional<FriendRequest> request = friendRequestRepository.findById(requestId);
        if (request.isPresent()) {
            FriendRequest friendRequest = request.get();
            // если текущий пользователь является получателем заявки на дружбу
            if (currentUser.getId() == friendRequest.getReceiver().getId()) {
                friendRequestRepository.delete(friendRequest);
                return true;
            }
        }
        return false;
    }


    /**
     * Метод отправляет запрос на
     * установление дружбы между пользователями.
     *
     * @param sender   пользователь отправитель.
     * @param receiver пользователь получатель.
     */
    @Transactional
    @Override
    public void sendFriendRequest(User sender, User receiver) {
        FriendRequest newRequest = new FriendRequest();
        newRequest.setReceiver(receiver);
        newRequest.setSender(sender);
        friendRequestRepository.save(newRequest);
    }

    /**
     * Метод находит пользователя по его username.
     *
     * @param username имя пользователя.
     * @return Optional.
     */
    public Optional<User> findByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    /**
     * Получение текущего пользователя из объекта Principal.
     *
     * @param principal пользователь, который может быть аутентифицирован.
     * @return User.
     */
    public User getCurrentUser(Principal principal) {
        String username = principal.getName();
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found with username "
                        + username));
    }

    /**
     * Метод производит поиск пользователя по id.
     *
     * @param userId идентификатор пользователя.
     * @return User. Найденный пользователь.
     * @throws UsernameNotFoundException если пользователь не найден.
     */
    public User loadUserByUserId(Long userId) {
        return userRepository.findUserById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found with id " + userId));
    }


    /**
     * Метод позволяет подписаиться на другого пользователя.
     *
     * @param userId    идентификатор пользователя, на которого происходит подписка.
     * @param principal аутентифицированный пользователь, который делает подписку.
     * @return true, если успешно, иначе false.
     */
    @Transactional
    @Override
    public boolean becomeSubscriber(Long userId, Principal principal) {
        User currentUser = getCurrentUser(principal);
        Optional<User> findUser = userRepository.findUserById(userId);

        if (findUser.isPresent()) {
            User publisherUser = findUser.get();
            Subscription subscription = new Subscription();
            subscription.setSubscriber(currentUser);
            subscription.setUser(publisherUser);
            subscriptionRepository.save(subscription);
            return true;
        }
        return false;
    }

    /**
     * Метод позволяет одобрить запрос на установление дружбы между пользователями.
     *
     * @param userId    идентификатор пользователя, которого добавляют в список друзей.
     * @param principal аутентифицированный пользователь одобряющий запрос.
     * @return true если успешно, иначе false.
     */
    @Transactional
    @Override
    public boolean acceptToFriend(Long userId, Principal principal) {

        User currentUser = getCurrentUser(principal);
        Optional<User> candidate = userRepository.findUserById(userId);

        if (candidate.isPresent()) {
            // устанавливаем подписку между пользователями
            makeSubscription(candidate.get(), currentUser);

            User candidateToFriends = candidate.get();
            Set<User> currentUserFriends = currentUser.getFriends();
            currentUserFriends.add(candidateToFriends);
            currentUser.setFriends(currentUserFriends);
            userRepository.save(currentUser);
            log.info("Add user with id {} to friends list", candidateToFriends.getId());

            Set<User> friendsCandidate = candidateToFriends.getFriends();
            friendsCandidate.add(currentUser);
            candidateToFriends.setFriends(friendsCandidate);
            userRepository.save(candidateToFriends);
            log.info("Added to the candidate as friends");
            return true;
        }
        return false;
    }

    /**
     * Метод используется для отправки запроса на дружбу
     * текущим (аутентифицированным пользователем).
     * если запрос уже есть, то выдаем сообщение, что он уже есть и надо ждать одобрения.
     *
     * @param recipientId пользователь получатель запроса.
     * @param principal   аутентифицированный пользователь отправитель запроса.
     * @return true, если запрос произведен впервые, иначе false, если запрос был найден.
     */
    public boolean sendRequestToAddFriend(Long recipientId, Principal principal) {
        User currentUser = getCurrentUser(principal);
        Optional<User> findUser = userRepository.findUserById(recipientId);

        if (findUser.isPresent()) {
            User recipientUser = findUser.get();

            if (!doesFriendRequestAlreadyExist(recipientUser, currentUser)) {
                sendFriendRequest(currentUser, recipientUser);
                return true;
            }
        }
        return false;
    }

    /**
     * Метод проверяет был ли ранее подан запрос на добавление в друзья.
     *
     * @param recipient пользователь получивший запрос.
     * @param sender    пользователь отправивший запрос.
     * @return true, если запрос найден, иначе false.
     */
    @Override
    public boolean doesFriendRequestAlreadyExist(User recipient, User sender) {
        return friendRequestRepository.findBySenderAndReceiver(sender, recipient).isPresent();
    }

    /**
     * Метод удаяляет пользователя по его id
     * из коллекции аутентифицированного пользователя.
     *
     * @param userId      идентификатор пользователя, которого удаляем.
     * @param currentUser текущий (аутентифицированный пользователь).
     * @param usersList   коллекция из которой удалям пользователя.
     * @return true если удалено успешно, иначе false.
     */
    private boolean doIterator(Long userId, User currentUser, Set<User> usersList) {
        Iterator<User> iterator = usersList.iterator();
        while (iterator.hasNext()) {
            User user = iterator.next();
            if (user.getId() == userId) {
                iterator.remove();
                currentUser.setFriends(usersList);
                userRepository.save(currentUser);
                return true;
            }
        }
        return false;
    }

    /**
     * Метод удаляет пользователя из списка друзей
     * у аутентифицированного пользователя по userId.
     *
     * @param userId    идентификатор удаляемого пользователя.
     * @param principal сущность пользователя, которая может быть аутентифицирована.
     * @return
     */
    @Transactional
    @Override
    public boolean deleteFromFriends(Long userId, Principal principal) {
        User currentUser = getCurrentUser(principal);
        Set<User> currentFriends = currentUser.getFriends();
        // удаляем подписку на пользователя
        unsubscribe(userId, principal);
        // удаляем из списка друзей
        return doIterator(userId, currentUser, currentFriends);
    }


    /**
     * Метод используется для отписки от пользователя.
     *
     * @param userID    идентификатор пользователя от которого
     *                  необходимо отписаться.
     * @param principal сущность которая может быть аутентифицирована.
     * @return true или false.
     */
    @Transactional
    @Override
    public boolean unsubscribe(Long userID, Principal principal) {
        User currentUser = getCurrentUser(principal);
        int countDelRow = subscriptionRepository.deleteSubscriptionByUserIdAndSubscriberId(userID, currentUser.getId());
        return countDelRow > 0;
    }


    /**
     * Метод используется для подписки на посты пользователя.
     *
     * @param candidate   пользователь на которого происходит подписка.
     * @param currentUser сущность которая была аутентифицирована.
     */
    @Transactional
    @Override
    public void makeSubscription(User candidate, User currentUser) {
        Subscription subsCurrentUser = new Subscription();
        Subscription subsCandidate = new Subscription();

        subsCurrentUser.setSubscriber(currentUser);
        subsCurrentUser.setUser(candidate);
        subsCandidate.setSubscriber(candidate);
        subsCandidate.setSubscriber(currentUser);

        subscriptionRepository.save(subsCurrentUser);
        subscriptionRepository.save(subsCandidate);
    }

}
