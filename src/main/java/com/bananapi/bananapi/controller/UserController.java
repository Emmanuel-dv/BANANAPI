package com.bananapi.bananapi.controller;

import com.bananapi.bananapi.dto.requestdto.UpdatePasswordDTO;
import com.bananapi.bananapi.dto.requestdto.UserLoginDTO;
import com.bananapi.bananapi.dto.requestdto.UserRegistrationDTO;
import com.bananapi.bananapi.dto.responsedto.UserProfileDTO;
import com.bananapi.bananapi.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public UserProfileDTO registerUser(@Valid @RequestBody UserRegistrationDTO userRegistrationDTO) {
        return this.userService.registerUser(userRegistrationDTO);
    }

    @PostMapping("/login")
    public UserProfileDTO loginUser(@Valid @RequestBody UserLoginDTO userLoginDTO) {
        return this.userService.loginUser(userLoginDTO);
    }

    @DeleteMapping("/{username}")
    public void deleteUser(@PathVariable String username) {
        this.userService.deleteUser(username);
    }

    @PutMapping("/edit")
    public void editUserPassword(@Valid @RequestBody UpdatePasswordDTO updatePasswordDTO) {
        this.userService.updateUserPassword(updatePasswordDTO.getUsernamne(), updatePasswordDTO.getOldPassword(), updatePasswordDTO.getNewPassword());
    }




}
