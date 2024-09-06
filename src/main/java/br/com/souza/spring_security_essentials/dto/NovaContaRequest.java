package br.com.souza.spring_security_essentials.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NovaContaRequest {

    private String username;
    private String password;
    private String roleName;
}
