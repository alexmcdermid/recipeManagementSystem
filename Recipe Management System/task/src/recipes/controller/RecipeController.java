package recipes.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import recipes.model.Recipe;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class RecipeController {
    private Recipe recipe;

    @PostMapping("/recipe")
    public ResponseEntity<?> addRecipe(@RequestBody Recipe recipe) {
        this.recipe = recipe;
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/recipe")
    public Map<String, String> getRecipe() {
        Map<String, String> response = new HashMap<>();
        response.put("name", recipe.getName());
        response.put("description", recipe.getDescription());
        response.put("ingredients", recipe.getIngredients());
        response.put("directions", recipe.getDirections());
        return response;
    }
}
