package com.bananapi.bananapi.service;

import com.bananapi.bananapi.domain.User;
import com.bananapi.bananapi.dto.requestdto.UserLoginDTO;
import com.bananapi.bananapi.dto.requestdto.UserRegistrationDTO;
import com.bananapi.bananapi.dto.responsedto.UserProfileDTO;
import com.bananapi.bananapi.exceptions.InvalidCredentialsException;
import com.bananapi.bananapi.exceptions.SamePasswordException;
import com.bananapi.bananapi.exceptions.UserAlreadyExistsException;
import com.bananapi.bananapi.repository.UserRepository;
import com.bananapi.bananapi.utils.mappers.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {


    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserProfileDTO registerUser(UserRegistrationDTO userRegistrationDTO) {

        User incomingUser = this.userMapper.UserRegistrationDTOToUser(userRegistrationDTO);

        if (this.userRepository.existsUserByEmail(incomingUser.getEmail()) || this.userRepository.existsUserByUsername(incomingUser.getUsername())) {
            throw new UserAlreadyExistsException("Ya existe un usuario");
        }

        incomingUser.setPassword(passwordEncoder.encode(incomingUser.getPassword()));

        this.userRepository.save(incomingUser);
        return userMapper.toUserProfileDTO(incomingUser);
    }

    public void deleteUser(String username) {
        this.userRepository.deleteByUsername(username);
    }

    public UserProfileDTO loginUser(UserLoginDTO userLoginDTO) {
        User userLogin = this.userRepository.findUserByUsername(userLoginDTO.getUsername());

        if (userLogin == null) throw new InvalidCredentialsException("Error de credenciales");

        if (!passwordEncoder.matches(userLoginDTO.getPassword(), userLogin.getPassword()))
            throw new InvalidCredentialsException("Error de credenciales");

        return userMapper.toUserProfileDTO(userLogin);
    }

    public void updateUserPassword(String username, String oldPassword, String newPassword) {

        User user = this.userRepository.findUserByUsername(username);

        if (user == null) throw new InvalidCredentialsException("Error de credenciales");

        if (!passwordEncoder.matches(oldPassword, user.getPassword()))
            throw new InvalidCredentialsException("Error de credenciales");

        if (oldPassword.equals(newPassword)) throw new SamePasswordException("No se puede poner la misma contraseña");

        user.setPassword(passwordEncoder.encode(newPassword));
        this.userRepository.save(user);
    }

}
