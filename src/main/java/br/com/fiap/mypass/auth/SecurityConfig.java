package br.com.fiap.mypass.auth;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@ConfigurationPropertiesScan
public class SecurityConfig {

    @ConfigurationProperties(prefix = "rsa")
    public record RsaKeyProperties(RSAPublicKey publicKey, RSAPrivateKey privateKey) {}

    @Bean
    SecurityFilterChain config(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/login").permitAll()
                        .requestMatchers(HttpMethod.GET, "/pass").authenticated()
                        .anyRequest().authenticated()
                )
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(withDefaults())
                .oauth2ResourceServer(auth -> auth.jwt(withDefaults()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }

    @Bean
    UserDetailsManager users(){
        UserDetails vitor = User.withUsername("vitor").password("{noop}123456").build();
        UserDetails maria = User.withUsername("maria").password("{noop}123456").build();
        return new InMemoryUserDetailsManager(vitor, maria);
    }

    @Bean
    JwtDecoder jwtDecoder(RsaKeyProperties rsaKeyProperties){
        return NimbusJwtDecoder
                .withPublicKey(rsaKeyProperties.publicKey())
                .build();
    }

    @Bean
    JwtEncoder jwtEncoder(RsaKeyProperties rsaKeyProperties){
        var privateKey = rsaKeyProperties.privateKey();
        var publicKey = rsaKeyProperties.publicKey();
        RSAKey rsaKey = new RSAKey.Builder(publicKey).privateKey(privateKey).build();
        ImmutableJWKSet<SecurityContext> jwtSource = new ImmutableJWKSet<>(new JWKSet(rsaKey));
        return new NimbusJwtEncoder(jwtSource);
    }
}
