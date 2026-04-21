package com.bananapi.bananapi.utils.mappers;

import com.bananapi.bananapi.domain.User;
import com.bananapi.bananapi.dto.requestdto.UserRegistrationDTO;
import com.bananapi.bananapi.dto.responsedto.UserProfileDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User UserRegistrationDTOToUser(UserRegistrationDTO userRegistrationDTO);

    UserProfileDTO toUserProfileDTO(User user);

}
