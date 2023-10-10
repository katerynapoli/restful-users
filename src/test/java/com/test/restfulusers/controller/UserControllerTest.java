package com.test.restfulusers.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.restfulusers.dto.request.UserRequestDto;
import com.test.restfulusers.dto.request.UserRequestDtoFieldUpdate;
import com.test.restfulusers.dto.response.UserResponseDto;
import com.test.restfulusers.service.impl.UserServiceImpl;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserController.class)
public class UserControllerTest {
    private static final Long VALID_ID = 1L;
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

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserServiceImpl userService;

    private UserRequestDto userRequestDto;
    private UserResponseDto userResponseDto;
    private List<UserResponseDto> userResponseDtoList;

    @BeforeEach
    public void setUp() {
        userRequestDto = new UserRequestDto();
        userRequestDto.setEmail(VALID_EMAIL);
        userRequestDto.setFirstName(FIRST_NAME);
        userRequestDto.setLastName(LAST_NAME);
        userRequestDto.setBirthDate(VALID_BIRTH_DATE);
        userRequestDto.setAddress(ADDRESS);
        userRequestDto.setPhoneNumber(VALID_PHONE_NUMBER);

        userResponseDto = new UserResponseDto();
        userResponseDto.setId(VALID_ID);
        userResponseDto.setEmail(VALID_EMAIL);
        userResponseDto.setFirstName(FIRST_NAME);
        userResponseDto.setLastName(LAST_NAME);
        userResponseDto.setBirthDate(VALID_BIRTH_DATE);
        userResponseDto.setAddress(ADDRESS);
        userResponseDto.setPhoneNumber(VALID_PHONE_NUMBER);

        userResponseDtoList = new ArrayList<>();
        userResponseDtoList.add(userResponseDto);
    }

    @Test
    void testCreateUser() throws Exception {
        when(userService.save(any())).thenReturn(userResponseDto);

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.email").value(VALID_EMAIL));
    }

    @Test
    void testGetUserById() throws Exception {
        when(userService.getById(any())).thenReturn(userResponseDto);

        mockMvc.perform(get("/users/{id}", VALID_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.email").value(VALID_EMAIL));
    }

    @Test
    void testGetAllUsers() throws Exception {
        when(userService.getAll()).thenReturn(userResponseDtoList);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].email").value(VALID_EMAIL));
    }

    @Test
    void testUpdateUserByIdPart() throws Exception {
        UserRequestDtoFieldUpdate userRequestDtoFieldUpdate =
                new UserRequestDtoFieldUpdate();
        userRequestDtoFieldUpdate.setEmail(UPDATED_EMAIL);

        userResponseDto.setEmail(UPDATED_EMAIL);

        when(userService.updateByIdPart(anyLong(), any(UserRequestDtoFieldUpdate.class)))
                .thenReturn(userResponseDto);

        mockMvc.perform(patch("/users/{id}", VALID_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequestDtoFieldUpdate)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(VALID_ID))
                .andExpect(jsonPath("$.email").value(UPDATED_EMAIL));
    }

    @Test
    void testUpdateUserByIdFull() throws Exception {
        UserRequestDto updatedUserRequestDto = new UserRequestDto();
        updatedUserRequestDto.setEmail(UPDATED_EMAIL);
        updatedUserRequestDto.setFirstName(UPDATED_FIRST_NAME);
        updatedUserRequestDto.setLastName(UPDATED_LAST_NAME);
        updatedUserRequestDto.setBirthDate(UPDATED_BIRTH_DATE);

        UserResponseDto updatedUserResponseDto = new UserResponseDto();
        updatedUserResponseDto.setId(VALID_ID);
        updatedUserResponseDto.setEmail(UPDATED_EMAIL);
        updatedUserResponseDto.setFirstName(UPDATED_FIRST_NAME);
        updatedUserResponseDto.setLastName(UPDATED_LAST_NAME);
        updatedUserResponseDto.setBirthDate(UPDATED_BIRTH_DATE);

        when(userService.updateByIdFull(anyLong(), any(UserRequestDto.class)))
                .thenReturn(updatedUserResponseDto);

        mockMvc.perform(put("/users/{id}", VALID_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedUserRequestDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(VALID_ID))
                .andExpect(jsonPath("$.email").value(UPDATED_EMAIL))
                .andExpect(jsonPath("$.firstName").value(UPDATED_FIRST_NAME))
                .andExpect(jsonPath("$.lastName").value(UPDATED_LAST_NAME))
                .andExpect(jsonPath("$.birthDate").value(UPDATED_BIRTH_DATE.toString()));
    }

    @Test
    void testDeleteUserById() throws Exception {
        mockMvc.perform(delete("/users/{id}", VALID_ID))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).deleteById(VALID_ID);
    }

    @Test
    void testSearchByBirthDate() throws Exception {
        when(userService.searchByBirthDate(any(), any())).thenReturn(userResponseDtoList);

        mockMvc.perform(get("/users/search")
                .param("from", VALID_BIRTH_DATE.toString())
                .param("to", UPDATED_BIRTH_DATE.toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].email").value(VALID_EMAIL));
    }
}
