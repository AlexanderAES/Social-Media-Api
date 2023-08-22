package com.socialmediaapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Информация о сообщении")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageDTO {

    @Schema(description = "Идентификатор сообщения")
    private Long id;
    @Schema(description = "Идентификатор отправителя сообщения")
    private Long senderId;
    @Schema(description = "Идентификатор получателя сообщения")
    private Long recipientId;
    @Schema(description = "Текст сообщения")
    private String messageContent;
}
