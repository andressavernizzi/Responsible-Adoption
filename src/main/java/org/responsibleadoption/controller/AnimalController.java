package org.responsibleadoption.controller;

import java.util.List;
import java.util.Optional;

import org.responsibleadoption.model.Animal;
import org.responsibleadoption.repository.AnimalRepository;
import org.responsibleadoption.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/animal")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AnimalController {

	@Autowired
	private AnimalRepository animalRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@GetMapping
	public ResponseEntity<List<Animal>> getAll() {
		return ResponseEntity.ok(animalRepository.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Animal> getById(@PathVariable Long id) {
		return animalRepository.findById(id).map(response -> ResponseEntity.ok(response))
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}

	@GetMapping("/age/{age}")
	public ResponseEntity<List<Animal>> getByAge(@PathVariable String age) {
		return ResponseEntity.ok(animalRepository.findAllByAgeContainingIgnoreCase(age));
	}

	@PostMapping
	public ResponseEntity<Animal> post(@Valid @RequestBody Animal animal) {
		if (categoryRepository.existsById(animal.getCategory().getId()))
			return ResponseEntity.status(HttpStatus.CREATED).body(animalRepository.save(animal));

		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category does not exist!", null);
	}

	@PutMapping
	public ResponseEntity<Animal> put(@Valid @RequestBody Animal animal) {
		if (animalRepository.existsById(animal.getId())) {

			if (categoryRepository.existsById(animal.getCategory().getId()))
				return ResponseEntity.status(HttpStatus.OK).body(animalRepository.save(animal));

			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category does not exist!", null);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {
		Optional<Animal> animal = animalRepository.findById(id);

		if (animal.isEmpty())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);

		animalRepository.deleteById(id);
	}

}
