package recipes.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import recipes.DTO.RecipeDTO;
import recipes.model.Recipe;
import recipes.service.RecipeService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/recipe")
public class RecipeController {

    private static final Logger log = LoggerFactory.getLogger(RecipeController.class);
    private final RecipeService recipeService;

    @Autowired
    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @PostMapping("/new")
    public ResponseEntity<?> addRecipe(@RequestBody Recipe recipe) {
        if (recipe.getName() == null || recipe.getName().trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Name cannot be blank");
        }
        if (recipe.getDescription() == null || recipe.getDescription().trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Description cannot be blank");
        }
        if (recipe.getIngredients() == null || recipe.getIngredients().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ingredients cannot be empty");
        }
        if (recipe.getDirections() == null || recipe.getDirections().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Directions cannot be empty");
        }

        Map<String, Integer> response = new HashMap<>();
        Recipe savedRecipe = recipeService.createRecipe(recipe);
        log.info(savedRecipe.toString());
        Long id = savedRecipe.getId();
        response.put("id", id.intValue());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRecipe(@PathVariable String id) {
        Recipe recipe = recipeService.getRecipe(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe not found"));
        RecipeDTO recipeDTO = new RecipeDTO(recipe.getName(), recipe.getDescription(), recipe.getIngredients(), recipe.getDirections());
        return ResponseEntity.ok(recipeDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRecipe(@PathVariable String id) {
        Recipe recipe = recipeService.getRecipe(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe not found"));
        recipeService.deleteRecipe(recipe.getId());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
