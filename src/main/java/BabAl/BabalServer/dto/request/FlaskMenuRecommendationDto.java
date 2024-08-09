package BabAl.BabalServer.dto.request;

import BabAl.BabalServer.domain.User;
import BabAl.BabalServer.domain.enums.UserGender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FlaskMenuRecommendationDto {
    private List<String> foodCategoryNameList;
    private String gender;
    private int age;

    public static FlaskMenuRecommendationDto flaskRequestDto(User user) {
        Map<String, String> categoryMapping = new HashMap<>();
        categoryMapping.put("FRUIT", "과일류");
        categoryMapping.put("SOUP","국 및 탕류");
        categoryMapping.put("NOODLE","면 및 만두류");
        categoryMapping.put("RICE","밥류");
        categoryMapping.put("BREAD","빵 및 과자류");
        categoryMapping.put("RAW_VEGES","생채·무침류");
        categoryMapping.put("TEA","음료 및 차류");
        categoryMapping.put("STIR_FRY","볶음류");
        categoryMapping.put("DAIRY_PRODS","유제품류 및 빙과류");
        categoryMapping.put("PICKLED","장아찌·절임류");
        categoryMapping.put("JEOTGAL","젓갈류");
        categoryMapping.put("HOTPOT","찌개 및 전골류");
        categoryMapping.put("SEASONED_VEGETABLES","나물·숙채류");
        categoryMapping.put("KIMCHI","김치류");
        categoryMapping.put("STEAMED","찜류");
        categoryMapping.put("BOILED","조림류");
        categoryMapping.put("FRIED","튀김류");
        categoryMapping.put("PORRIDGE","죽 및 스프류");
        categoryMapping.put("PANCAKE","전·적 및 부침류");
        categoryMapping.put("SEASONING","장류, 양념류");
        categoryMapping.put("FISH","수·조·어·육류");
        categoryMapping.put("NUT","두류, 견과 및 종실류");
        categoryMapping.put("GRILLED", "구이류");

        List<String> switchCategoryNameToKorean = user.getFoodCategoryNameList().stream()
                .map(name -> categoryMapping.getOrDefault(name, "빵 및 과자류"))
                .collect(Collectors.toList());

        return FlaskMenuRecommendationDto.builder()
                .foodCategoryNameList(switchCategoryNameToKorean)
                .gender(enumToString(user.getGender(), user.getAge()))
                .age(user.getAge())
                .build();
    }

    private static String enumToString(UserGender gender, int age) {
        if (age < 6) {
            return "유아";
        }
        if (gender == UserGender.MAN) {
            return "남자";
        } else {
            return "여자";
        }
    }
}
