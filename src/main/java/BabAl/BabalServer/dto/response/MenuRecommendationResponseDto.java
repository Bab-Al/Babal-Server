package BabAl.BabalServer.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuRecommendationResponseDto {
    private String menuName;
    private int calories;
    private int carbo;
    private int protein;
    private int fat;
}
