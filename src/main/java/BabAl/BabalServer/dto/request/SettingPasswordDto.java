package BabAl.BabalServer.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SettingPasswordDto {
    private String password;
    private String newPassword;
    private String confirmPassword;
}
