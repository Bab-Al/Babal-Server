package BabAl.BabalServer.dto.response;

import BabAl.BabalServer.domain.Food;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

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

    public static List<MainStatisticsResponseDto> mainStatisticsResponse(List<Food> foodList) {
        return foodList.stream()
                .map(m -> MainStatisticsResponseDto.builder()
                        .date(m.getCreatedAt())
                        .carbohydrate(m.getCarbohydrate())
                        .protein(m.getProtein())
                        .fat(m.getFat())
                        .kcal((m.getCarbohydrate() * 4) + (m.getProtein() * 4) + (m.getFat() * 9))
                        .build())
                .collect(Collectors.toList());
    }

    public static MainStatisticsResponseDto createWithDefaults(LocalDate date) {
        return MainStatisticsResponseDto.builder()
                .date(date)
                .carbohydrate(0)
                .protein(0)
                .fat(0)
                .kcal(0)
                .build();
    }
}
