package recipes.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String category;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    private String description;

    @ElementCollection
    private List<String> ingredients;

    @ElementCollection
    private List<String> directions;

    @PrePersist
    public void onCreate() {
        this.date = new Date();
    }

    @PreUpdate
    public void onUpdate() {
        this.date = new Date();
    }
}
