package com.test.restfulusers.mapper;

import com.test.restfulusers.config.MapperConfig;
import com.test.restfulusers.dto.request.UserRequestDto;
import com.test.restfulusers.dto.request.UserRequestDtoForFieldUpdate;
import com.test.restfulusers.dto.response.UserResponseDto;
import com.test.restfulusers.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    UserResponseDto toDto(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    User toModel(UserRequestDto userRequestDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    default void updateFromDto(UserRequestDtoForFieldUpdate userRequestDtoForFieldUpdate,
                       @MappingTarget User user) {
        if (userRequestDtoForFieldUpdate == null) {
            return;
        }

        if (userRequestDtoForFieldUpdate.getEmail() != null) {
            user.setEmail(userRequestDtoForFieldUpdate.getEmail());
        }
        if (userRequestDtoForFieldUpdate.getFirstName() != null) {
            user.setFirstName(userRequestDtoForFieldUpdate.getFirstName());
        }
        if (userRequestDtoForFieldUpdate.getLastName() != null) {
            user.setLastName(userRequestDtoForFieldUpdate.getLastName());
        }
        if (userRequestDtoForFieldUpdate.getBirthDate() != null) {
            user.setBirthDate(userRequestDtoForFieldUpdate.getBirthDate());
        }
        if (userRequestDtoForFieldUpdate.getAddress() != null) {
            user.setAddress(userRequestDtoForFieldUpdate.getAddress());
        }
        if (userRequestDtoForFieldUpdate.getPhoneNumber() != null) {
            user.setPhoneNumber(userRequestDtoForFieldUpdate.getPhoneNumber());
        }
    }
}
