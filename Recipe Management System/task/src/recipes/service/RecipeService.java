package recipes.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import recipes.model.Recipe;
import recipes.repository.RecipeRepository;

import java.util.List;
import java.util.Optional;

@Service
public class RecipeService {

    private static final Logger log = LoggerFactory.getLogger(RecipeService.class);
    private final RecipeRepository recipeRepository;

    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public Recipe saveRecipe(Recipe recipe) {
        log.debug(recipe.toString());
        return recipeRepository.save(recipe);
    }

    public Optional<Recipe> getRecipe(String id) {
        return recipeRepository.findById(Long.parseLong(id));
    }

    public void deleteRecipe(Long id) {
        recipeRepository.deleteById(id);
    }

    public List<Recipe> searchRecipes(String category, String name) {
        if (category != null) {
            return recipeRepository.findByCategoryIgnoreCaseOrderByDateDesc(category);
        } else if (name != null) {
            return recipeRepository.findByNameContainingIgnoreCaseOrderByDateDesc(name);
        }
        return List.of();
    }
}
