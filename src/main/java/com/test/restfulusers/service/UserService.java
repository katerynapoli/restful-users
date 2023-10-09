package com.test.restfulusers.service;

import com.test.restfulusers.dto.request.UserRequestDto;
import com.test.restfulusers.dto.request.UserRequestDtoForFieldUpdate;
import com.test.restfulusers.dto.response.UserResponseDto;
import com.test.restfulusers.model.User;
import java.time.LocalDate;
import java.util.List;

public interface UserService {
    UserResponseDto save(UserRequestDto userRequestDto);

    UserResponseDto getById(Long id);

    List<UserResponseDto> getAll();

    UserResponseDto updateByIdPart(Long id,
                                   UserRequestDtoForFieldUpdate userRequestDtoForFieldUpdate);

    UserResponseDto updateByIdFull(Long id, UserRequestDto userRequestDto);

    void deleteById(Long id);

    List<UserResponseDto> searchByBirthDate(LocalDate from, LocalDate to);

    User findUser(Long id);

    void checkAge(UserRequestDto userRequestDto);

    void checkAge(UserRequestDtoForFieldUpdate userRequestDtoForFieldUpdate);

    void validateEmailUnique(String email);

    void validatePhoneNumberUnique(String phoneNumber);

}
