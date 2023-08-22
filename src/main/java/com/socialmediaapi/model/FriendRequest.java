package com.socialmediaapi.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Schema(description = "Сущность уведомления")
@Data
@ToString
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "friend_request")
public class FriendRequest {

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Schema(description = "Отправитель заявки в друзья")
    @ManyToOne
    private User sender;
    @Schema(description = "Получатель заявки в друзья")
    @ManyToOne
    private User receiver;

}
