package com.test.restfulusers.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;
import lombok.Data;

@Data
public class UserRequestDto {
    @Email(message = "Email must be of valid format")
    @NotBlank(message = "Email must not be empty")
    private String email;

    @NotBlank(message = "First name must not be empty")
    private String firstName;

    @NotBlank(message = "Last name must not be empty")
    private String lastName;

    @Past(message = "Birth date must be in the past")
    @NotNull(message = "Birth date must not be empty")
    private LocalDate birthDate;

    private String address;

    @Pattern(regexp = "^\\+\\d{10,15}$",
            message = "Phone number must begin with '+' and contain at least 10 digits")
    private String phoneNumber;
}
