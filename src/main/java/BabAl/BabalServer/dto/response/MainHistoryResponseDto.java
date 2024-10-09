package BabAl.BabalServer.dto.response;

import BabAl.BabalServer.domain.Food;
import BabAl.BabalServer.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MainHistoryResponseDto {
    int userTotalKcal;
    int userNowKcal;
    int userCarbo;
    int userProtein;
    int userFat;
    String breakfastName;
    int breakfastKcal;
    String lunchName;
    int lunchKcal;
    String dinnerName;
    int dinnerKcal;

    public static MainHistoryResponseDto mainHistoryResponse(User user, List<Food> foodList) {
        // 영양소 및 식사 정보 초기화 (저장된 기록이 없을 수도 있음)
        int totalCarbo = 0, totalProtein = 0, totalFat = 0, totalKcal = 0;
        String breakfastName = null, lunchName = null, dinnerName = null;
        int breakfastKcal = 0, lunchKcal = 0, dinnerKcal = 0;

        for (Food food : foodList) {
            int foodKcal = food.getCalories(); //(food.getCarbohydrate() * 4) + (food.getProtein() * 4) + (food.getFat() * 9);
            totalCarbo += food.getCarbohydrate();
            totalProtein += food.getProtein();
            totalFat += food.getFat();
            totalKcal += foodKcal;

            // 식사별로 나누어서 저장
            switch (food.getMealtime()) {
                case BREAKFAST:
                    breakfastName = food.getName();
                    breakfastKcal += foodKcal;
                    break;
                case LUNCH:
                    lunchName = food.getName();
                    lunchKcal += foodKcal;
                    break;
                case DINNER:
                    dinnerName = food.getName();
                    dinnerKcal += foodKcal;
                    break;
            }
        }

        return MainHistoryResponseDto.builder()
                .userTotalKcal(user.getBmr())
                .userNowKcal(totalKcal)
                .userCarbo(totalCarbo)
                .userProtein(totalProtein)
                .userFat(totalFat)
                .breakfastName(breakfastName)
                .breakfastKcal(breakfastKcal)
                .lunchName(lunchName)
                .lunchKcal(lunchKcal)
                .dinnerName(dinnerName)
                .dinnerKcal(dinnerKcal)
                .build();
    }
}
