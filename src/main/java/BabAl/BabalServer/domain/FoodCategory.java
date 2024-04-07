package BabAl.BabalServer.domain;

import BabAl.BabalServer.domain.common.BaseEntity;
import BabAl.BabalServer.domain.enums.FoodCategoryName;
import BabAl.BabalServer.domain.mapping.UserFoodCategory;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class FoodCategory extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "foodCategory", cascade = CascadeType.ALL)
    private List<UserFoodCategory> userFoodCategoryList = new ArrayList<>();

    // 음식 카테고리 이름
    private FoodCategoryName name;
}
