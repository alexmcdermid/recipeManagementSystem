package recipes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import recipes.DTO.RecipeDTO;
import recipes.model.Recipe;
import recipes.model.User;
import recipes.repository.UserRepository;
import recipes.service.RecipeService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/recipe")
public class RecipeController {

    private final RecipeService recipeService;
    private final UserRepository userRepository;

    @Autowired
    public RecipeController(RecipeService recipeService, UserRepository userRepository) {
        this.recipeService = recipeService;
        this.userRepository = userRepository;
    }

    @PostMapping("/new")
    public ResponseEntity<?> addRecipe(@RequestBody Recipe recipe) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByUsername(currentUsername).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));
        recipe.setUserId(currentUser.getId());

        validateSaveRecipe(recipe);
        Map<String, Integer> response = new HashMap<>();
        Recipe savedRecipe = recipeService.saveRecipe(recipe);
        Long id = savedRecipe.getId();
        response.put("id", id.intValue());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRecipe(@PathVariable Long id, @RequestBody Recipe recipe) {
        Recipe existingRecipe = recipeService.getRecipe(String.valueOf(id)).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe not found"));
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByUsername(currentUsername).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));

        if (!existingRecipe.getUserId().equals(currentUser.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not the author of this recipe");
        }

        existingRecipe.setName(recipe.getName());
        existingRecipe.setCategory(recipe.getCategory());
        existingRecipe.setDescription(recipe.getDescription());
        existingRecipe.setIngredients(recipe.getIngredients());
        existingRecipe.setDirections(recipe.getDirections());
        validateSaveRecipe(existingRecipe);
        recipeService.saveRecipe(existingRecipe);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRecipe(@PathVariable String id) {
        Recipe recipe = recipeService.getRecipe(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe not found"));
        RecipeDTO recipeDTO = new RecipeDTO(recipe.getName(), recipe.getCategory(), recipe.getDate(), recipe.getDescription(), recipe.getIngredients(), recipe.getDirections());
        return ResponseEntity.ok(recipeDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRecipe(@PathVariable String id) {
        Recipe recipe = recipeService.getRecipe(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe not found"));
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByUsername(currentUsername).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));
        if (!recipe.getUserId().equals(currentUser.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not the author of this recipe");
        }
        recipeService.deleteRecipe(recipe.getId());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/search/")
    public ResponseEntity<?> searchRecipe(@RequestParam(required = false) String category, @RequestParam(required = false) String name) {
        if ((category != null && name != null) || (category == null && name == null)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid request parameters");
        }

        List<Recipe> recipes = recipeService.searchRecipes(category, name);

        List<RecipeDTO> recipeDTOs = recipes.stream()
                .map(recipe -> new RecipeDTO(recipe.getName(), recipe.getCategory(), recipe.getDate(), recipe.getDescription(), recipe.getIngredients(), recipe.getDirections())).toList();

        return ResponseEntity.ok(recipeDTOs);
    }

    private void validateSaveRecipe(Recipe recipe) {
        if (recipe.getName() == null || recipe.getName().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name cannot be blank");
        }
        if (recipe.getCategory() == null || recipe.getCategory().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category cannot be blank");
        }
        if (recipe.getDescription() == null || recipe.getDescription().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Description cannot be blank");
        }
        if (recipe.getIngredients() == null || recipe.getIngredients().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ingredients cannot be empty");
        }
        if (recipe.getDirections() == null || recipe.getDirections().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Directions cannot be empty");
        }
    }
}
