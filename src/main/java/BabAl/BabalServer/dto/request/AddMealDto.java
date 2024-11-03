package BabAl.BabalServer.dto.request;

import BabAl.BabalServer.domain.Food;
import BabAl.BabalServer.domain.User;
import BabAl.BabalServer.domain.enums.Mealtime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AddMealDto {
    private Mealtime mealtime;
    private int carbohydrate;
    private int protein;
    private int fat;
    private int calories;
    private String foodName;

    @Builder
    public Food toEntity(User user) {
        return Food.builder()
                .user(user)
                .mealtime(mealtime)
                .name(foodName)
                .carbohydrate(carbohydrate)
                .protein(protein)
                .fat(fat)
                .calories(calories)
                .build();
    }
}
