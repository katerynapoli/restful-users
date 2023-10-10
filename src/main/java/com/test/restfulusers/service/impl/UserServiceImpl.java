package com.test.restfulusers.service.impl;

import com.test.restfulusers.dto.request.UserRequestDto;
import com.test.restfulusers.dto.request.UserRequestDtoFieldUpdate;
import com.test.restfulusers.dto.response.UserResponseDto;
import com.test.restfulusers.exception.EntityAlreadyExistException;
import com.test.restfulusers.exception.EntityNotFoundException;
import com.test.restfulusers.mapper.UserMapper;
import com.test.restfulusers.model.User;
import com.test.restfulusers.repository.UserRepository;
import com.test.restfulusers.service.UserService;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Value("${user.registration.min-age}")
    private int minAge;

    @Transactional
    @Override
    public UserResponseDto save(UserRequestDto userRequestDto) {
        validateEmailUnique(userRequestDto.getEmail());
        validatePhoneNumberUnique(userRequestDto.getPhoneNumber());
        checkAge(userRequestDto.getBirthDate());

        User user = userMapper.toModel(userRequestDto);
        userRepository.save(user);
        return userMapper.toDto(user);
    }

    @Override
    public UserResponseDto getById(Long id) {
        return userMapper.toDto(findUser(id));
    }

    @Override
    public List<UserResponseDto> getAll() {
        return userRepository.findAll().stream()
                .map(userMapper::toDto)
                .toList();
    }

    @Transactional
    @Override
    public UserResponseDto updateByIdPart(
            Long id, UserRequestDtoFieldUpdate userRequestDtoFieldUpdate) {
        if (userRequestDtoFieldUpdate.getEmail() != null) {
            validateEmailUnique(userRequestDtoFieldUpdate.getEmail());
        }
        if (userRequestDtoFieldUpdate.getPhoneNumber() != null) {
            validatePhoneNumberUnique(userRequestDtoFieldUpdate.getPhoneNumber());
        }
        if (userRequestDtoFieldUpdate.getBirthDate() != null) {
            checkAge(userRequestDtoFieldUpdate.getBirthDate());
        }

        User existingUser = findUser(id);
        userMapper.updateFromDto(userRequestDtoFieldUpdate, existingUser);
        return userMapper.toDto(userRepository.save(existingUser));
    }

    @Transactional
    @Override
    public UserResponseDto updateByIdFull(Long id, UserRequestDto userRequestDto) {
        validateEmailUnique(userRequestDto.getEmail());
        validatePhoneNumberUnique(userRequestDto.getPhoneNumber());
        checkAge(userRequestDto.getBirthDate());

        User existingUser = findUser(id);
        User newUser = userMapper.toModel(userRequestDto);
        newUser.setId(existingUser.getId());
        return userMapper.toDto(userRepository.save(newUser));
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public List<UserResponseDto> searchByBirthDate(LocalDate from, LocalDate to) {
        if (from.isAfter(to)) {
            throw new IllegalArgumentException("The date FROM: "
                    + from
                    + " must be before the date TO: "
                    + to);
        }
        return userRepository.findAllByBirthDateBetween(from, to).stream()
                .map(userMapper::toDto)
                .toList();
    }

    @Override
    public User findUser(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("There is no user with id " + id));
    }

    private void checkAge(LocalDate birthDate) {
        LocalDate today = LocalDate.now();
        int age = Period.between(birthDate, today).getYears();

        if (age < minAge) {
            throw new IllegalArgumentException("You must be at least " + minAge + " years old");
        }
    }

    private void validateEmailUnique(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new EntityAlreadyExistException("User with email " + email + " already exists");
        }
    }

    private void validatePhoneNumberUnique(String phoneNumber) {
        if (phoneNumber != null && userRepository.findByPhoneNumber(phoneNumber).isPresent()) {
            throw new EntityAlreadyExistException(
                    "User with phone number " + phoneNumber + " already exists");
        }
    }
}
