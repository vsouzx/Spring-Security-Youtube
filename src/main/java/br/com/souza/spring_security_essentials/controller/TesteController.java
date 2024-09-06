package br.com.souza.spring_security_essentials.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/test")
public class TesteController {

    @GetMapping("/liberado")
    public ResponseEntity<String> endpointLiberado(){
        return new ResponseEntity<>("Liberado", HttpStatus.OK);
    }

    @GetMapping("/requer_autenticacao")
    public ResponseEntity<String> requerAutenticacao(){
        return new ResponseEntity<>("Autenticado", HttpStatus.OK);
    }

    @GetMapping("/requer_autenticacao_admin")
    public ResponseEntity<String> requerAutenticacaoAdmin(){
        return new ResponseEntity<>("Autenticado admin", HttpStatus.OK);
    }
}
