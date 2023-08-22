package com.socialmediaapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Schema(description = "Информация о посте")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO {
    @Schema(description = "Идентификатор поста")
    private Long id;
    @Schema(description = "Название поста")
    private String title;
    @Schema(description = "Текст поста")
    private String text;
    @Schema(description = "Имя файла")
    private String filename;
    @Schema(description = "Идентификатор автора поста")
    private Long authorId;
    @Schema(description = "Время создания поста")
    private LocalDateTime timeCreate;
}
