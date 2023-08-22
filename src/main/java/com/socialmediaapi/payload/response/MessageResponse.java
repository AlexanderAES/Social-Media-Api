package com.socialmediaapi.payload.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Сущность показываемого сообщения, при возвращении результата")
public class MessageResponse {

    @Schema(description = "Текст сообщения")
    private String message;

    public MessageResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "MessageResponse{" +
                "message='" + message + '\'' +
                '}';
    }
}
