package BabAl.BabalServer.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDto {
    private String token;

    public static LoginResponseDto loginResponse(String token) {
        return LoginResponseDto.builder()
                .token(token)
                .build();
    }
}
