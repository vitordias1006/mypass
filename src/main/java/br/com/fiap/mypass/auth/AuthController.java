package br.com.fiap.mypass.auth;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;

    public AuthController(TokenService tokenService, AuthenticationManager authenticationManager) {
        this.tokenService = tokenService;
        this.authenticationManager = authenticationManager;
    }

    record LoginRequest(@NotBlank String username,  @NotBlank String password) {}
    record LoginResponse(String username, String type, String token) {}

    @PostMapping("login")
    public LoginResponse login(@Valid @RequestBody LoginRequest loginRequest){
        var auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.username, loginRequest.password));
        var jwt = tokenService.generateToken(auth.getName());
        return new LoginResponse(auth.getName(), "Bearer",  jwt);
    }
}
