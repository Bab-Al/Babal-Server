package BabAl.BabalServer.dto.flaskRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IngredientAlpha {
    private String alphabet;

    public static IngredientAlpha ingredientAlpha(String alpha) {
        return IngredientAlpha.builder().alphabet(alpha).build();
    }
}
