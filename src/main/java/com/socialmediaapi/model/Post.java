package com.socialmediaapi.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.Date;

@Schema(description = "Сущность поста")
@Data
@Entity
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "posts")
public class Post {

    @Schema(description = "идентификатор поста")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Schema(description = "Заголовок поста")
    @NotBlank(message = "Пожалуйста заполните заголовок")
    @Length(max = 255, message = "Превышена макисмальная длина (255)")
    @Column(name = "title")
    private String title;

    @Schema(description = "текст поста")
    @Length(max = 4096, message = "Превышена максимальная длина (4KB)")
    @Column(name = "text")
    private String text;

    @Schema(description = "Пользователь автор поста")
    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

    @Schema(description = "название файла")
    @Column(name = "filename")
    private String filename;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime timeCreate;

    public void setDateTime() {
        this.timeCreate = LocalDateTime.now();
    }
}
