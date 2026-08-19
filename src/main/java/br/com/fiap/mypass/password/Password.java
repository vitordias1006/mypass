package br.com.fiap.mypass.password;

import br.com.fiap.mypass.user.User;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "passwords")
public class Password {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String url;

    private String password;

    private String username;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;
}
