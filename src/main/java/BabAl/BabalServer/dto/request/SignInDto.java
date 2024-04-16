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
public class SignInDto {
    private String name;
    private String email;
    private String password;
    private int age;
    private String gender;
    private int height;
    private int weight;
    private List<FoodCategoryName> foodCategory;
    
    @Builder
    public User toEntity() {
        
        // 1. bmr 계산 로직 추가
        // 2. food category 처리

        return User.builder()
                .name(name)
                .email(email)
                .password(password)
                .age(age)
                .gender(UserGender.valueOf(gender))
                .height(height)
                .weight(weight)
                .bmr(0)
                .foodCategoryNameList(foodCategory)
                .role(UserRole.USER)
                .build();
    }
}
