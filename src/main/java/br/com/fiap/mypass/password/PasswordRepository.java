package br.com.fiap.mypass.password;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PasswordRepository extends JpaRepository<Password, Long> {

    List<Password> findAllByOwnerUsername(String ownerUsername);
}
