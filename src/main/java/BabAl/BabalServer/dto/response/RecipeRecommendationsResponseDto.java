package BabAl.BabalServer.dto.response;

import BabAl.BabalServer.dto.flaskResponse.RecipeRecommendationResponse;
import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecipeRecommendationsResponseDto {
    private String name;
    private int minutes;
    private float calories;
    private float carbohydrate;
    private float protein;
    private float fat;
    private int n_steps;
    private List<String> steps;
    private int n_ingredients;
    private List<String> ingredients;

    public static RecipeRecommendationsResponseDto recipeRecommendationsResponse(RecipeRecommendationResponse dto, List<String> stepList) {
        return RecipeRecommendationsResponseDto.builder()
                .name(dto.getName())
                .minutes(dto.getMinutes())
                .calories(dto.getCalories())
                .carbohydrate(dto.getCarbohydrate())
                .protein(dto.getProtein())
                .fat(dto.getFat())
                .n_steps(dto.getN_steps())
                .steps(stepList)
                .n_ingredients(dto.getN_ingredients())
                .ingredients(dto.getIngredients())
                .build();
    }
}
