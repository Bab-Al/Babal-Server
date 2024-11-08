package BabAl.BabalServer.dto.flaskResponse;

import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecipeRecommendationResponse {
    private String name;
    private int minutes;
    private float calories;
    private float carbohydrate;
    private float protein;
    private float fat;
    private int n_steps;
    private String steps;
    private int n_ingredients;
    private List<String> ingredients;
}
