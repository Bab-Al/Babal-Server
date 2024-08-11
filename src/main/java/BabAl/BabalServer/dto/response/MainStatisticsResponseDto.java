package BabAl.BabalServer.dto.response;

import BabAl.BabalServer.domain.Food;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MainStatisticsResponseDto {
    private LocalDate date;
    private int carbohydrate;
    private int protein;
    private int fat;
    private int kcal;

    public static MainStatisticsResponseDto mainStatisticsResponse(Food food) {
        return MainStatisticsResponseDto.builder()
                .date(food.getCreatedAt())
                .carbohydrate(food.getCarbohydrate())
                .protein(food.getProtein())
                .fat(food.getFat())
                .kcal((food.getCarbohydrate() * 4) + (food.getProtein() * 4) + (food.getFat() * 9))
                .build();
    }
}
