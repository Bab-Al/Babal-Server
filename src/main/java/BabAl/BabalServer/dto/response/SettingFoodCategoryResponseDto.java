package BabAl.BabalServer.dto.response;

import BabAl.BabalServer.domain.enums.FoodCategoryName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SettingFoodCategoryResponseDto {
    private List<FoodCategoryName> foodCategoryList;

    public static SettingFoodCategoryResponseDto settingFoodCategoryResponse(List<FoodCategoryName> foodCategoryList) {
        return SettingFoodCategoryResponseDto.builder()
                .foodCategoryList(foodCategoryList)
                .build();
    }
}
