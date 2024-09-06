package br.com.souza.spring_security_essentials.service;

import br.com.souza.spring_security_essentials.config.TokenProvider;
import br.com.souza.spring_security_essentials.database.model.Users;
import br.com.souza.spring_security_essentials.database.repository.IUsersRepository;
import br.com.souza.spring_security_essentials.dto.LoginRequest;
import br.com.souza.spring_security_essentials.dto.NovaContaRequest;
import br.com.souza.spring_security_essentials.dto.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final IUsersRepository usersRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;

    public void criarConta(NovaContaRequest request) {
        usersRepository.save(Users.builder()
                .identifier(UUID.randomUUID().toString())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(Collections.singletonList(roleService.getRoleByName(request.getRoleName())))
                .build());
    }

    public TokenResponse login(LoginRequest request) throws Exception {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
            return tokenProvider.generateToken(authentication);
        } catch (Exception e) {
            throw new Exception("Erro ao autenticar: " + e.getMessage());
        }
    }
}
