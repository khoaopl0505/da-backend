package net.javaguids.da09.service.imp;

import net.javaguids.da09.dto.UserDto;
import net.javaguids.da09.model.User;
import net.javaguids.da09.model.UserGroup;
import net.javaguids.da09.repository.GroupRepository;
import net.javaguids.da09.repository.UserRepository;
import net.javaguids.da09.service.EmailService;
import net.javaguids.da09.service.JwtUtils;
import net.javaguids.da09.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImp implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private EmailService emailService;
    @Autowired
    private GroupRepository groupRepository;

    private static final int MAX_FAILED_ATTEMPTS = 3;
    @Override
    public UserDto register(UserDto registrationRequest) {

        UserDto userDto = new UserDto();
        try{
            if(userRepository.existsByEmail(registrationRequest.getEmail())){
                userDto.setMessage("Email already exists");
                userDto.setStatusCode(400);
                return userDto;
            }

            User user = new User();
            user.setEmail(registrationRequest.getEmail());
            user.setName(registrationRequest.getName());
            user.setRole(registrationRequest.getRole());
            user.setDateOfBirth(registrationRequest.getDateOfBirth());
            user.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
            user.setCreateAt(LocalDateTime.now());
            String generateTokenMail = UUID.randomUUID().toString();
            user.setTokenMail(generateTokenMail);
            emailService.sendEmailVerifyAccount(registrationRequest.getEmail(), generateTokenMail);
            user.setEnable(false);
            User newUser = userRepository.save(user);
            if(newUser.getId() != null){
                userDto.setUser(newUser);
                userDto.setMessage("User Saved Successfully");
                userDto.setStatusCode(200);
            }
        }catch (Exception e){
            userDto.setMessage(e.getMessage());
            userDto.setStatusCode(500);
        }
        return userDto;
    }

    @Override
    public UserDto login(UserDto loginRequest) {
        UserDto userDto = new UserDto();
        try{
            User user = userRepository.findByEmail(loginRequest.getEmail())
                    .orElseThrow(()-> new UsernameNotFoundException("User not found"));
            if(user.isLocked()){
                userDto.setStatusCode(403);
                userDto.setMessage("Account is locked, please contact support.");
            return userDto;
            }
            try {
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
                );
            user.setFailedAttempts(0);
            user.setLastLoginAt(LocalDateTime.now());
            userRepository.save(user);
            var jwt = jwtUtils.generateToken(user);
            var refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), user);
            userDto.setMessage("Successfully logged in");
            userDto.setStatusCode(200);
            userDto.setToken(refreshToken);
            userDto.setRole(user.getRole());
            userDto.setExpirationTime("24 hours");
            }catch (BadCredentialsException e){
                user.setFailedAttempts(user.getFailedAttempts()+1);
                if(user.getFailedAttempts() >= MAX_FAILED_ATTEMPTS){
                    user.setLocked(true);
                    userDto.setStatusCode(403);
                    userDto.setMessage("Account locked due to multiple failed attempts");
                }else {
                    userDto.setStatusCode(401);
                    userDto.setMessage("Invalid credentials. Attempts left: " + (MAX_FAILED_ATTEMPTS - user.getFailedAttempts()));
                }
                userRepository.save(user);
                return userDto;
            }
        }catch (UsernameNotFoundException e){
            userDto.setStatusCode(404);
            userDto.setMessage("User not found");
        }catch (Exception e){
            userDto.setStatusCode(500);
            userDto.setMessage(e.getMessage());
        }
        return userDto;
    }

    @Override
    public UserDto unlockAccount(UserDto unlockRequest) {
        UserDto userDto = new UserDto();
        try{
            User user = userRepository.findByEmail(unlockRequest.getEmail())
                    .orElseThrow(()->new UsernameNotFoundException("User not found"));
            String tokenMail = UUID.randomUUID().toString();
            user.setTokenMail(tokenMail);
            emailService.sendEmailVerifyToUnlock(unlockRequest.getEmail(), tokenMail);
            userRepository.save(user);
            userDto.setStatusCode(200);
            userDto.setMessage("Check your email confirm to unlock account");
        }catch (UsernameNotFoundException e){
            userDto.setStatusCode(404);
            userDto.setMessage("User not found");
        }catch (Exception e){
            userDto.setStatusCode(500);
            userDto.setMessage(e.getMessage());
        }
        return userDto;
    }

    @Override
    public UserDto forgotPassword(String email, String newPassword) {
        UserDto userDto = new UserDto();
        try{
            Optional<User> userOptional = userRepository.findByEmail(email);
            if(userOptional.isPresent()){
                User user = userOptional.get();
                if(newPassword != null && !newPassword.isEmpty()){
                    user.setPassword(passwordEncoder.encode(newPassword));
                    String generateTokenMail = UUID.randomUUID().toString();
                    user.setTokenMail(generateTokenMail);
                    emailService.sendEmailVerifyToChangePassword(user.getEmail(), generateTokenMail);
                    user.setEnable(false);
                    User saveUser = userRepository.save(user);
                    userDto.setUser(saveUser);
                    userDto.setStatusCode(200);
                    userDto.setMessage("Check your email to confirm change password");
                }else{
                    userDto.setStatusCode(400);
                    userDto.setMessage("New password cannot be null or empty");
                }
            }else {
                userDto.setStatusCode(404);
                userDto.setMessage("User not found to change password");
            }
        }catch (Exception e){
            userDto.setStatusCode(500);
            userDto.setMessage("Error occurred while updating password: " + e.getMessage());
        }
        return userDto;
    }



    @Override
    public UserDto changePassword(Long id, String password, String newPassword) {
        UserDto userDto = new UserDto();
        try{
            Optional<User> userOptional = userRepository.findById(id);
            if(userOptional.isPresent()){
                User user = userOptional.get();
                if(passwordEncoder.matches(password, user.getPassword())){
                    if(newPassword != null && !newPassword.isEmpty()){
                        user.setPassword(passwordEncoder.encode(newPassword));
                        String tokenMail = UUID.randomUUID().toString();
                        user.setTokenMail(tokenMail);
                        emailService.sendEmailVerifyToChangePassword(user.getEmail(), tokenMail);
                        user.setEnable(false);
                        User savedUser = userRepository.save(user);

                        userDto.setUser(savedUser);
                        userDto.setStatusCode(200);
                        userDto.setMessage("Check email to confirm change password");
                    } else {
                        userDto.setStatusCode(400);
                        userDto.setMessage("New password cannot be null or empty");
                    }
                }else {
                    userDto.setStatusCode(400);
                    userDto.setMessage("Old password is incorrect");
                }
            }else {
                userDto.setStatusCode(404);
                userDto.setMessage("User not found for password update");
            }
        }catch (Exception e) {
            userDto.setStatusCode(500);
            userDto.setMessage("Error occurred while updating password: " + e.getMessage());
        }
        return userDto;
    }

    @Override
    public UserDto refreshToken(UserDto refreshTokenRequest) {
        UserDto userDto = new UserDto();
        try{
            String ourEmail = jwtUtils.extractUsername(refreshTokenRequest.getToken());
            User users = userRepository.findByEmail(ourEmail).orElseThrow();
            if (jwtUtils.isTokenValid(refreshTokenRequest.getToken(), users)) {
                var jwt = jwtUtils.generateToken(users);
                userDto.setStatusCode(200);
                userDto.setToken(jwt);
                userDto.setRefreshToken(refreshTokenRequest.getToken());
                userDto.setExpirationTime("24Hr");
                userDto.setMessage("Successfully Refreshed Token");
            }
            userDto.setStatusCode(200);
        }catch (Exception e){
            userDto.setStatusCode(500);
            userDto.setMessage(e.getMessage());
        }
        return userDto;
    }

    @Override
    public UserDto getAllUsers() {
        UserDto userDto = new UserDto();

        try {
            List<User> result = userRepository.findAll();
            if (!result.isEmpty()) {
                userDto.setListUsers(result);
                userDto.setStatusCode(200);
                userDto.setMessage("Successful");
            } else {
                userDto.setStatusCode(404);
                userDto.setMessage("No users found");
            }
        } catch (Exception e) {
            userDto.setStatusCode(500);
            userDto.setMessage("Error occurred: " + e.getMessage());
        }
        return userDto;
    }

    @Override
    public UserDto getUserById(Long id) {
        UserDto userDto = new UserDto();
        try {
            User usersById = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User Not found"));
            userDto.setUser(usersById);
            userDto.setStatusCode(200);
            userDto.setMessage("Users with id '" + id + "' found successfully");
        } catch (Exception e) {
            userDto.setStatusCode(500);
            userDto.setMessage("Error occurred: " + e.getMessage());
        }
        return userDto;
    }

    @Override
    public UserDto getUserByName(String name) {
        UserDto userDto = new UserDto();
        try {
            User usersByName = userRepository.findByName(name).orElseThrow(() -> new RuntimeException("User Not found"));
            userDto.setUser(usersByName);
            userDto.setStatusCode(200);
            userDto.setMessage("Users with name '" + name + "' found successfully");
        } catch (Exception e) {
            userDto.setStatusCode(500);
            userDto.setMessage("Error occurred: " + e.getMessage());
        }
        return userDto;
    }

    @Override
    public UserDto updateUser(Long id, UserDto updateRequest) {
        UserDto userDto = new UserDto();
        try {
            Optional<User> userOptional = userRepository.findById(id);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                user.setEmail(updateRequest.getEmail());
                user.setName(updateRequest.getName());
                user.setRole(updateRequest.getRole());
                user.setDateOfBirth(updateRequest.getDateOfBirth());

                if (updateRequest.getPassword() != null && !updateRequest.getPassword().isEmpty()) {

                    user.setPassword(passwordEncoder.encode(updateRequest.getPassword()));
                }

                User savedUser = userRepository.save(user);
                userDto.setUser(savedUser);
                userDto.setStatusCode(200);
                userDto.setMessage("User updated successfully");
            } else {
                userDto.setStatusCode(404);
                userDto.setMessage("User not found for update");
            }
        } catch (Exception e) {
            userDto.setStatusCode(500);
            userDto.setMessage("Error occurred while updating user: " + e.getMessage());
        }
        return userDto;
    }

    @Override
    public UserDto deleteUser(Long id) {
        UserDto userDto = new UserDto();
        try {
            Optional<User> userOptional = userRepository.findById(id);
            if (userOptional.isPresent()) {
                userRepository.deleteById(id);
                userDto.setStatusCode(200);
                userDto.setMessage("User deleted successfully");
            } else {
                userDto.setStatusCode(404);
                userDto.setMessage("User not found for deletion");
            }
        } catch (Exception e) {
            userDto.setStatusCode(500);
            userDto.setMessage("Error occurred while deleting user: " + e.getMessage());
        }
        return userDto;
    }

    @Override
    public UserDto getMyInfo(String email) {
        UserDto  userDto = new UserDto();
        try {
            Optional<User> userOptional = userRepository.findByEmail(email);
            if (userOptional.isPresent()) {
                userDto.setUser(userOptional.get());
                userDto.setStatusCode(200);
                userDto.setMessage("successful");
            } else {
                userDto.setStatusCode(404);
                userDto.setMessage("User not found for update");
            }

        }catch (Exception e){
            userDto.setStatusCode(500);
            userDto.setMessage("Error occurred while getting user info: " + e.getMessage());
        }
        return userDto;
    }

    @Override
    public UserDto verifyEmail(String tokenMail) {
        UserDto userDto = new UserDto();

        try {
            User user = userRepository.findByTokenMail(tokenMail);
            if (user != null) {
                user.setEnable(true);
                user.setTokenMail(null);
                user.setLocked(false);
                user.setFailedAttempts(0);
                userRepository.save(user);

                userDto.setMessage("Email verified successfully. Your account is now active.");
                userDto.setStatusCode(200);
            } else {
                userDto.setMessage("Invalid verification token");
                userDto.setStatusCode(400);
            }
        } catch (Exception e) {
            userDto.setStatusCode(500);
            userDto.setError(e.getMessage());
        }
        return userDto;
    }

    @Override
    public List<User> getUsersByGroupId(Long groupId) {
        return userRepository.findUsersByGroupId(groupId);
    }

    @Override
    public Long countUsersByGroupId(Long groupId) {
        Optional<UserGroup> updateOptional = groupRepository.findById(groupId);
        Long userCount = userRepository.countUsersByGroupId(groupId);

        UserGroup group = updateOptional.get();

        group.setMembersQuantity(Math.toIntExact(userCount));
        groupRepository.save(group);
        return userCount;
    }


}
