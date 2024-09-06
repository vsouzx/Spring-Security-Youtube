package br.com.souza.spring_security_essentials.database.repository;

import br.com.souza.spring_security_essentials.database.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUsersRepository extends JpaRepository<Users, String> {

    Optional<Users> findByUsername(String username);

}
