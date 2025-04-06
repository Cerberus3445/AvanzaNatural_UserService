package com.cerberus.userservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Builder
@Table(schema = "users", name = "confirmation_code")
@AllArgsConstructor
@NoArgsConstructor
public class ConfirmationCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer code;

    @Temporal(value = TemporalType.TIMESTAMP)
    private LocalDateTime expirationDate;

    @Enumerated(value = EnumType.STRING)
    private Type type;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
}
