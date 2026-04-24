package com.bananapi.bananapi.dto.requestdto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRegistrationDTO {

    @NotBlank(message = "El usuario no puede estar vacío")
    @Size(min = 3, max = 20, message = "El usuario debe tener entre 3 y 20 caracteres")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Solo letras y números")
    private String username;

    @NotBlank
    @Email(message = "Formatgo de correo no valido")
    private String email;

    @NotBlank
    @Size(min = 8, max = 64, message = "La contraseña debe tener mínimo 8 caracteres")
    private String password;
}
