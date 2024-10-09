package BabAl.BabalServer.domain;

import BabAl.BabalServer.domain.common.BaseEntity;
import BabAl.BabalServer.domain.enums.Mealtime;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Food extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // 아침, 점심, 저녁 중 선택
    @Enumerated(EnumType.STRING)
    private Mealtime mealtime;

    // 음식 이름
    @Column(length = 20)
    private String name;

    // 탄수화물량
    private int carbohydrate;

    // 단백질량
    private int protein;

    // 지방량
    private int fat;

    // 칼로리
    private int calories;
}
