package org.astrobrains.authflow.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "verification_codes")
@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VerificationCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String otp;
    private String email;
    private String phoneNumber;

    // otp creation and expiration time
    private Instant createdAt;
    private Instant expiresAt;

    @PrePersist
    private void prePersist() {
        if (createdAt == null)
            createdAt = Instant.now();
    }

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne
    @JoinColumn(name = "seller_id")
    private Seller seller;
}
