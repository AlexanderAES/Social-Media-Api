package com.socialmediaapi.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
@Schema(description = "Сущность подписки")
@Data
@Entity
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table
public class Subscription {

    @Schema(description = "Идентификатор подписки")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Schema(description = "Пользователь publisher")
    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @Schema(description = "Пользователь подписчик")
    @ManyToOne
    @JoinColumn(name="subscriber_id")
    private User subscriber;

}
