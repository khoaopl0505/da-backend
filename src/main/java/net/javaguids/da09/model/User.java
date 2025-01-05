package net.javaguids.da09.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "user")
@FieldDefaults(level = AccessLevel.PRIVATE)

public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "name")
    String name;

    @Column(name = "email")
    String email;

    @Column(name = "password")
    String password;

    @Column(name = "date_of_birth")
    Date dateOfBirth;

    @CreationTimestamp
    @Column(name = "create_at", updatable = false)
    LocalDateTime createAt;

    @Column(name = "last_login")
    LocalDateTime lastLoginAt;

    @Column(name = "failed_attempts")
    private Integer failedAttempts = 0;

    @Column(name = "is_locked")
    private boolean isLocked = false;

    @Column(name = "status_account")
    boolean enable;

    @Column(name = "role")
    Role role;

    @Column(name = "token_mail")
    String tokenMail;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_user_group")
    private UserGroup group;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enable;
    }
}
