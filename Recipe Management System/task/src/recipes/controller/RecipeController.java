package recipes.controller;

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

    private final RecipeService recipeService;

    @Autowired
    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @PostMapping("/new")
    public ResponseEntity<?> addRecipe(@RequestBody Recipe recipe) {
        Map<String, Integer> response = new HashMap<>();
        Long id = recipeService.createRecipe(recipe).getId();
        response.put("id", id.intValue());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRecipe(@PathVariable String id) {
        Recipe recipe = recipeService.getRecipe(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe not found"));
        RecipeDTO recipeDTO = new RecipeDTO(recipe.getName(), recipe.getDescription(), recipe.getIngredients(), recipe.getDirections());
        return ResponseEntity.ok(recipeDTO);
    }
}
