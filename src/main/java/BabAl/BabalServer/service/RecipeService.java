package BabAl.BabalServer.service;

import BabAl.BabalServer.dto.request.RecipeRecommendationsDto;
import BabAl.BabalServer.dto.response.RecipeIngredientsResponseDto;
import BabAl.BabalServer.dto.response.RecipeRecommendationsResponseDto;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface RecipeService {

    // 레시피 검색
    RecipeIngredientsResponseDto getIngredients(String alpha) throws JsonProcessingException;

    // 레시피 추천
    RecipeRecommendationsResponseDto getRecommendations(String userEmail, RecipeRecommendationsDto ingredients) throws JsonProcessingException;
}
