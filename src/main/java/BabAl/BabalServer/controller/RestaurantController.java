package BabAl.BabalServer.controller;

import BabAl.BabalServer.apiPayload.ApiResponse;
import BabAl.BabalServer.dto.response.MenuRecommendationResponseDto;
import BabAl.BabalServer.jwt.JwtUtil;
import BabAl.BabalServer.service.RestaurantService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/restaurant")
@Tag(name = "RESTAURANT", description = "Restaurant Recommendation API")
public class RestaurantController {

    private final RestaurantService restaurantService;

    @GetMapping()
    @Operation(summary = "메뉴 추천", description = "메뉴 추천 결과 조회 시 사용하는 API")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청이 정상 처리되었습니다", content = @Content(mediaType = "application/json")),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "MEMBER4003", description = "이미 등록된 사용자입니다", content = @Content(mediaType = "application/json"))
    })
    public ApiResponse<List<MenuRecommendationResponseDto>> recommendMenu(@RequestHeader("Authorization") String token) throws JsonProcessingException {
        String userEmail = JwtUtil.getEmail(token.substring(7));
        return ApiResponse.onSuccess(restaurantService.recommendMenuList(userEmail));
    }
}
