package com.bananapi.bananapi.service;

import com.bananapi.bananapi.config.SecurityConfig;
import com.bananapi.bananapi.domain.User;
import com.bananapi.bananapi.dto.requestdto.UserRegistrationDTO;
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

    public void registerUser(UserRegistrationDTO userRegistrationDTO) {

        User incomingUser = this.userMapper.UserRegistrationDTOToUser(userRegistrationDTO);

        if (this.userRepository.existsUserByEmail(incomingUser.getEmail()) || this.userRepository.existsUserByUsername(incomingUser.getUsername())) {
            //Aqui deberia tirar una excepcion personalizada como userAlreadyExists e informar en vez de un early return

            //Tirar un UserAlreadyExistsException que debo configurar aun)))))
            return;
        }

        incomingUser.setPassword(passwordEncoder.encode(incomingUser.getPassword()));

        this.userRepository.save(incomingUser);
        //Deberia informar al usuario de que se ha guardado correctamente de alguna manera? Entonces mi metodo no deberia devolver un void sino otra cosa?
    }

}
