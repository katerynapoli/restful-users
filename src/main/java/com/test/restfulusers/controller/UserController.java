package com.test.restfulusers.controller;

import com.test.restfulusers.dto.request.UserRequestDto;
import com.test.restfulusers.dto.request.UserRequestDtoForFieldUpdate;
import com.test.restfulusers.dto.response.UserResponseDto;
import com.test.restfulusers.service.UserService;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto createUser(@RequestBody @Valid UserRequestDto userRequestDto) {
        return userService.save(userRequestDto);
    }

    @GetMapping("/{id}")
    public UserResponseDto getUserById(@PathVariable Long id) {
        return userService.getById(id);
    }

    @GetMapping
    public List<UserResponseDto> getAllUsers() {
        return userService.getAll();
    }

    @PatchMapping("/{id}")
    public UserResponseDto updateUserByIdPart(@PathVariable Long id,
                                              @RequestBody @Valid
                                              UserRequestDtoForFieldUpdate
                                                      userRequestDtoForFieldUpdate) {
        return userService.updateByIdPart(id, userRequestDtoForFieldUpdate);
    }

    @PutMapping("/{id}")
    public UserResponseDto updateUserByIdFull(@PathVariable Long id,
                                              @RequestBody @Valid UserRequestDto userRequestDto) {
        return userService.updateByIdFull(id, userRequestDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserById(@PathVariable Long id) {
        userService.deleteById(id);
    }

    @GetMapping("/search")
    public List<UserResponseDto> searchByBirthDate(LocalDate from, LocalDate to) {
        return userService.searchByBirthDate(from, to);
    }
}
