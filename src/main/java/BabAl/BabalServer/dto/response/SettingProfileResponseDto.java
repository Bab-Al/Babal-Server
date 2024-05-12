package BabAl.BabalServer.dto.response;

import BabAl.BabalServer.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SettingProfileResponseDto {
    private String username;
    private String email;

    public static SettingProfileResponseDto settingProfileResponse(User user) {
        return SettingProfileResponseDto.builder()
                .username(user.getName())
                .email(user.getEmail())
                .build();
    }
}
