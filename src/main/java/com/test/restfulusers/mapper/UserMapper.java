package com.test.restfulusers.mapper;

import com.test.restfulusers.config.MapperConfig;
import com.test.restfulusers.dto.request.UserRequestDto;
import com.test.restfulusers.dto.request.UserRequestDtoFieldUpdate;
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
    default void updateFromDto(UserRequestDtoFieldUpdate userRequestDtoFieldUpdate,
                               @MappingTarget User user) {
        if (userRequestDtoFieldUpdate == null) {
            return;
        }

        if (userRequestDtoFieldUpdate.getEmail() != null) {
            user.setEmail(userRequestDtoFieldUpdate.getEmail());
        }
        if (userRequestDtoFieldUpdate.getFirstName() != null) {
            user.setFirstName(userRequestDtoFieldUpdate.getFirstName());
        }
        if (userRequestDtoFieldUpdate.getLastName() != null) {
            user.setLastName(userRequestDtoFieldUpdate.getLastName());
        }
        if (userRequestDtoFieldUpdate.getBirthDate() != null) {
            user.setBirthDate(userRequestDtoFieldUpdate.getBirthDate());
        }
        if (userRequestDtoFieldUpdate.getAddress() != null) {
            user.setAddress(userRequestDtoFieldUpdate.getAddress());
        }
        if (userRequestDtoFieldUpdate.getPhoneNumber() != null) {
            user.setPhoneNumber(userRequestDtoFieldUpdate.getPhoneNumber());
        }
    }
}
