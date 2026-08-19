package br.com.fiap.mypass.password;

import br.com.fiap.mypass.user.User;
import br.com.fiap.mypass.user.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PasswordService {

    private final PasswordRepository passwordRepository;

    private final UserRepository userRepository;

    public PasswordService(PasswordRepository passwordRepository, UserRepository userRepository) {
        this.passwordRepository = passwordRepository;
        this.userRepository = userRepository;
    }

    public List<PasswordDTO.PasswordResponse> getAllPasswords() {
        return passwordRepository.findAll()
                .stream()
                .map(password -> new PasswordDTO.PasswordResponse(password.getId(), password.getUrl(), password.getPassword(), password.getUsername()))
                .collect(Collectors.toList());
    }

    public List<PasswordDTO.PasswordResponse> getAllPasswordsByOwner(String ownerUsername) {
        return passwordRepository.findAllByOwnerUsername(ownerUsername)
                .stream()
                .map(password -> new PasswordDTO.PasswordResponse(password.getId(), password.getUrl(), password.getPassword(), password.getUsername()))
                .collect(Collectors.toList());
    }

    public PasswordDTO.PasswordResponse createPassword(PasswordDTO.PasswordRequest request, String ownerUsername) {
        User owner = userRepository.findByUsername(ownerUsername)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + ownerUsername));

        Password password = new Password();
        password.setUrl(request.url());
        password.setUsername(request.username());
        password.setPassword(request.password());
        password.setOwner(owner);

        Password saved = passwordRepository.save(password);
        return new PasswordDTO.PasswordResponse(saved.getId(), saved.getUrl(), saved.getPassword(), saved.getUsername());
    }
}
