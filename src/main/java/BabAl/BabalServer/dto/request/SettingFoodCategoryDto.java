package BabAl.BabalServer.dto.request;

import BabAl.BabalServer.domain.enums.FoodCategoryName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SettingFoodCategoryDto {
    private List<FoodCategoryName> foodCategory;
}
