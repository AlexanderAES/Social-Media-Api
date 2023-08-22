package com.socialmediaapi.controller;

import com.socialmediaapi.dto.MessageDTO;
import com.socialmediaapi.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@SecurityRequirement(name = "JWT")
@RestController
@RequestMapping("api/v1/messages")
@Tag(name = "Message-controller", description = "контроллер для обмена сообщениями между пользователями")
public class MessageController {

    private final MessageService messageService;


    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }


    @Operation(summary = "Отправка сообщения пользователю",
            description = "Позволяет отправить пользователю сообщение")
    @PostMapping("/send/{recipientId}")
    public ResponseEntity<MessageDTO> sendMessage(
            @Parameter(description = "Идентификатор получателя сообщения")
            @PathVariable("recipientId") Long recipientId,
            @Parameter(description = "Текст сообщения")
            @RequestParam("text") String text,
            Principal principal) {
        MessageDTO message = messageService.sendMessage(recipientId, text, principal);
        return new ResponseEntity<>(message, HttpStatus.OK);

    }


    @Operation(summary = "Чтение сообщения",
            description = "Позволяет прочитать сообщение, если пользователь является получателем этого сообщения")
    @GetMapping("/read/{messageId}")
    public ResponseEntity<MessageDTO> getMessage(
            @Parameter(description = "Идентификатор сообщения, которое необходимо прочитать")
            @PathVariable Long messageId,
            Principal principal) {
        MessageDTO messageDTO = messageService.getMessage(messageId, principal);
        return new ResponseEntity<>(messageDTO, HttpStatus.OK);
    }


    @Operation(summary = "Чтение сообщений",
            description = "Позволяет получить список сообщений которые писал пользователь или которые были адресованы пользователю")
    @GetMapping
    public ResponseEntity<List<MessageDTO>> getMessages(Principal principal) {
        List<MessageDTO> messages = messageService.getMessages(principal);
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

}
