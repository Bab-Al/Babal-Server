package BabAl.BabalServer.domain;

import BabAl.BabalServer.domain.enums.FoodCategoryName;
import BabAl.BabalServer.domain.enums.UserGender;
import BabAl.BabalServer.domain.common.BaseEntity;
import BabAl.BabalServer.domain.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 사용자 이름
    @Column(length = 20)
    private String name;

    // 이메일
    @Column(length = 50)
    private String email;

    // 비밀번호
    private String password;

    // 나이
    private int age;

    // 성별
    @Enumerated(EnumType.STRING)
    private UserGender gender;

    // 키
    private int height;

    // 몸무게
    private int weight;

    // 기초대사량
    private int bmr;

    // 음식 카테고리
    @ElementCollection
    @Enumerated(EnumType.STRING)
    private List<FoodCategoryName> foodCategoryNameList;

    // spring security role
    @Enumerated(EnumType.STRING)
    private UserRole role;

    public void addUserAuthority() {
        this.role = UserRole.USER;
    }

    public void encodePassword(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(password);
    }

    public void updatePassword(String password) {
        this.password = password;
    }

}
