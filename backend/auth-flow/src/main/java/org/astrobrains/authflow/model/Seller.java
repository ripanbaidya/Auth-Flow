package org.astrobrains.authflow.model;

import jakarta.persistence.*;
import lombok.*;
import org.astrobrains.authflow.enums.AccountStatus;
import org.astrobrains.authflow.enums.UserRole;

@Entity
@Table(name = "sellers")
@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Seller {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;
    private String phoneNumber;
    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.ROLE_SELLER;

    @Column(name = "is_email_verified")
    private Boolean isEmailVerified = false;

    private AccountStatus accountStatus = AccountStatus.PENDING_VERIFICATION;
}
