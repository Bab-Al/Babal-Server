package BabAl.BabalServer.dto.response;

import BabAl.BabalServer.domain.User;
import BabAl.BabalServer.domain.enums.UserGender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SettingResponseDto {
    private String username;
    private int height;
    private int weight;
    private int age;
    private UserGender gender;
    private int bmr;

    public static SettingResponseDto settingResponse(User user) {
        return SettingResponseDto.builder()
                .username(user.getName())
                .height(user.getHeight())
                .weight(user.getWeight())
                .age(user.getAge())
                .gender(user.getGender())
                .bmr(user.getBmr())
                .build();
    }
}
