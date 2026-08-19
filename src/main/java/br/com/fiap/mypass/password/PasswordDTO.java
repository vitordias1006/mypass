package br.com.fiap.mypass.password;

import jakarta.validation.constraints.NotBlank;

public class PasswordDTO {
    public record PasswordResponse(Long id, String url, String password, String username) {
    }

    public record PasswordRequest(@NotBlank String url, @NotBlank String username, @NotBlank String password) {}
}
