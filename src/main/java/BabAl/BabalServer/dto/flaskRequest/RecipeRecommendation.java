package BabAl.BabalServer.dto.flaskRequest;

import BabAl.BabalServer.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Optional;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecipeRecommendation {
    private List<String> ingredients;
    private List<String> tags;
    private String gender;
    private int age;

    public static RecipeRecommendation recipeRecommendation(Optional<User> user, List<String> ingredients) {
        return RecipeRecommendation.builder()
                .ingredients(ingredients)
                .tags(user.get().getTagList())
                .gender(String.valueOf(user.get().getGender()))
                .age(user.get().getAge())
                .build();
    }
}
