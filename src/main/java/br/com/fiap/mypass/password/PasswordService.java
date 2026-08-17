package br.com.fiap.mypass.password;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PasswordService {

    private final PasswordRepository passwordRepository;

    public PasswordService(PasswordRepository passwordRepository) {
        this.passwordRepository = passwordRepository;
    }

    public List<PasswordResponse> getAllPasswords() {
        return passwordRepository.findAll()
                .stream()
                .map(password -> new PasswordResponse(password.getId(), password.getUrl(), password.getPassword(), password.getUsername()))
                .collect(Collectors.toList());
    }
}
