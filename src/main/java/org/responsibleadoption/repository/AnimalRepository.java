package org.responsibleadoption.repository;

import java.util.List;

import org.responsibleadoption.model.Animal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface AnimalRepository extends JpaRepository<Animal, Long>{
	
	public List<Animal> findAllByAgeContainingIgnoreCase(@Param("age")String age);

}
