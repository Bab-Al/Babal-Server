package BabAl.BabalServer.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RecipeTagsResponseDto {
    private int count;
    private List<String> tags;
}
