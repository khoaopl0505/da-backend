package net.javaguids.da09.controller;

import net.javaguids.da09.dto.UserDto;
import net.javaguids.da09.model.User;
import net.javaguids.da09.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/auth/register")
    public ResponseEntity<UserDto> register(@RequestBody UserDto requestRegister){
        return ResponseEntity.ok(userService.register(requestRegister));
    }

    @PostMapping("/auth/login")
    public  ResponseEntity<UserDto> login(@RequestBody UserDto requestLogin){
        return ResponseEntity.ok(userService.login(requestLogin));
    }

    @PostMapping("/auth/refresh_token")
    public ResponseEntity<UserDto> refreshToken(@RequestBody UserDto responseToken){
        return  ResponseEntity.ok(userService.refreshToken(responseToken));
    }

    @GetMapping("/admin/list_users")
    public  ResponseEntity<UserDto> getAllUsers(){
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/group/find_by_id/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id){
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/admin/find_by_name/{name}")
    public ResponseEntity<UserDto> getUserByName(@PathVariable String name){
        return ResponseEntity.ok(userService.getUserByName(name));
    }

    @PutMapping("/admin/update-user/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody UserDto updateRequest){
        return ResponseEntity.ok(userService.updateUser(id, updateRequest));
    }

    @GetMapping("/group/get-profile")
    public ResponseEntity<UserDto> getProfile(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email =authentication.getName();
        return ResponseEntity.ok(userService.getMyInfo(email));
    }

    @DeleteMapping("/admin/delete/{id}")
    public ResponseEntity<UserDto> deleteUserById(@PathVariable Long id){
        return ResponseEntity.ok(userService.deleteUser(id));
    }

    @PutMapping("/group/change-password/{id}")
    public ResponseEntity<UserDto> changePassword(@PathVariable Long id, @RequestBody UserDto changePass){
        return ResponseEntity.ok(userService.changePassword(id, changePass.getPassword(), changePass.getNewPassword()));
    }

    @PutMapping("/group/reset-password/{email}")
    public ResponseEntity<UserDto> ResetPassword(@PathVariable String email, @RequestBody UserDto resetPassword){
        return ResponseEntity.ok(userService.forgotPassword(email, resetPassword.getNewPassword()));
    }

    @GetMapping("/confirm")
    public ResponseEntity<String> verifyUser(@RequestParam String tokenMail){
        UserDto userDto = userService.verifyEmail(tokenMail);
        if(userDto.getStatusCode()==200){
            return ResponseEntity.ok(userDto.getMessage());
        }else {
            return ResponseEntity.badRequest().body(userDto.getMessage());
        }
    }

    @PostMapping("/auth/unlock")
    public ResponseEntity<UserDto> unlockAccount(@RequestBody UserDto unlockRequest){
        return ResponseEntity.ok(userService.unlockAccount(unlockRequest));
    }
    @GetMapping("/group/user-by-id-group/{groupId}")
    public ResponseEntity<List<User>> getUsersByGroupId(@PathVariable Long groupId) {
        List<User> users = userService.getUsersByGroupId(groupId);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/count-by-group/{groupId}")
    public ResponseEntity<Long> countUsersByGroup(@PathVariable Long groupId) {
        Long userCount = userService.countUsersByGroupId(groupId);
        return ResponseEntity.ok(userCount);
    }
}
