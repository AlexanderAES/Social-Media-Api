package com.socialmediaapi.service.impl;

import com.socialmediaapi.dto.MessageDTO;
import com.socialmediaapi.model.Message;
import com.socialmediaapi.model.User;
import com.socialmediaapi.repository.MessageRepository;
import com.socialmediaapi.repository.UserRepository;

import com.socialmediaapi.service.MessageService;
import com.socialmediaapi.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final UserService userService;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;


    @Autowired
    public MessageServiceImpl(MessageRepository messageRepository,
                              UserService userService,
                              UserRepository userRepository,
                              ModelMapper modelMapper
    ) {
        this.messageRepository = messageRepository;
        this.userService = userService;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;

    }

    /**
     * Метод используется для отправки сообщения пользователю,
     * при условии что оба пользователя и отправитель и получатель являются друзьями.
     *
     * @param recipientId идентификатор получателя сообщения.
     * @param text        передаваемый текст сообщения.
     * @param principal   аутентифицированный пользователь, отправитель сообщения.
     * @return MessageDTO содержащий информацию об отправленном сообщении
     * если пользовтаели имееют отношение дружбы , иначе возвращает пустой MessageDTO.
     */
    @Transactional
    @Override
    public MessageDTO sendMessage(Long recipientId, String text, Principal principal) {
        User currentUser = userService.getCurrentUser(principal);
        Optional<User> findUser = userRepository.findUserById(recipientId);
        Message message = new Message();
        if (findUser.isPresent()) {
            User recipientUser = findUser.get();
            if (!userService.doesFriendRequestAlreadyExist(recipientUser, currentUser)) {
                message.setSender(currentUser); // отправитель сообщения
                message.setReceiver(recipientUser); // получатель сообщения
                message.setMessageContent(text);
                messageRepository.save(message);
                MessageDTO messageDTO = modelMapper.map(message, MessageDTO.class);
                messageDTO.setRecipientId(recipientUser.getId());
                return messageDTO;
            }
        }
        return new MessageDTO();
    }


    /**
     * Метод позволяет пользователю прочитать сообщение по идентификаиору сообщения,
     * если это сообщение было отправлено именно этому пользователю.
     *
     * @param messageId идентификатор сообщения.
     * @param principal аутентифицированный пользователь, получатель сообщения.
     * @return MessageDTO содержащий информацию об отправленном сообщении,
     * если это сообщение было отправлено именно этому пользователю, иначе возвращает пустой MessageDTO.
     */
    @Override
    public MessageDTO getMessage(Long messageId, Principal principal) {
        User currentUser = userService.getCurrentUser(principal);
        Optional<Message> message = messageRepository.getMessageById(messageId);

        if (message.isPresent()) {
            Message currentMessage = message.get();
            if (currentUser.getId() == currentMessage.getReceiver().getId()) {
                MessageDTO messageDTO = modelMapper.map(message, MessageDTO.class);
                messageDTO.setRecipientId(currentMessage.getReceiver().getId());
                return messageDTO;
            }
        }
        return new MessageDTO();
    }

    /**
     * Метод позволяет получить все сообщения пользователя,
     * те которые он отправлял сам и являлся получателем сообщений отправленных ему.
     *
     * @param principal аутентифицированный пользователь.
     * @return List<MessageDTO>.
     */
    @Override
    public List<MessageDTO> getMessages(Principal principal) {
        User currentUser = userService.getCurrentUser(principal);
        return messageRepository.findBySenderIdOrReceiverId(currentUser.getId(), currentUser.getId());
    }

}
