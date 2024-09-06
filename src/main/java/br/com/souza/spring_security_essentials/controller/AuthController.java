package br.com.souza.spring_security_essentials.controller;

import br.com.souza.spring_security_essentials.dto.LoginRequest;
import br.com.souza.spring_security_essentials.dto.NovaContaRequest;
import br.com.souza.spring_security_essentials.dto.TokenResponse;
import br.com.souza.spring_security_essentials.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/auth")
public class AuthController {

    private final AuthService service;

    @PostMapping("/signup")
    public ResponseEntity<String> criarConta(@RequestBody NovaContaRequest request) {
        service.criarConta(request);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest request) throws Exception {
        return new ResponseEntity<>(service.login(request), HttpStatus.CREATED);
    }
}
