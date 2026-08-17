package br.com.fiap.mypass.password;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/pass")
@RequiredArgsConstructor
public class PasswordController {

    private final PasswordService service;

    @GetMapping
    public ResponseEntity<List<PasswordResponse>> getAllPasswords() {
        return ResponseEntity.ok(service.getAllPasswords());
    }
}
