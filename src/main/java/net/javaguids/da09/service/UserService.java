package net.javaguids.da09.service;

import net.javaguids.da09.dto.UserDto;
import net.javaguids.da09.model.User;

import java.util.List;

public interface UserService {
    UserDto register(UserDto registrationRequest);
    UserDto login(UserDto loginRequest);
    UserDto unlockAccount(UserDto unlockRequest);
    UserDto forgotPassword(String email, String newPassword);
    UserDto changePassword(Long id, String password, String newPassword);
    UserDto refreshToken(UserDto refreshTokenRequest);
    UserDto getAllUsers();
    UserDto getUserById(Long id);
    UserDto getUserByName(String name);
    UserDto updateUser(Long id,UserDto updateRequest);
    UserDto deleteUser(Long id);
    UserDto getMyInfo(String email);
    UserDto verifyEmail(String tokenMail);
    List<User> getUsersByGroupId(Long groupId);
    Long countUsersByGroupId(Long groupId);
}
