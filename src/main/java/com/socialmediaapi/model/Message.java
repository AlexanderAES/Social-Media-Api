package com.socialmediaapi.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Schema(description = "Сущность сообщение")
@Data
@ToString
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "messages")
public class Message {

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    private User sender;

    @ManyToOne
    private User receiver;

    private String messageContent;

    @Column(name = "timestamp")
    private LocalDateTime createDate;

    @PrePersist
    protected void init() {
        createDate = LocalDateTime.now();
    }
}
