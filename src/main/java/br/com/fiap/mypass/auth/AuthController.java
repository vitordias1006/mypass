package br.com.fiap.mypass.auth;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private final TokenService tokenService;

    public AuthController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    record LoginResponse(String username, String type, String token) {}

    @PostMapping("login")
    public LoginResponse login(Authentication authentication){
        var jwt = tokenService.generateToken(authentication.getName());
        return new LoginResponse(authentication.getName(), "Bearer",  jwt);
    }
}
