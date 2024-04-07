package BabAl.BabalServer.domain;

import BabAl.BabalServer.domain.common.BaseEntity;
import BabAl.BabalServer.domain.enums.FoodCategoryName;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class FoodCategory extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 음식 카테고리 이름
    private FoodCategoryName name;
}
