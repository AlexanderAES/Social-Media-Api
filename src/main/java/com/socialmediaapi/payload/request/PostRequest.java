package com.socialmediaapi.payload.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Schema(description = "Сущность запроса поста")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostRequest {
    @Schema(description = "Название поста")
    private String title;
    @Schema(description = "Текст поста")
    private String text;
    @Schema(description = "Прикрепляемый файл изображения")
    private MultipartFile file;
}
