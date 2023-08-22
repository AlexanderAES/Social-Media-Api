package com.socialmediaapi.service;

import com.socialmediaapi.model.User;
import com.socialmediaapi.payload.RegistrationUserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.security.Principal;
import java.util.Optional;


public interface UserService extends UserDetailsService {

    boolean becomeSubscriber(Long userId, Principal principal);

    boolean acceptToFriend(Long userId, Principal principal);

    boolean sendRequestToAddFriend(Long recipientId, Principal principal);

    void sendFriendRequest(User sender, User receiver);

    boolean unsubscribe(Long userID, Principal principal);

    boolean deleteFromFriends(Long userId, Principal principal);

    User getCurrentUser(Principal principal);

    User loadUserByUserId(Long userId);

    Optional<User> findByUsername(String username);

    void createNewUser(RegistrationUserDto userDto);

    boolean rejectFriendRequest(Long requestId, Principal principal);

    void makeSubscription(User candidate, User currentUser);

    boolean doesFriendRequestAlreadyExist(User recipient, User sender);

}
