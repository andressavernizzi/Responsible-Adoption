package org.responsibleadoption.repository;

import java.util.Optional;

import org.responsibleadoption.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>{
	
	public Optional<User> findByUser(String user);

}
