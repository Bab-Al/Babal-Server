package BabAl.BabalServer.dto.request;

import BabAl.BabalServer.domain.User;
import BabAl.BabalServer.domain.enums.FoodCategoryName;
import BabAl.BabalServer.domain.enums.UserGender;
import BabAl.BabalServer.domain.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SignUpDto {
    private String name;
    private String email;
    private String password;
    private int age;
    private UserGender gender;
    private int height;
    private int weight;
    private int activity;
    private List<FoodCategoryName> foodCategory;
    
    @Builder
    public User toEntity() {

        int calculatedBmr = 0;
        
        // bmr 계산 로직 추가
        if (gender.equals(UserGender.MAN)) {
            calculatedBmr = totalBmr(bmrMan(weight, height, age), activity);
        } else if (gender.equals(UserGender.WOMAN)) {
            calculatedBmr = totalBmr(bmrWoman(weight, height, age), activity);
        }

        return User.builder()
                .name(name)
                .email(email)
                .password(password)
                .age(age)
                .gender(gender)
                .height(height)
                .weight(weight)
                .bmr(calculatedBmr)
                .foodCategoryNameList(foodCategory)
                .role(UserRole.USER)
                .cameraAddress(null)
                .build();
    }

    public int bmrMan(int weight, int height, int age) {
        double bmr = 66.5 + (13.75 * weight) + (5.003 * height) - (6.75 * age);
        return (int) bmr;
    }

    public int bmrWoman(int weight, int height, int age) {
        double bmr = 655.1 + (9.563 * weight) + (1.850 * height) - (4.676 * age);
        return (int) bmr;
    }

    public int totalBmr(int bmr, int activity) {
        return switch (activity) {
            case 0 -> (int) (bmr * 1.2);
            case 1 -> (int) (bmr * 1.375);
            case 2 -> (int) (bmr * 1.55);
            case 3 -> (int) (bmr * 1.725);
            case 4 -> (int) (bmr * 1.9);
            default -> throw new IllegalArgumentException("Unknown activity level: " + activity);
        };
    }
}
