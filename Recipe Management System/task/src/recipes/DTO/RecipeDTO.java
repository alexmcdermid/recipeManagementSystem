package recipes.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class RecipeDTO {
    private String name;
    private String description;
    private List<String> ingredients;
    private List<String> directions;
}
