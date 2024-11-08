package BabAl.BabalServer.controller;

import BabAl.BabalServer.apiPayload.ApiResponse;
import BabAl.BabalServer.dto.response.RecipeIngredientsResponseDto;
import BabAl.BabalServer.jwt.JwtUtil;
import BabAl.BabalServer.service.RecipeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/recipe")
@Tag(name = "Recipe Recommendation Page", description = "Recipe API")
public class RecipeController {

    private final RecipeService recipeService;

    @GetMapping() // 레시피 재료 검색
    @Operation(summary = "레시피 재료 검색", description = "레시피 재료 검색할 때 사용하는 API")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청이 정상 처리되었습니다", content = @Content(mediaType = "application/json")),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "MEMBER4001", description = "사용자가 없습니다", content = @Content(mediaType = "application/json"))
    })
    public ApiResponse<RecipeIngredientsResponseDto> getIngredients(@RequestParam String alpha) throws JsonProcessingException {
        return ApiResponse.onSuccess(recipeService.getIngredients(alpha));
    }
}