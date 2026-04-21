package com.bananapi.bananapi.service;

import com.bananapi.bananapi.domain.Mock;
import com.bananapi.bananapi.domain.User;
import com.bananapi.bananapi.dto.requestdto.RequestMockDTO;
import com.bananapi.bananapi.dto.responsedto.ResponseMockDTO;
import com.bananapi.bananapi.exceptions.UserNotFoundException;
import com.bananapi.bananapi.repository.MockRepository;
import com.bananapi.bananapi.repository.UserRepository;
import com.bananapi.bananapi.utils.mappers.MockMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class MockService {

    private final MockRepository mockRepository;
    private final MockMapper mockMapper;
    private final UserRepository userRepository;

    public ResponseMockDTO createApiMock(RequestMockDTO mockDTO, String username) {

        User user = this.userRepository.findUserByUsername(username);

        if (user == null) throw new UserNotFoundException("Usuario no encontrado");

        Mock newMock = this.mockMapper.toMock(mockDTO);

        newMock.setUser(user);

        newMock.setUrl(UUID.randomUUID().toString());

        return mockMapper.toResponseDTO(this.mockRepository.save(newMock));
    }

}
