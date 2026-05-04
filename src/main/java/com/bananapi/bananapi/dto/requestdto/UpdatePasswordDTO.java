package com.bananapi.bananapi.dto.requestdto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdatePasswordDTO {

    @NotBlank
    private String usernamne;

    @NotBlank
    private String oldPassword;

    @NotBlank
    private String newPassword;
}
