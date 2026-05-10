package com.bananapi.bananapi.service;

import com.bananapi.bananapi.domain.User;
import com.bananapi.bananapi.dto.requestdto.UserLoginDTO;
import com.bananapi.bananapi.dto.requestdto.UserRegistrationDTO;
import com.bananapi.bananapi.dto.responsedto.AuthResponseDTO;
import com.bananapi.bananapi.dto.responsedto.UserProfileDTO;
import com.bananapi.bananapi.exceptions.InvalidCredentialsException;
import com.bananapi.bananapi.exceptions.SamePasswordException;
import com.bananapi.bananapi.exceptions.UserAlreadyExistsException;
import com.bananapi.bananapi.repository.UserRepository;
import com.bananapi.bananapi.security.JwtService;
import com.bananapi.bananapi.utils.mappers.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

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

    public AuthResponseDTO loginUser(UserLoginDTO userLoginDTO) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userLoginDTO.getUsername(),
                        userLoginDTO.getPassword()
                )
        );

        String token = this.jwtService.generateToken(userLoginDTO.getUsername());

        return AuthResponseDTO.builder().token(token).build();
    }

    public void updateUserPassword(String username, String oldPassword, String newPassword) {
        User user = this.userRepository.findUserByUsername(username).orElseThrow(() -> new InvalidCredentialsException("Error de credenciales"));

        if (!passwordEncoder.matches(oldPassword, user.getPassword()))
            throw new InvalidCredentialsException("Error de credenciales");

        if (oldPassword.equals(newPassword)) throw new SamePasswordException("No se puede poner la misma contraseña");

        user.setPassword(passwordEncoder.encode(newPassword));
        this.userRepository.save(user);
    }

}
