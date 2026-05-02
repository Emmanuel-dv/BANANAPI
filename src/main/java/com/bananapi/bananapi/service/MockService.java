package com.bananapi.bananapi.service;

import com.bananapi.bananapi.domain.Mock;
import com.bananapi.bananapi.domain.User;
import com.bananapi.bananapi.dto.requestdto.RequestMockDTO;
import com.bananapi.bananapi.dto.responsedto.ResponseMockDTO;
import com.bananapi.bananapi.exceptions.InvalidCredentialsException;
import com.bananapi.bananapi.exceptions.InvalidMockException;
import com.bananapi.bananapi.exceptions.UserNotFoundException;
import com.bananapi.bananapi.repository.MockRepository;
import com.bananapi.bananapi.repository.UserRepository;
import com.bananapi.bananapi.utils.mappers.MockMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class MockService {

    private final MockRepository mockRepository;
    private final MockMapper mockMapper;
    private final UserRepository userRepository;

    public ResponseMockDTO createApiMock(RequestMockDTO mockDTO, String username) {

        User user = this.userRepository.findUserByUsername(username).orElseThrow(() -> new InvalidCredentialsException("Error de credenciales"));

        Mock newMock = this.mockMapper.toMock(mockDTO);

        newMock.setUser(user);

        newMock.setUrl(UUID.randomUUID().toString());

        return mockMapper.toResponseDTO(this.mockRepository.save(newMock));
    }

    public ResponseMockDTO updateApiMock(RequestMockDTO requestMockDTO, String url, String username) {

        Mock updatedMock = getWrapper(url, username);

        updatedMock.setName(requestMockDTO.getName());
        updatedMock.setJsonData(requestMockDTO.getJsonData());

        return mockMapper.toResponseDTO(this.mockRepository.save(updatedMock));
    }

    public void deleteMock(String url, String username) {
        this.mockRepository.delete(getWrapper(url, username));
    }

    public Map<String, Object> getJson(String url) {
        Mock mock = this.mockRepository.findByUrl(url).orElseThrow(() -> new InvalidMockException("Error de mock"));

        mock.setLastUsageDate(LocalDateTime.now());

        return mock.getJsonData();
    }

    public ResponseMockDTO getSingleMock(String url, String username) {
        return this.mockMapper.toResponseDTO(getWrapper(url, username));
    }

    public List<ResponseMockDTO> getAllUserMocks(String username) {
        return this.mockRepository.getMocksByUserUsername(username).stream().map(this.mockMapper::toResponseDTO).toList();
    }

    record MockUserRecord(Mock mock, User user) {

    }

    private Mock getWrapper(String url, String username) {
        Mock mock = this.mockRepository.findByUrl(url).orElseThrow(() -> new InvalidMockException("Error de mock"));

        if (mock.getUser() == null) throw new InvalidMockException("Error de mock");

        if (!mock.getUser().getUsername().equals(username))
            throw new InvalidMockException("Error de mock");

        return mock;
    }


}
