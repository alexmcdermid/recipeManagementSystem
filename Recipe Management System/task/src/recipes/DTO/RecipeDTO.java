package recipes.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
public class RecipeDTO {
    private String name;
    private String category;
    private Date date;
    private String description;
    private List<String> ingredients;
    private List<String> directions;
}
