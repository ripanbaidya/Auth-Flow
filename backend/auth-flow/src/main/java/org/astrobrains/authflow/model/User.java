package org.astrobrains.authflow.model;

import jakarta.persistence.*;
import lombok.*;
import org.astrobrains.authflow.enums.Role;

@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "email"),
        @UniqueConstraint(columnNames = "phoneNumber")
})
@Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;
    private String email;
    private String phoneNumber;
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role = Role.ROLE_CUSTOMER;

    private Boolean isEmailVerified = Boolean.FALSE;
}
