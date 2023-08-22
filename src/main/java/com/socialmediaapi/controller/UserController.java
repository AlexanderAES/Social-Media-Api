package com.socialmediaapi.controller;

import com.socialmediaapi.payload.response.MessageResponse;
import com.socialmediaapi.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@SecurityRequirement(name = "JWT")
@RestController
@RequestMapping(value = "api/v1/users")
@Tag(name = "User-controller", description = "контроллер для взаимодействия пользователей")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     *
     * @param userId
     * @param principal
     * @return message
     */
    @Operation(summary = "Подписка на пользователя",
            description = "Позволяет подписаться на посты другого пользователя.")
    @PostMapping(value = "/subscriptions/{userId}")
    public ResponseEntity<MessageResponse> becomeSubscriber(
            @Parameter(description = "Идентификатор пользователя на чьи посты необходимо подписаться")
            @PathVariable("userId") Long userId, Principal principal) {
        return (userService.becomeSubscriber(userId, principal) ?
                new ResponseEntity<>(new MessageResponse("Вы успешно подписались на посты пользователя с id: " + userId), HttpStatus.OK) :
                new ResponseEntity<>(new MessageResponse("Действие запрещено..."), HttpStatus.FORBIDDEN));
    }

    /**
     *
     * @param recipientId
     * @param principal
     * @return
     */
    @Operation(summary = "Заявка на добавление в друзья к другому пользователю ",
            description = "Позволяет отправить заявку на добавление в друзья к другому пользователю, также становясь его подписчиком.")
    @PostMapping(value = "/friend-request/{recipientId}")
    public ResponseEntity<MessageResponse> sendRequestToBeFriend(
            @Parameter(description = "Идентификатор получателя заявки, к кому необходимо добавться в список друзей")
            @PathVariable("recipientId") Long recipientId, Principal principal) {
        return (userService.sendRequestToAddFriend(recipientId, principal) ?
                new ResponseEntity<>(new MessageResponse("Вы успешно отправили запрос на добавление в друзья к пользователю с id: " +
                        recipientId), HttpStatus.OK) :
                new ResponseEntity<>(new MessageResponse("Вы уже подавали заявку на добавление в друзья"), HttpStatus.FORBIDDEN));
    }

    /**
     *
     * @param userId
     * @param principal
     * @return
     */
    @Operation(summary = "Подтверждение заявки в друзья",
            description = "Позволяет подтвердить заявку другого пользователя на добавление к себе в друзья. Друзья становятся подписчиками друг на друга.")
    @PostMapping(value = "/accept/{userId}")
    public ResponseEntity<MessageResponse> becomeFriend(
            @Parameter(description = "Идентификатор пользователя, которого добавляем в друзья")
            @PathVariable("userId") Long userId, Principal principal) {
        return (userService.acceptToFriend(userId, principal) ?
                new ResponseEntity<>(new MessageResponse("Вы успешно добавили в друзья пользователя с id: " + userId), HttpStatus.OK) :
                new ResponseEntity<>(new MessageResponse("Действие запрещено..."), HttpStatus.FORBIDDEN));
    }

    @Operation(summary = "Отклонение запроса на дружбу.",
            description = "Позволяет отклонить запрос на дружбу.")
    @PostMapping("/reject/{requestId}")
    public ResponseEntity<MessageResponse> rejectFriendRequest(
            @Parameter(description = "Идентификатор запроса на установление дружбы")
            @PathVariable("requestId") Long requestId, Principal principal) {
        return (userService.rejectFriendRequest(requestId, principal)) ?
                new ResponseEntity<>(new MessageResponse("Успешно отклонена заявка на дружбу"), HttpStatus.OK) :
                new ResponseEntity<>(new MessageResponse("Такой заявки нет."), HttpStatus.FORBIDDEN);
    }

    @Operation(summary = "Отписка от пользователя",
            description = "Позволяет пользователям отписаться от других пользователей")
    @PostMapping("/unsubscribe/{userId}")
    public ResponseEntity<MessageResponse> unsubscribe(
            @Parameter(description = "Идентификатор пользователя от необходимо отписаться")
            @PathVariable("userId") Long userId, Principal principal) {
        return (userService.unsubscribe(userId, principal) ?
                new ResponseEntity<>(new MessageResponse("Вы успешно отписались от пользователя"), HttpStatus.OK) :
                new ResponseEntity<>(new MessageResponse("Действие запрещено..."), HttpStatus.FORBIDDEN));
    }

    @Operation(summary = "Удаление пользователя из друзей",
            description = "Позволяет пользователям удалять других пользователей из их списка друзей, также отписываясь от них.")
    @DeleteMapping("/unfriend/{userId}")
    public ResponseEntity<MessageResponse> deleteFriend(
            @Parameter(description = "Идентификатор пользователя, которого удаляем из списка друзей.")
            @PathVariable("userId") Long userId, Principal principal) {
        return (userService.deleteFromFriends(userId, principal) ?
                new ResponseEntity<>(new MessageResponse("Пользователь удален из списка ваших друзей"), HttpStatus.OK) :
                new ResponseEntity<>(new MessageResponse("Не получилось удалить пользователя, действие запрещено"), HttpStatus.FORBIDDEN));
    }

}
