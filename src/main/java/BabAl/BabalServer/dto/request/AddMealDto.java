package BabAl.BabalServer.dto.request;

import BabAl.BabalServer.domain.enums.Mealtime;
import lombok.AllArgsConstructor;
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
    private String foodName;
    private MultipartFile foodImage;
}
