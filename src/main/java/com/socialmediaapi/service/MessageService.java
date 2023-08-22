package com.socialmediaapi.service;

import com.socialmediaapi.dto.MessageDTO;
import com.socialmediaapi.model.Message;

import java.security.Principal;
import java.util.List;

public interface MessageService {

    MessageDTO sendMessage(Long recipientId, String text, Principal principal);

    MessageDTO getMessage(Long messageId, Principal principal);

    List<MessageDTO> getMessages(Principal principal);
}
