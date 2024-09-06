package br.com.souza.spring_security_essentials.database.repository;

import br.com.souza.spring_security_essentials.database.model.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IRolesRepository extends JpaRepository<Roles, String> {

    Optional<Roles> findByName(String name);
}
