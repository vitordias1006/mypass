package br.com.fiap.mypass.password;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pass")
@RequiredArgsConstructor
public class PasswordController {

    private final PasswordService service;

    @GetMapping
    @PreAuthorize("hasRole( 'ADMIN')")
    public ResponseEntity<List<PasswordDTO.PasswordResponse>> getAllPasswords() {
        return ResponseEntity.ok(service.getAllPasswords());
    }

    @GetMapping("/owner")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<PasswordDTO.PasswordResponse>> getAllPasswordsByOwner(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(service.getAllPasswordsByOwner(jwt.getSubject()));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<PasswordDTO.PasswordResponse> createPassword(
            @RequestBody @Valid PasswordDTO.PasswordRequest request,
            @AuthenticationPrincipal Jwt jwt
    ) {
        return ResponseEntity.ok(service.createPassword(request, jwt.getSubject()));
    }
}
