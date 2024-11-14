package BabAl.BabalServer.service;

import BabAl.BabalServer.dto.request.RecipeRecommendationsDto;
import BabAl.BabalServer.dto.response.RecipeIngredientsResponseDto;
import BabAl.BabalServer.dto.response.RecipeRecommendationsResponseDto;
import BabAl.BabalServer.dto.response.RecipeTagsResponseDto;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface RecipeService {

    // 레시피 재료 검색
    RecipeIngredientsResponseDto getIngredients(String alpha) throws JsonProcessingException;

    // 레시피 태그 검색
    RecipeTagsResponseDto getTags(String alpha) throws JsonProcessingException;

    // 레시피 추천
    List<RecipeRecommendationsResponseDto> getRecommendations(String userEmail, RecipeRecommendationsDto ingredients) throws JsonProcessingException;
}
