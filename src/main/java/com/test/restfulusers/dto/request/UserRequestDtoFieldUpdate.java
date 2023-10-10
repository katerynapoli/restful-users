package com.test.restfulusers.dto.request;

import com.test.restfulusers.lib.NotEmptyIfPresent;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;
import lombok.Data;

@Data
public class UserRequestDtoFieldUpdate {
    @Email(message = "Email must be of valid format")
    @NotEmptyIfPresent(message = "Email must not be empty")
    private String email;

    @NotEmptyIfPresent(message = "First name must not be empty")
    private String firstName;

    @NotEmptyIfPresent(message = "Last name must not be empty")
    private String lastName;

    @Past(message = "Birth date must be in the past")
    @NotEmptyIfPresent(message = "Birth date must not be empty")
    private LocalDate birthDate;

    @NotEmptyIfPresent(message = "Address must not be empty")
    private String address;

    @Pattern(regexp = "^\\+\\d{10,15}$",
            message = "Phone number must begin with '+' and contain at least 10 digits")
    @NotEmptyIfPresent(message = "Phone number must not be empty")
    private String phoneNumber;
}
