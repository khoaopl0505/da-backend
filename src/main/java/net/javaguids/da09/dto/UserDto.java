package net.javaguids.da09.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.javaguids.da09.model.UserGroup;
import net.javaguids.da09.model.Role;
import net.javaguids.da09.model.User;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto {
    Long id;
    String name;
    String email;
    String password;
    Date dateOfBirth;
    LocalDateTime createAt;
    LocalDateTime lastLoginAt;
    String tokenMail;
    String newPassword;
    int statusCode;
    String error;
    String message;
    String token;
    String refreshToken;
    String expirationTime;
    UserGroup group;
    Role role;
    User user;
    List<User> listUsers;
}
