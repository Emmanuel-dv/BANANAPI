package com.bananapi.bananapi.utils.mappers;

import com.bananapi.bananapi.domain.Mock;
import com.bananapi.bananapi.dto.requestdto.RequestMockDTO;
import com.bananapi.bananapi.dto.responsedto.ResponseMockDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MockMapper {

    Mock toMock(RequestMockDTO mockDTO);

    ResponseMockDTO toResponseDTO(Mock mock);
}
