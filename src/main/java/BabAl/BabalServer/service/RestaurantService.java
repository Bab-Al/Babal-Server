package BabAl.BabalServer.service;

import BabAl.BabalServer.dto.response.MenuRecommendationResponseDto;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface RestaurantService {

    // 메뉴 추천 결과 조회
    List<MenuRecommendationResponseDto> recommendMenuList(String userEmail) throws JsonProcessingException;
}
