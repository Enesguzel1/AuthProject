package com.enesguzel.authify.io;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileRequest {
    @NotBlank(message = "Name should be not empty")
    private String name;
    @Email(message = "Enter valid email")
    @NotNull(message = "Email should be not empty")
    private String email;
    @Size(min = 6, message = "Password must be at least 6 digit")
    private String password;
}
