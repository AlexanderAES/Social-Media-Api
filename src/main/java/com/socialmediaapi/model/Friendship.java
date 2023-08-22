package com.socialmediaapi.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;

@Schema(description = "Сущность дружеское отношение")
@Entity
@Table(name = "friendships")
@AllArgsConstructor
@Data
public class Friendship {

    @Schema(description = "Идентификатор дружеского отношения")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "Идентификатор первого пользователя")
    @Column(name = "user_id1")
    private Long userId1;

    @Schema(description = "Идентификатор второго пользователя")
    @Column(name = "user_id2")
    private Long userId2;

    public Friendship() {
    }

}
