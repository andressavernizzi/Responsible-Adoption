package org.responsibleadoption.controller;

import java.util.List;
import java.util.Optional;

import org.responsibleadoption.model.Category;
import org.responsibleadoption.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/category")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CategoryController {

	@Autowired
	private CategoryRepository categoryRepository;
	
	@GetMapping
	public String renderAllCategories(Model model) {
	    List<Category> categories = categoryRepository.findAll();
	    model.addAttribute("categories", categories);
	    return "AllCategories";
	}
	
	@GetMapping("/category/{categoryId}")
	public String getAnimalPage(@PathVariable Long categoryId, Model model) {
	    Category category = categoryRepository.findById(categoryId).orElse(null);
	    if (category == null) {
	        return "error";
	    }
	    model.addAttribute("category", category);
	    return "Category_details";
	}
	
	@GetMapping("/create")
    public String getCreatePage() {
        return "CreateCategory";
    }
	
	@GetMapping("/update/{id}")
	public String getUpdatePage(Model model, @PathVariable Long id) {
	    Category category = categoryRepository.findById(id).orElse(null);
	    model.addAttribute("category", category);
	    return "UpdateCategory";
	}
	
	@GetMapping("/")
	public ResponseEntity<List<Category>> getAll() {
		return ResponseEntity.ok(categoryRepository.findAll());
	}
	
	@PostMapping("/create")
	public String createCategory(@RequestParam("description") String description) {
	    Category category = new Category();
	    category.setDescription(description);
	    categoryRepository.save(category);
	    return "redirect:/category";
	}
	
	@PostMapping("/update/{id}")
	public String updateCategory(@PathVariable Long id, @ModelAttribute("category") Category category) {
	    Category existingCategory = categoryRepository.findById(id).orElse(null);
	    
	    if (existingCategory != null) {
	        existingCategory.setDescription(category.getDescription());
	        categoryRepository.save(existingCategory);
	    }
	    
	    return "redirect:/category";
	}
	
	@GetMapping("/delete/{id}")
	public String deleteCategory(@PathVariable Long id) {
	    categoryRepository.deleteById(id);
	    return "redirect:/category";
	}

	@GetMapping("/{id}")
	public ResponseEntity<Category> getById(@PathVariable Long id) {
		return categoryRepository.findById(id).map(resposta -> ResponseEntity.ok(resposta))
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}

	@PostMapping
	public ResponseEntity<Category> post(@Valid @RequestBody Category category) {
		return ResponseEntity.status(HttpStatus.CREATED).body(categoryRepository.save(category));
	}

	@PutMapping
	public ResponseEntity<Category> put(@Valid @RequestBody Category category) {
		return categoryRepository.findById(category.getId())
				.map(response -> ResponseEntity.status(HttpStatus.CREATED).body(categoryRepository.save(category)))
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {
		Optional<Category> category = categoryRepository.findById(id);

		if (category.isEmpty())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);

		categoryRepository.deleteById(id);
	}

}
