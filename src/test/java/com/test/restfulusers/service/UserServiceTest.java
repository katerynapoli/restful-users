package com.test.restfulusers.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.test.restfulusers.dto.request.UserRequestDto;
import com.test.restfulusers.dto.request.UserRequestDtoFieldUpdate;
import com.test.restfulusers.dto.response.UserResponseDto;
import com.test.restfulusers.exception.EntityAlreadyExistException;
import com.test.restfulusers.exception.EntityNotFoundException;
import com.test.restfulusers.mapper.UserMapper;
import com.test.restfulusers.model.User;
import com.test.restfulusers.repository.UserRepository;
import com.test.restfulusers.service.impl.UserServiceImpl;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    private static final int MIN_AGE = 18;
    private static final Long VALID_ID = 1L;
    private static final Long INVALID_ID = 0L;
    private static final String VALID_EMAIL = "user@gmail.com";
    private static final String UPDATED_EMAIL = "updateduser@gmail.com";
    private static final String FIRST_NAME = "John";
    private static final String UPDATED_FIRST_NAME = "Bob";
    private static final String LAST_NAME = "Smith";
    private static final String UPDATED_LAST_NAME = "Parker";
    private static final LocalDate VALID_BIRTH_DATE = LocalDate.of(2000, 10, 10);
    private static final LocalDate UPDATED_BIRTH_DATE = LocalDate.of(2001, 10, 10);
    private static final String ADDRESS = "Brighton 5th St, Brooklyn";
    private static final String VALID_PHONE_NUMBER = "+1928374650";
    private static final boolean IS_DELETED_FALSE = false;
    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private UserRequestDto userRequestDto;
    private User user;
    private UserResponseDto expectedUserResponse;
    private List<User> users;
    private List<UserResponseDto> expectedUserResponseList;

    @BeforeEach
    void setUp() {
        userRequestDto = new UserRequestDto();
        userRequestDto.setEmail(VALID_EMAIL);
        userRequestDto.setFirstName(FIRST_NAME);
        userRequestDto.setLastName(LAST_NAME);
        userRequestDto.setBirthDate(VALID_BIRTH_DATE);
        userRequestDto.setAddress(ADDRESS);
        userRequestDto.setPhoneNumber(VALID_PHONE_NUMBER);

        user = new User();
        user.setId(VALID_ID);
        user.setEmail(VALID_EMAIL);
        user.setFirstName(FIRST_NAME);
        user.setLastName(LAST_NAME);
        user.setBirthDate(VALID_BIRTH_DATE);
        user.setAddress(ADDRESS);
        user.setPhoneNumber(VALID_PHONE_NUMBER);
        user.setDeleted(IS_DELETED_FALSE);

        expectedUserResponse = new UserResponseDto();
        expectedUserResponse.setId(VALID_ID);
        expectedUserResponse.setEmail(VALID_EMAIL);
        expectedUserResponse.setFirstName(FIRST_NAME);
        expectedUserResponse.setLastName(LAST_NAME);
        expectedUserResponse.setBirthDate(VALID_BIRTH_DATE);
        expectedUserResponse.setAddress(ADDRESS);
        expectedUserResponse.setPhoneNumber(VALID_PHONE_NUMBER);

        users = new ArrayList<>();
        users.add(new User());
        users.add(new User());

        expectedUserResponseList = new ArrayList<>();
        expectedUserResponseList.add(new UserResponseDto());
        expectedUserResponseList.add(new UserResponseDto());
    }

    @Test
    void save_ValidUserRequestDto_Ok() {
        when(userMapper.toModel(userRequestDto)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(expectedUserResponse);

        UserResponseDto actualResponse = userService.save(userRequestDto);

        assertNotNull(actualResponse);
        assertEquals(expectedUserResponse, actualResponse);
    }

    @Test
    void getById_ValidId_Ok() {
        when(userRepository.findById(VALID_ID)).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(expectedUserResponse);

        UserResponseDto actualResponse = userService.getById(VALID_ID);

        assertNotNull(actualResponse);
        assertEquals(expectedUserResponse, actualResponse);
    }

    @Test
    void getAll_Ok() {
        when(userRepository.findAll()).thenReturn(users);
        when(userMapper.toDto(any(User.class))).thenReturn(new UserResponseDto());

        List<UserResponseDto> actualResponseList = userService.getAll();

        assertNotNull(actualResponseList);
        assertEquals(expectedUserResponseList.size(), actualResponseList.size());
        assertTrue(actualResponseList.containsAll(expectedUserResponseList));
    }

    @Test
    void updateByIdPart_Ok() {
        UserRequestDtoFieldUpdate userRequestDtoFieldUpdate =
                new UserRequestDtoFieldUpdate();
        userRequestDtoFieldUpdate.setEmail(UPDATED_EMAIL);

        when(userRepository.findById(VALID_ID)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        UserResponseDto expectedResponseDto = new UserResponseDto();
        expectedResponseDto.setId(user.getId());
        expectedResponseDto.setEmail(userRequestDtoFieldUpdate.getEmail());
        expectedResponseDto.setFirstName(user.getFirstName());
        expectedResponseDto.setLastName(user.getLastName());
        expectedResponseDto.setBirthDate(user.getBirthDate());
        expectedResponseDto.setAddress(user.getAddress());
        expectedResponseDto.setPhoneNumber(user.getPhoneNumber());

        when(userMapper.toDto(user)).thenReturn(expectedResponseDto);

        UserResponseDto actualResponse = userService.updateByIdPart(
                VALID_ID, userRequestDtoFieldUpdate);

        assertNotNull(actualResponse);
        assertEquals(user.getId(), actualResponse.getId());
        assertEquals(userRequestDtoFieldUpdate.getEmail(), actualResponse.getEmail());
    }

    @Test
    void updateByIdFull_Ok() {
        UserRequestDto updatedUserRequestDto = new UserRequestDto();
        updatedUserRequestDto.setEmail(UPDATED_EMAIL);
        updatedUserRequestDto.setFirstName(UPDATED_FIRST_NAME);
        updatedUserRequestDto.setLastName(UPDATED_LAST_NAME);
        updatedUserRequestDto.setBirthDate(UPDATED_BIRTH_DATE);

        when(userRepository.findById(VALID_ID)).thenReturn(Optional.of(user));

        User updatedUser = new User();
        updatedUser.setId(user.getId());
        updatedUser.setEmail(updatedUserRequestDto.getEmail());
        updatedUser.setFirstName(updatedUserRequestDto.getFirstName());
        updatedUser.setLastName(updatedUserRequestDto.getLastName());
        updatedUser.setBirthDate(updatedUserRequestDto.getBirthDate());

        when(userMapper.toModel(updatedUserRequestDto)).thenReturn(updatedUser);
        when(userRepository.save(updatedUser)).thenReturn(updatedUser);

        UserResponseDto expectedResponseDto = new UserResponseDto();
        expectedResponseDto.setId(user.getId());
        expectedResponseDto.setEmail(updatedUserRequestDto.getEmail());
        expectedResponseDto.setFirstName(updatedUserRequestDto.getFirstName());
        expectedResponseDto.setLastName(updatedUserRequestDto.getLastName());
        expectedResponseDto.setBirthDate(updatedUserRequestDto.getBirthDate());

        when(userMapper.toDto(updatedUser)).thenReturn(expectedResponseDto);

        UserResponseDto actualResponse = userService.updateByIdFull(
                VALID_ID, updatedUserRequestDto);

        assertNotNull(actualResponse);
        assertEquals(updatedUserRequestDto.getEmail(), actualResponse.getEmail());
        assertEquals(updatedUserRequestDto.getFirstName(), actualResponse.getFirstName());
        assertEquals(updatedUserRequestDto.getLastName(), actualResponse.getLastName());
        assertEquals(updatedUserRequestDto.getBirthDate(), actualResponse.getBirthDate());
        assertNull(actualResponse.getAddress());
        assertNull(actualResponse.getPhoneNumber());
    }

    @Test
    void deleteById_Ok() {
        userService.deleteById(VALID_ID);
        verify(userRepository, times(1)).deleteById(VALID_ID);
    }

    @Test
    void searchByBirthDate_Ok() {
        List<User> usersInRange = Arrays.asList(user);

        when(userRepository.findAllByBirthDateBetween(
                VALID_BIRTH_DATE, UPDATED_BIRTH_DATE)).thenReturn(usersInRange);

        List<UserResponseDto> expectedUserResponses = usersInRange.stream()
                .map(userMapper::toDto)
                .toList();

        List<UserResponseDto> actualUserResponses = userService.searchByBirthDate(
                VALID_BIRTH_DATE, UPDATED_BIRTH_DATE);

        assertEquals(expectedUserResponses.size(), actualUserResponses.size());
        assertEquals(expectedUserResponses, actualUserResponses);
    }

    @Test
    void searchByBirthDate_FromIsAfterTo_NotOk() {
        assertThrows(IllegalArgumentException.class,
                () -> userService.searchByBirthDate(UPDATED_BIRTH_DATE, VALID_BIRTH_DATE));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 10, 17})
    void checkAge_AgeUnderMinimum_NotOk(int age) {
        ReflectionTestUtils.setField(userService, "minAge", MIN_AGE);
        LocalDate birthDate = LocalDate.now().minusYears(age);
        userRequestDto.setBirthDate(birthDate);

        assertThrows(IllegalArgumentException.class,
                () -> userService.save(userRequestDto));
    }

    @Test
    void validateEmailUnique_DuplicateEmail_NotOk() {
        when(userRepository.findByEmail(userRequestDto.getEmail()))
                .thenReturn(Optional.of(user));

        assertThrows(EntityAlreadyExistException.class,
                () -> userService.save(userRequestDto));
    }

    @Test
    void validatePhoneNumberUnique_DuplicatePhoneNumber_NotOk() {
        when(userRepository.findByPhoneNumber(userRequestDto.getPhoneNumber()))
                .thenReturn(Optional.of(user));

        assertThrows(EntityAlreadyExistException.class,
                () -> userService.save(userRequestDto));
    }

    @Test
    void findUser_InvalidId_NotOk() {
        when(userRepository.findById(INVALID_ID)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> userService.findUser(INVALID_ID));
    }
}
